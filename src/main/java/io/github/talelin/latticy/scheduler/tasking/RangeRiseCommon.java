package io.github.talelin.latticy.scheduler.tasking;

import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.FinAnalysisIndexBackMapper;
import io.github.talelin.latticy.mapper.RangeRiseCommonMapper;
import io.github.talelin.latticy.model.FinAnalysisIndexBackDO;
import io.github.talelin.latticy.model.RangeRiseCommonDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读取code文件目录下所有核心指标文件,持久到DB
 * @author zhangjiahao
 * {@code @date} 2022-08-21
 * {@code @desc} 回测：将之前文件全部上传至服务器，进行计算回显
 */
@Component
@Slf4j
public class RangeRiseCommon {

    /**
     * 季度-回测文件路径目录
     */
    private static final String QUARTER_BACK_FILE_PATH = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅季度\\";

    /**
     * 半年-回测文件路径目录
     */
    private static final String HALF_YEAR_BACK_FILE_PATH = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅半年\\";

    /**
     * 文件期数
     */
    private static int FILE_PERIODS = 1;

    private static final String[] QUARTER_METHODS = {"setCode", "setCodeName", "setQuarterRise"};

    private static final String[] HALF_YEAR_METHODS = {"setCode", "setCodeName", "setHalfYearRise"};

    @Autowired
    private RangeRiseCommonMapper rangeRiseCommonMapper;

    /**
     * 批量创建或更新回测财务指标表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateRangeRiseCommon() throws Exception {
        List<Object> quarterExcelDate = getExcelDate(QUARTER_BACK_FILE_PATH, "HC_QJZF_JD_", QUARTER_METHODS);
        List<Object> halfYearExcelDate = getExcelDate(HALF_YEAR_BACK_FILE_PATH, "HC_QJZF_BN_", HALF_YEAR_METHODS);

        if (quarterExcelDate == null || halfYearExcelDate == null) {
            throw new Exception("excelList集合为null");
        }

        //封装DO对象
        List<RangeRiseCommonDO> rangeRiseCommonDOList = assembleDate(quarterExcelDate, halfYearExcelDate);
        log.info("封装对象成功");
        try {
            //持久化到数据库
            rangeRiseCommonMapper.saveOrUpdateBatch(rangeRiseCommonDOList);
            log.info("数据库持久化成功");
        }catch (Exception e){
            log.info("持久化失败");
            throw e;
        }finally {
            FILE_PERIODS++;
        }

    }

    /**
     * 根据文件路径,文件名前缀读取excel数据
     *
     * @param filePath
     * @param fileNamePrefix
     * @param methods
     * @return
     * @throws Exception
     */
    private List<Object> getExcelDate(String filePath, String fileNamePrefix, String[] methods) throws Exception {
        //1.获取文件名称，读取excel数据
        String fileAbsolutePath = filePath + fileNamePrefix + FILE_PERIODS + ".xlsx";
        File file = new File(fileAbsolutePath);
        //1.1判断文件是否存在,是否是excel文件
        if (file.exists()) {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!(".xlsx".equals(extension) || ".xls".equals(extension))) {
                log.info(fileName + "文件不是excel文件");
                return null;
            }
        } else {
            log.info(fileNamePrefix + "第" + FILE_PERIODS + "期文件不存在");
            return null;
        }
        // 2.1获取文件路径、文件名称和文件期数
        String fileName = file.getName();
        log.info("------start 开始计算财务指标第" + FILE_PERIODS + "期: " + fileName);
        //3.1读取excel中核心指标数据
        List<Object> excelDataList;
        try {
            excelDataList = ExcelUtil.
                    readExcel(file, 0, 2, methods, RangeRiseCommonDO.class);
            log.info("读取" + fileName + "-excel文件成功");
        } catch (Exception e) {
            log.error("读取" + fileName + "-excel文件失败");
            throw e;
        }
        return excelDataList;
    }


    /**
     * 组装数据
     *
     * @param quarterExcelDate
     * @param halfYearExcelDate
     * @return List<RangeRiseCommonDO>
     */
    private List<RangeRiseCommonDO> assembleDate(List<Object> quarterExcelDate, List<Object> halfYearExcelDate) {

        List<RangeRiseCommonDO> rangeRiseCommonDOList = new ArrayList<>();
        for (Object excelDate : quarterExcelDate) {
            RangeRiseCommonDO quarterRangeRiseCommonDO = (RangeRiseCommonDO) excelDate;
            quarterRangeRiseCommonDO.setPeriods(FILE_PERIODS);
            quarterRangeRiseCommonDO.setUpdateTime(new Date());
            quarterRangeRiseCommonDO.setCreateTime(new Date());

            for (Object o : halfYearExcelDate) {
                RangeRiseCommonDO halfYearRangeRiseCommonDO = (RangeRiseCommonDO) excelDate;
                if (halfYearRangeRiseCommonDO.getCode().equals(quarterRangeRiseCommonDO.getCode())){
                    quarterRangeRiseCommonDO.setHalfYearRise(halfYearRangeRiseCommonDO.getHalfYearRise());
                }
            }
            rangeRiseCommonDOList.add(quarterRangeRiseCommonDO);
        }

        return rangeRiseCommonDOList;
    }

}
