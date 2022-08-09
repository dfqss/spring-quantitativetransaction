package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.CoreIndexMapper;
import io.github.talelin.latticy.mapper.TradingStrategyMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.CoreIndexDO;
import io.github.talelin.latticy.model.TradingStrategyDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量创建或更新核心指数表
 * @author sushuai
 * @date 2022-07-21
 * @desc 在不同服务类的方法中使用Transactional的propagation属性来实现隔离事务。（注意两个方法不在同一个服务类中）
 *       Propagation.REQUIRES_NEW: 不管是否存在事务，都创建一个新的事务，原来的挂起，新的执行完毕，继续执行老的事务
 *       propagation=Propagation.REQUIRED: 如果有事务，那么加入事务，没有的话新创建一个;不指定propagation默认就是这个
 *       通过手动将当前代理对象绑定到当前线程ThreadLocal中，此时可以使事务生效：牺牲性能
 *       ((CoreIndex) AopContext.currentProxy()).updateBatchFilesStatus(fileName);
 */
@Component
@Slf4j
public class CoreIndex extends ServiceImpl<CoreIndexMapper, CoreIndexDO> {

    private static String[] methods = {"setCode","setCodeName","setCurrentCore"} ;

    private Map<String, Map<String, String>> strategyContainer = new HashMap<>() ;

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Autowired
    private BatchFiles batchFiles ;

    @Autowired
    private TradingStrategyMapper tradingStrategyMapper ;

    /**
     * 批量创建或更新核心指数表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateCoreIndex() throws Exception {
        /**
         * 1.获取要读取文件的信息
         */
        BatchFilesDO batchFilesDO = getBatchFile();
        if(null == batchFilesDO) {
            log.info("没有需要计算的核心指标数据文件");
            return;
        }
        // 获取文件路径、文件名称和文件期数
        String filePath = batchFilesDO.getFilePath();
        String fileName = batchFilesDO.getFileName();
        Integer filePeriods = batchFilesDO.getFilePeriods();
        // 将数据更新为：毫秒值-[读取中]
        batchFiles.updateBatchFilesStatus(fileName,
                String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);
        log.info("------start 开始计算核心指数: " + fileName);
        String fullName = filePath + File.separator + fileName;
        List<CoreIndexDO> coreIndexList = null;
        try {
            /**
             * 2.读取excel中核心指标数据
             */
            List<Object> excelDataList = ExcelUtil.
                    readExcel(new File(fullName), 0, 2, methods, CoreIndexDO.class);
            /**
             * 3.根据核心指标文件类型查询核心指标数据
             */
            if(fileName.contains(FileLogoConstant.REC8)) {
                log.info("本次处理的是REC8文件: " + fileName);
            }else {
                // 查询核心指数表中的所有信息
                coreIndexList = baseMapper.selectList(null);
            }
            /**
             * 4.新增并更新核心指数表
             */
            if(null == coreIndexList) coreIndexList = new ArrayList<>();
            // 将上一期的数据原封不动插入到历史表中
            backupCoreIndex();
            // 根据执行策略组装或新增数据，并将数据入库
            this.saveOrUpdateBatch(assembleData(coreIndexList, excelDataList));
            // 更新计算日期：更新为当前时间
            updateCalDate(fileName);
            // 更新展示次数
            updateShowTimes();
            // 更新期数
            updatePeriods(filePeriods);
            // 更新数据读取状态为：2-成功
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
            log.info("end------计算核心指数完成: " + fileName);
        }catch (Exception e) {
            log.error("读取核心指标文件失败: " + e);
            // 更新数据读取状态为：1-失败
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE, FileLogoConstant.ERROR);
            throw e;
        }
        return;
    }

    /**
     * 获取要读取文件的信息
     * @return
     */
    private BatchFilesDO getBatchFile() {
        List<BatchFilesDO> batchFilesList = batchFilesMapper.
                queryBatchFilesByFileType(FileLogoConstant.HXZB, FileLogoConstant.STATUS_ZERO);
        if(CollectionUtils.isEmpty(batchFilesList)) {
            return null;
        }
        return batchFilesList.get(0);
    }

    /**
     * 根据执行策略组装或新增数据
     * @param coreIndexList
     * @param excelDataList
     */
    private List<CoreIndexDO> assembleData(List<CoreIndexDO> coreIndexList, List<Object> excelDataList) {
        log.info("开始组装数据-新增或更新");
        List<CoreIndexDO> returnList = new ArrayList<>();
        Map<String, CoreIndexDO> coreIndexMap = coreIndexList.stream().
                collect(Collectors.toMap(CoreIndexDO::getCode, coreIndexDO->coreIndexDO));
        // 如果表中存在当前股票数据，则更新数据，否则添加
        for(Object excelData : excelDataList) {
            String code = ((CoreIndexDO) excelData).getCode();
            if(coreIndexMap.containsKey(code)) {
                modifyExcelData(coreIndexMap.get(code), ((CoreIndexDO) excelData));
            }else {
                addExcelData(((CoreIndexDO) excelData));
            }
            returnList.add((CoreIndexDO) excelData);
        }
        return returnList;
    }

    /**
     * 添加新数据
     * @param excelData
     */
    private void addExcelData(CoreIndexDO excelData) {
        excelData.setIsDeleted(0);
        excelData.setPeriodCore("0");
        excelData.setFinalCalCore("0.00");
        excelData.setStatus("1");
        excelData.setShowTimes(0);
        excelData.setCreateTime(new Date());
        excelData.setUpdateTime(new Date());
    }

    /**
     * 根据执行策略修改数据
     * @param coreIndexData
     * @param excelData
     */
    private void modifyExcelData(CoreIndexDO coreIndexData, CoreIndexDO excelData) {
        if(coreIndexData.getCode().equals(excelData.getCode())) {
            // 判断excel是否上传当期核心指标，没传递则将status状态修改成：1-不展示
            if(!StringUtils.hasLength(excelData.getCurrentCore())) {
                excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
                excelData.setPeriodCore(coreIndexData.getPeriodCore());
                excelData.setCurrentCore(coreIndexData.getCurrentCore());
                excelData.setFinalCalCore(coreIndexData.getFinalCalCore());
                return;
            }
            // 判断查询出的核心指标是否为空字符，为空则将status修改成：1-不展示
            if(!StringUtils.hasLength(coreIndexData.getCurrentCore())) {
                excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
                // 上期核心指标换成当期核心指标
                excelData.setPeriodCore(coreIndexData.getCurrentCore());
                // 核心指数不计算
                excelData.setFinalCalCore(coreIndexData.getFinalCalCore());
                return;
            }
            // 将表中current_core的值与excel中的核心指标进行对比：如果不一样则进行计算
            String currentCore = coreIndexData.getCurrentCore();
            String excelCurrentCore = excelData.getCurrentCore();
            if(!currentCore.equals(excelCurrentCore)) {
                // 将current_core的数据赋值给period_core
                excelData.setPeriodCore(currentCore);
                // 计算：(current_core - period_core) / period_core 结果保留2为小数
                BigDecimal finalCalCore = new BigDecimal(excelCurrentCore).
                        subtract(new BigDecimal(currentCore)).
                        divide(new BigDecimal(currentCore), 2);
                // 将计算结果赋值给final_cal_core
                excelData.setFinalCalCore(String.valueOf(finalCalCore));
                // 执行核心策略
                exeCoreStrategy(coreIndexData, excelData);
                // 执行连续减少策略
                exeConRedStrategy(coreIndexData, excelData);
            }else {
                // 当excel上传的核心指标与上次的核心指标一样，则保留上次的展示结果
                excelData = coreIndexData;
            }
            excelData.setUpdateTime(new Date());
        }
    }

    /**
     * 执行核心策略
     * @param coreIndexData
     * @param excelData
     */
    private void exeCoreStrategy(CoreIndexDO coreIndexData, CoreIndexDO excelData) {
        // 本期核心指数
        BigDecimal finalCalCore = new BigDecimal(excelData.getFinalCalCore());
        // coreStrategy-核心策略集合
        Map<String, String> tradingStrategyMap =
                setStrategyContainer(FileLogoConstant.S_100000, FileLogoConstant.S_CORE);
        // 将计算结果小于 -0.1 的status状态修改成：0-展示  否则修改成：1-不展示
        if(finalCalCore.compareTo(new BigDecimal(tradingStrategyMap.get(coreIndexData.getCode()))) <= 0) {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ZERO);
        }else {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
        }
    }

    /**
     * 执行连续减少策略
     * @param coreIndexData
     * @param excelData
     */
    private void exeConRedStrategy(CoreIndexDO coreIndexData, CoreIndexDO excelData) {
        // 上期核心指数
        BigDecimal lastCore = new BigDecimal(coreIndexData.getFinalCalCore());
        // 本期核心指数
        BigDecimal currentCore = new BigDecimal(excelData.getFinalCalCore());
        // continueReductionStrategy-连续减少策略集合
        Map<String, String> tradingStrategyMap =
                setStrategyContainer(FileLogoConstant.S_100001, FileLogoConstant.S_CONTINUE_REDUCTION);
        String strategy = tradingStrategyMap.get(coreIndexData.getCode());
        // 当期和上期都小于或等于目标值的时候：0-展示  否则修改成：1-不展示
        if(lastCore.compareTo(new BigDecimal(strategy)) <= 0 && currentCore.compareTo(new BigDecimal(strategy)) <= 0) {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ZERO);
        }else {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
        }
    }

    /**
     * 设置策略容器
     * @param strategyId
     * @param strategyType
     */
    private Map<String, String> setStrategyContainer(String strategyId, String strategyType) {
        if(!strategyContainer.containsKey(strategyType)) {
            Map<String, Object> params = new HashMap<>();
            params.put("strategyId", strategyId);
            List<TradingStrategyDO> tradingStrategyList = tradingStrategyMapper.selectByMap(params);
            Map<String, String> tradingStrategyMap = tradingStrategyList.stream().
                    collect(Collectors.toMap(TradingStrategyDO::getCode, TradingStrategyDO::getStrategy));
            this.strategyContainer.put(strategyType, tradingStrategyMap);
        }
        return strategyContainer.get(strategyType);
    }

    /**
     * 将上一期的数据原封不动插入到历史表中
     */
    private void backupCoreIndex() {
        log.info("开始备份上期数据");
        baseMapper.backupCoreIndex();
    }

    /**
     * 更新日期：获取日期和报告日期
     * @param fileName
     */
    private void updateCalDate(String fileName) {
        log.info("开始更新获取日期和报告日期");
        int fileNameIndex = fileName.indexOf("&");
        baseMapper.updateCalDate(fileName.substring(fileNameIndex-8, fileNameIndex),
                fileName.substring(fileNameIndex+1, fileNameIndex+9));
    }

    /**
     * 更新展示次数
     */
    private void updateShowTimes() {
        log.info("更新展示次数");
        baseMapper.updateShowTimes();
    }

    /**
     * 更新期数
     * @param filePeriods
     */
    private void updatePeriods(Integer filePeriods) {
        log.info("更新期数");
        baseMapper.updatePeriods(filePeriods);
    }








    public static void main(String[] args) {
//        TradingStrategyDO tradingStrategyDO1 = new TradingStrategyDO();
//        tradingStrategyDO1.setStrategyId("1");
//        tradingStrategyDO1.setCode("100");
//        tradingStrategyDO1.setStrategy("aaa");
//
//        TradingStrategyDO tradingStrategyDO2 = new TradingStrategyDO();
//        tradingStrategyDO2.setStrategyId("1");
//        tradingStrategyDO2.setCode("200");
//        tradingStrategyDO2.setStrategy("aaa");
//        List<TradingStrategyDO> list =  new ArrayList<>();
//        list.add(tradingStrategyDO1);list.add(tradingStrategyDO2);
//        Map<String, TradingStrategyDO> tradingStrategyMap = list.stream().
//                collect(Collectors.toMap(TradingStrategyDO::getCode, a->a));
//
//        list.get(0).setCode("修改以后");
        String fileName = "HXZB_20220710&20220711_632680";
        int fileNameIndex = fileName.indexOf("&");


        System.err.println(fileName.substring(fileNameIndex-8, fileNameIndex));
        System.err.println(fileName.substring(fileNameIndex+1, fileNameIndex+9));


    }

}
