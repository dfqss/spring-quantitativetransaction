package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.RangeRiseCommonMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.FinAnalysisIndexBackDO;
import io.github.talelin.latticy.model.RangeRiseCommonDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 读取code文件目录下所有核心指标文件,持久到DB
 * @author zhangjiahao
 * {@code @date} 2022-08-21
 * {@code @desc} 回测：将之前文件全部上传至服务器，进行计算回显
 */
@Component
@Slf4j
public class RangeRiseCommon extends ServiceImpl<RangeRiseCommonMapper, RangeRiseCommonDO>  {

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Autowired
    private BatchFiles batchFiles ;

    @Autowired
    private RangeRiseCommonMapper rangeRiseCommonMapper;

    private static final String quarter = "HC_QJZF_JD";

    private static final String halfYear = "HC_QJZF_BN";

    private static final String[] QUARTER_METHODS = {"setCode", "setCodeName", "setQuarterRise"};

    private static final String[] HALF_YEAR_METHODS = {"setCode", "setCodeName", "setHalfYearRise"};

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateRangeRiseCommon() throws Exception {
        log.info("--------start 开始读取批量回测区间涨幅数据");
        List<RangeRiseCommonDO> saveBatchList = new ArrayList<>();
        List<RangeRiseCommonDO> quarterData = getDataList(quarter);
        List<RangeRiseCommonDO> halfYearData = getDataList(halfYear);
        if(CollectionUtils.isEmpty(quarterData) && CollectionUtils.isEmpty(halfYearData)) return;
        if(CollectionUtils.isEmpty(quarterData)) {
            saveBatchList = halfYearData;
        }
        if(CollectionUtils.isEmpty(halfYearData)) {
            saveBatchList = quarterData;
        }
        if(!CollectionUtils.isEmpty(quarterData) && !CollectionUtils.isEmpty(halfYearData)) {
            saveBatchList = combineData(quarterData, halfYearData);
        }
        System.err.println(saveBatchList.size() + ":" + saveBatchList.get(0));
        this.saveBatch(saveBatchList);
        log.info("--------end 读取批量回测区间涨幅数据结束");
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    private List<RangeRiseCommonDO> getDataList(String fileType) throws Exception {
        // 获取要读取文件的信息
        List<BatchFilesDO> file = batchFilesMapper.
                queryBatchFilesByFileType(fileType, FileLogoConstant.STATUS_ZERO);
        // 没有查询到要读取的文件直接返回
        if(CollectionUtils.isEmpty(file)) {
            log.info("该文件类型下没有需要读取的数据文件" + fileType);
            return null;
        }
        // 获取文件路径、文件名称
        String filePath = file.get(0).getFilePath();
        String fileName = file.get(0).getFileName();
        String periods = fileName.substring(11).replace(FileLogoConstant.SUFFIX_XLSX,"");
        // 将数据更新为：毫秒值-[读取中]
        batchFiles.updateBatchFilesStatus(fileName,
                String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);
        // 读取excel中的指标数据
        String fullName = filePath + File.separator + fileName;
        String[] methods = HALF_YEAR_METHODS;
        if("HC_QJZF_JD".equals(fileType)) {
            methods = QUARTER_METHODS;
        }
        log.info("-------- 读取类型：" + fileType + "[" + periods + "]");
        List<RangeRiseCommonDO> rangeRiseCommonDOList = null;
        try{
            List<Object> excelDataList = ExcelUtil.readExcel(new File(fullName), 0, 2,
                    methods, RangeRiseCommonDO.class);
            rangeRiseCommonDOList = getRangeRiseCommonDOList(excelDataList, periods);
            // 更新数据读取状态为：2-成功
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
        }catch (Exception e) {
            log.error("读取批量指标文件失败[" + e + "]");
            // 更新数据读取状态为：1-失败
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE,
                    FileLogoConstant.ERROR + ": " + e);
            throw e;
        }
        return rangeRiseCommonDOList;
    }

    private List<RangeRiseCommonDO> getRangeRiseCommonDOList(List<Object> excelDataList, String periods) {
        List<RangeRiseCommonDO> rangeRiseCommonDOList = new ArrayList<>();
        for (Object excelData : excelDataList) {
            RangeRiseCommonDO rangeRiseCommonDO = (RangeRiseCommonDO) excelData;
            rangeRiseCommonDO.setPeriods(Integer.valueOf(periods));
            rangeRiseCommonDO.setIsDeleted(0);
            rangeRiseCommonDO.setCreateTime(new Date());
            rangeRiseCommonDO.setUpdateTime(new Date());
            rangeRiseCommonDOList.add(rangeRiseCommonDO);
        }
        return rangeRiseCommonDOList;
    }

    private List<RangeRiseCommonDO> combineData(List<RangeRiseCommonDO> quarterData,
                                                List<RangeRiseCommonDO> halfYearData) {
        List<RangeRiseCommonDO> returnList = new ArrayList<>();
        Map<String, RangeRiseCommonDO> quarterDataMap = quarterData.stream().
                collect(Collectors.toMap(RangeRiseCommonDO::getCode, rangeRiseCommonDO->rangeRiseCommonDO));
        for(RangeRiseCommonDO rangeRiseCommonDO : halfYearData) {
            RangeRiseCommonDO returnData = new RangeRiseCommonDO();
            returnData.setCode(rangeRiseCommonDO.getCode());
            returnData.setCodeName(rangeRiseCommonDO.getCodeName());
            returnData.setHalfYearRise(rangeRiseCommonDO.getHalfYearRise());
            if(quarterDataMap.containsKey(rangeRiseCommonDO.getCode())) {
                returnData.setQuarterRise(quarterDataMap.get(rangeRiseCommonDO.getCode()).getQuarterRise());
            }
            returnData.setPeriods(rangeRiseCommonDO.getPeriods());
            returnData.setIsDeleted(0);
            returnData.setCreateTime(new Date());
            returnData.setUpdateTime(new Date());
            returnList.add(returnData);
        }
        return returnList;
    }
}
