package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.CoreIndexBackMapper;
import io.github.talelin.latticy.mapper.TradingStrategyMapper;
import io.github.talelin.latticy.model.CoreIndexBackDO;
import io.github.talelin.latticy.model.TradingStrategyDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 读取code文件目录下所有核心指标文件,持久到DB
 *
 * @author zhangjiahao
 * {@code @date} 2022-08-21
 * {@code @desc} 回测：将之前文件全部上传至服务器，进行计算回显
 */
@Component
@Slf4j
public class CoreIndexBack extends ServiceImpl<CoreIndexBackMapper, CoreIndexBackDO> {

    /**
     * 回测文件路径目录
     */
    private static final String BACK_FILE_PATH = "C:\\Users\\29686\\Desktop\\股票回测\\core";

    private static String[] methods = {"setCode", "setCodeName", "setCurrentCore"};

    private Map<String, Map<String, String>> strategyContainer = new HashMap<>() ;

    @Autowired
    private TradingStrategyMapper tradingStrategyMapper ;

    @Autowired
    private CoreIndexBackMapper coreIndexBackMapper;

    @Autowired
    private CoreIndex coreIndex;

    /**
     * 批量创建或更新回测核心指数表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateCoreIndexBack() throws Exception {
        //1.获取回测路径下所有的文件
        File[] files = new File(BACK_FILE_PATH).listFiles();
        if (files == null && files.length <= 0) {
            log.info("回测目录下没有需要计算的文件");
            return;
        }
        //2.遍历回测文件
        int i = 0;
        while (i < Objects.requireNonNull(files).length) {
        //while (i < 1) {
            File file = files[i];
            //3.获取文件名称，读取excel数据
            //3.1判断文件是否存在,是否是excel文件
            if (file != null && file.exists()) {
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                if (!(".xlsx".equals(extension) || ".xls".equals(extension))) {
                    ++i;
                    log.info(fileName + "文件不是excel文件");
                    break;
                }
            }
            // 3.2获取文件路径、文件名称和文件期数
            String filePath = file.getAbsolutePath();
            String fileName = file.getName();
            int filePeriods = Integer.parseInt(file.getName().split("_")[2].substring(0, file.getName().split("_")[2].indexOf(".")));
            log.info("------start 开始计算核心指数第" + filePeriods + "期: " + fileName);
            List<CoreIndexBackDO> coreIndexBackDOList = null;
            //3.3读取excel中核心指标数据
            try {
                List<Object> excelDataList = ExcelUtil.
                        readExcel(file, 0, 2, methods, CoreIndexBackDO.class);
                //4.计算封装回测核心指标文件
                //4.1查询上一期的数据
                if (filePeriods != 0){
                    QueryWrapper<CoreIndexBackDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("periods", filePeriods - 1);
                    coreIndexBackDOList = coreIndexBackMapper.selectList(queryWrapper);
                }else {
                    coreIndexBackDOList = new ArrayList<>();
                }
                // 4.2根据执行策略组装或新增数据，并将数据入库
                log.info("根据执行策略组装或新增数据，并将数据入库");
                List<CoreIndexBackDO> coreIndexBackDOS = assembleData(coreIndexBackDOList, excelDataList);
                log.info("完成111111111111111");
//                for (CoreIndexBackDO coreIndexBackDO : coreIndexBackDOS) {
//                    coreIndexBackDO.setPeriods(filePeriods);
//                }
                log.info("完成222222222222222");
                //this.saveOrUpdateBatch(coreIndexBackDOS);
                System.out.println("==========================");
                log.info("持久化完成");
            } catch (Exception e) {
                throw e;
            }


            //5.持久化至数据库

            i++;
        }
    }

    /**
     * 根据执行策略组装或新增数据
     * @param coreIndexBackList
     * @param excelDataList
     */
    private List<CoreIndexBackDO> assembleData(List<CoreIndexBackDO> coreIndexBackList, List<Object> excelDataList) {
        log.info("开始组装数据-新增或更新");
        List<CoreIndexBackDO> returnList = new ArrayList<>();
        Map<String, CoreIndexBackDO> coreIndexBackMap = coreIndexBackList.stream().
                collect(Collectors.toMap(CoreIndexBackDO::getCode, CoreIndexBackDO->CoreIndexBackDO));
        // 如果表中存在当前股票数据，则更新数据，否则添加
        for(Object excelData : excelDataList) {
            String code = ((CoreIndexBackDO) excelData).getCode();
            if(coreIndexBackMap.containsKey(code)) {
                modifyExcelData(coreIndexBackMap.get(code), ((CoreIndexBackDO) excelData));
            }else {
                addExcelData(((CoreIndexBackDO) excelData));
            }
            returnList.add((CoreIndexBackDO) excelData);
        }
        return returnList;
    }

    /**
     * 添加新数据
     * @param excelData
     */
    private void addExcelData(CoreIndexBackDO excelData) {
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
     * @param coreIndexBackData
     * @param excelData
     */
    private void modifyExcelData(CoreIndexBackDO coreIndexBackData, CoreIndexBackDO excelData) {
        if(coreIndexBackData.getCode().equals(excelData.getCode())) {
            // 判断excel是否上传当期核心指标，没传递则将status状态修改成：1-不展示
            if(!StringUtils.hasLength(excelData.getCurrentCore())) {
                excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
                excelData.setPeriodCore(coreIndexBackData.getPeriodCore());
                excelData.setCurrentCore(coreIndexBackData.getCurrentCore());
                excelData.setFinalCalCore(coreIndexBackData.getFinalCalCore());
                return;
            }
            // 判断查询出的核心指标是否为空字符，为空则将status修改成：1-不展示
            if(!StringUtils.hasLength(coreIndexBackData.getCurrentCore())) {
                excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
                // 上期核心指标换成当期核心指标
                excelData.setPeriodCore(coreIndexBackData.getCurrentCore());
                // 核心指数不计算
                excelData.setFinalCalCore(coreIndexBackData.getFinalCalCore());
                return;
            }
            // 将表中current_core的值与excel中的核心指标进行对比：如果不一样则进行计算
            String currentCore = coreIndexBackData.getCurrentCore();
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
                exeCoreStrategy(coreIndexBackData, excelData);
                // 执行连续减少策略
                exeConRedStrategy(coreIndexBackData, excelData);
            }else {
                // 当excel上传的核心指标与上次的核心指标一样，则保留上次的展示结果
                excelData = coreIndexBackData;
            }
            excelData.setUpdateTime(new Date());
        }
    }

    /**
     * 执行核心策略
     * @param coreIndexBackDate
     * @param excelData
     */
    private void exeCoreStrategy(CoreIndexBackDO coreIndexBackDate, CoreIndexBackDO excelData) {
        // 本期核心指数
        BigDecimal finalCalCore = new BigDecimal(excelData.getFinalCalCore());
        // coreStrategy-核心策略集合
        Map<String, String> tradingStrategyMap =
                setStrategyContainer(FileLogoConstant.S_100000, FileLogoConstant.S_CORE);
        // 将计算结果小于 -0.1 的status状态修改成：0-展示  否则修改成：1-不展示
        if(finalCalCore.compareTo(new BigDecimal(tradingStrategyMap.get(coreIndexBackDate.getCode()))) <= 0) {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ZERO);
        }else {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
        }
    }

    /**
     * 执行连续减少策略
     * @param coreIndexBackData
     * @param excelData
     */
    private void exeConRedStrategy(CoreIndexBackDO coreIndexBackData, CoreIndexBackDO excelData) {
        // 上期核心指数
        BigDecimal lastCore = new BigDecimal(coreIndexBackData.getFinalCalCore());
        // 本期核心指数
        BigDecimal currentCore = new BigDecimal(excelData.getFinalCalCore());
        // continueReductionStrategy-连续减少策略集合
        Map<String, String> tradingStrategyMap =
                setStrategyContainer(FileLogoConstant.S_100001, FileLogoConstant.S_CONTINUE_REDUCTION);
        String strategy = tradingStrategyMap.get(coreIndexBackData.getCode());
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

    public static void main(String[] args) throws Exception {
        new CoreIndexBack().createOrUpdateCoreIndexBack();
    }
}
