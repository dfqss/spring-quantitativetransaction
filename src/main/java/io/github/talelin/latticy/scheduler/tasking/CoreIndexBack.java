package io.github.talelin.latticy.scheduler.tasking;

import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.CoreIndexBackMapper;
import io.github.talelin.latticy.mapper.TradingStrategyMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.CoreIndexBackDO;
import io.github.talelin.latticy.model.TradingStrategyDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class CoreIndexBack {

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Autowired
    private BatchFiles batchFiles ;

    private static String[] methods = {"setCode", "setCodeName", "setCurrentCore"};

    private Map<String, Map<String, String>> strategyContainer = new HashMap<>();

    @Autowired
    private TradingStrategyMapper tradingStrategyMapper;

    @Autowired
    private CoreIndexBackMapper coreIndexBackMapper;


    /**
     * 批量创建或更新回测核心指数表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateCoreIndexBack() throws Exception {

        //0.获取要读取文件的信息
        BatchFilesDO batchFilesDO = getBatchFile();
        if(null == batchFilesDO) {
            log.info("没有需要计算的回测核心指标数据文件");
            return;
        }

        // 3获取文件路径、文件名称和文件期数
        String filePath = batchFilesDO.getFilePath();
        String fileName = batchFilesDO.getFileName();
        Integer filePeriods = batchFilesDO.getFilePeriods();
        String fullName = filePath + File.separator + fileName;
        String reportDate = fileName.split("_")[1];
        // 将数据更新为：毫秒值-[读取中]
        batchFiles.updateBatchFilesStatus(fileName,
                String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);

        log.info("------start 开始计算核心指数第" + filePeriods + "期: " + fileName);

        List<CoreIndexBackDO> coreIndexBackDOList = null;
        //4.读取excel中核心指标数据
        try {
            List<Object> excelDataList = ExcelUtil.
                    readExcel(new File(fullName), 0, 2, methods, CoreIndexBackDO.class);
            //4.1查询上一期的数据
            if (filePeriods != 0) {
                coreIndexBackDOList = coreIndexBackMapper.selectList(filePeriods - 1);
            } else {
                coreIndexBackDOList = new ArrayList<>();
            }
            // 4.2根据执行策略组装或新增数据，并将数据入库
            log.info("根据执行策略组装或新增数据，并将数据入库");
            List<CoreIndexBackDO> indexBackDOList = assembleData(coreIndexBackDOList, excelDataList);
            assembleUpdateDate(filePeriods,reportDate, indexBackDOList);
            log.info("组装数据完成");
            coreIndexBackMapper.saveOrUpdateBatch(indexBackDOList);

            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
            log.info("end------计算核心指数完成: " + fileName);
        } catch (Exception e) {
            log.error("读取核心指标文件失败: " + e);
            // 更新数据读取状态为：1-失败
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE, FileLogoConstant.ERROR);
            throw e;
        }
    }

    /**
     * 获取要读取文件的信息
     * @return 文件对象
     */
    private BatchFilesDO getBatchFile() {
        List<BatchFilesDO> batchFilesList = batchFilesMapper.
                queryBatchFilesByFileType(FileLogoConstant.HC_HXZB, FileLogoConstant.STATUS_ZERO);
        if(CollectionUtils.isEmpty(batchFilesList)) {
            return null;
        }
        return batchFilesList.get(0);
    }

    private void assembleUpdateDate(int filePeriods,String reportDate, List<CoreIndexBackDO> coreIndexBackDOS) throws ParseException {
        for (CoreIndexBackDO coreIndexBackDO : coreIndexBackDOS) {
            coreIndexBackDO.setPeriods(filePeriods);
            if (coreIndexBackDO.getStatus() == null) coreIndexBackDO.setStatus("1");
            if (coreIndexBackDO.getPeriodCore() == null) coreIndexBackDO.setPeriodCore("0");
            if (coreIndexBackDO.getFinalCalCore() == null) coreIndexBackDO.setFinalCalCore("0.00");
            if (coreIndexBackDO.getIsDeleted() == null) coreIndexBackDO.setIsDeleted(0);
            if (coreIndexBackDO.getShowTimes() == null) coreIndexBackDO.setShowTimes(0);
            if (coreIndexBackDO.getReportDate() == null) coreIndexBackDO.setReportDate(new SimpleDateFormat("YYYYMMdd").parse(reportDate));
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd").parse(reportDate);
//            Date date = simpleDateFormat.parse(reportDate);
//            System.out.println(date);
        }
    }

    /**
     * 根据执行策略组装或新增数据
     *
     * @param coreIndexBackList
     * @param excelDataList
     */
    private List<CoreIndexBackDO> assembleData(List<CoreIndexBackDO> coreIndexBackList, List<Object> excelDataList) {
        log.info("开始组装数据-新增或更新");
        List<CoreIndexBackDO> returnList = new ArrayList<>();
        Map<String, CoreIndexBackDO> coreIndexBackMap = coreIndexBackList.stream().
                collect(Collectors.toMap(CoreIndexBackDO::getCode, CoreIndexBackDO -> CoreIndexBackDO));
        // 如果表中存在当前股票数据，则更新数据，否则添加
        for (Object excelData : excelDataList) {
            String code = ((CoreIndexBackDO) excelData).getCode();
            if (coreIndexBackMap.containsKey(code)) {
                modifyExcelData(coreIndexBackMap.get(code), ((CoreIndexBackDO) excelData));
            } else {
                addExcelData(((CoreIndexBackDO) excelData));
            }
            returnList.add((CoreIndexBackDO) excelData);
        }
        return returnList;
    }

    /**
     * 添加新数据
     *
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
     *
     * @param coreIndexBackData
     * @param excelData
     */
    private void modifyExcelData(CoreIndexBackDO coreIndexBackData, CoreIndexBackDO excelData) {
        if (coreIndexBackData.getCode().equals(excelData.getCode())) {
            // 判断excel是否上传当期核心指标，没传递则将status状态修改成：1-不展示
            if (!StringUtils.hasLength(excelData.getCurrentCore())) {
                excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
                excelData.setPeriodCore(coreIndexBackData.getPeriodCore());
                excelData.setCurrentCore(coreIndexBackData.getCurrentCore());
                excelData.setFinalCalCore(coreIndexBackData.getFinalCalCore());
                return;
            }
            // 判断查询出的核心指标是否为空字符，为空则将status修改成：1-不展示
            if (!StringUtils.hasLength(coreIndexBackData.getCurrentCore())) {
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
            if (!currentCore.equals(excelCurrentCore)) {
                // 将current_core的数据赋值给period_core
                excelData.setPeriodCore(currentCore);
                // 计算：(current_core - period_core) / period_core 结果保留2为小数
                BigDecimal finalCalCore = new BigDecimal(excelCurrentCore).
                        subtract(new BigDecimal(currentCore)).
                        divide(new BigDecimal(currentCore), 2, BigDecimal.ROUND_DOWN);
                // 将计算结果赋值给final_cal_core
                excelData.setFinalCalCore(String.valueOf(finalCalCore));
                // 执行核心策略
                exeCoreStrategy(coreIndexBackData, excelData);
                // 执行连续减少策略
                exeConRedStrategy(coreIndexBackData, excelData);
            } else {
                // 当excel上传的核心指标与上次的核心指标一样，则保留上次的展示结果
                excelData = coreIndexBackData;
            }
            excelData.setUpdateTime(new Date());
        }
    }

    /**
     * 执行核心策略
     *
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
        String s = tradingStrategyMap.get(coreIndexBackDate.getCode());
        System.out.println(coreIndexBackDate.getCode());
        System.out.println(s);
        BigDecimal bigDecimal = new BigDecimal(tradingStrategyMap.get(coreIndexBackDate.getCode()));
        if (finalCalCore.compareTo(new BigDecimal(tradingStrategyMap.get(coreIndexBackDate.getCode()))) <= 0) {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ZERO);
        } else {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
        }
    }

    /**
     * 执行连续减少策略
     *
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
        if (lastCore.compareTo(new BigDecimal(strategy)) <= 0 && currentCore.compareTo(new BigDecimal(strategy)) <= 0) {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ZERO);
        } else {
            excelData.setStatus(FileLogoConstant.SHOW_STATUS_ONE);
        }
    }

    /**
     * 设置策略容器
     *
     * @param strategyId
     * @param strategyType
     */
    private Map<String, String> setStrategyContainer(String strategyId, String strategyType) {
        if (!strategyContainer.containsKey(strategyType)) {
            Map<String, Object> params = new HashMap<>();
            params.put("strategy_id", strategyId);
            List<TradingStrategyDO> tradingStrategyList = tradingStrategyMapper.selectByMap(params);
            Map<String, String> tradingStrategyMap = tradingStrategyList.stream().
                    collect(Collectors.toMap(TradingStrategyDO::getCode, TradingStrategyDO::getStrategy));
            this.strategyContainer.put(strategyType, tradingStrategyMap);
        }
        return strategyContainer.get(strategyType);
    }

    public static void main(String[] args) throws Exception {
        //new CoreIndexBack().createOrUpdateCoreIndexBack();
        String excelCurrentCore = "62505";
        String currentCore = "13603";
        BigDecimal finalCalCore = new BigDecimal(excelCurrentCore).
                subtract(new BigDecimal(currentCore));
        System.out.println(finalCalCore);
        BigDecimal divide = finalCalCore.divide(new BigDecimal(currentCore),2, BigDecimal.ROUND_DOWN);
        System.out.println(divide);
    }
}
