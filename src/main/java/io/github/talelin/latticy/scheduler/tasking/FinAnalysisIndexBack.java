package io.github.talelin.latticy.scheduler.tasking;

import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.FinAnalysisIndexBackMapper;
import io.github.talelin.latticy.model.FinAnalysisIndexBackDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class FinAnalysisIndexBack {

    /**
     * 回测文件路径目录
     */
    private static final String BACK_FILE_PATH = "C:\\Users\\29686\\Desktop\\股票回测\\财务指标\\";

    /**
     * 文件期数
     */
    private static int FILE_PERIODS = 1;

    private static String[] methods = {"setCode", "setCodeName", "setRoeAvg"};

    @Autowired
    private FinAnalysisIndexBackMapper finAnalysisIndexBackMapper;

    /**
     * 批量创建或更新回测财务指标表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateFinAnalysisIndexBack() throws Exception {

        //1.获取文件名称，读取excel数据
        String fileAbsolutePath = BACK_FILE_PATH + "HC_JZCSYL_" + FILE_PERIODS + ".xlsx";
        File file = new File(fileAbsolutePath);
        //2.判断文件是否存在,是否是excel文件
        if (file.exists()) {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!(".xlsx".equals(extension) || ".xls".equals(extension))) {
                log.info(fileName + "文件不是excel文件");
                return;
            }
        } else {
            log.info("财务指标第" + FILE_PERIODS + "期文件不存在");
            return;
        }
        // 3.2获取文件路径、文件名称和文件期数
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        log.info("------start 开始计算财务指标第" + FILE_PERIODS + "期: " + fileName);
        List<FinAnalysisIndexBackDO> finAnalysisIndexBackDOList = new ArrayList<>();
        //3.3读取excel中核心指标数据
        try {
            List<Object> excelDataList = ExcelUtil.
                    readExcel(file, 0, 2, methods, FinAnalysisIndexBackDO.class);
            log.info("读取" + fileName + "-excel文件成功");
            //组装ListDO对象
            finAnalysisIndexBackDOList = assembleDate(excelDataList);
            log.info("数据组装完成");
            //保存至数据库
            finAnalysisIndexBackMapper.saveOrUpdateBatch(finAnalysisIndexBackDOList);
            log.info("持久化至数据库成功");
        } catch (Exception e) {
            log.error(fileAbsolutePath + "文件持久化至数据库失败");
            throw e;
        } finally {
            FILE_PERIODS++;
        }

    }

    /**
     * 组装数据
     *
     * @param excelDataList
     * @return List<FinAnalysisIndexBackDO>
     */
    private List<FinAnalysisIndexBackDO> assembleDate(List<Object> excelDataList) {

        List<FinAnalysisIndexBackDO> finAnalysisIndexBackDOList = new ArrayList<>();
        for (Object excelDate : excelDataList) {
            FinAnalysisIndexBackDO finAnalysisIndexBackDO = (FinAnalysisIndexBackDO) excelDate;
            finAnalysisIndexBackDO.setPeriods(FILE_PERIODS);
            finAnalysisIndexBackDO.setUpdateTime(new Date());
            finAnalysisIndexBackDO.setCreateTime(new Date());
            finAnalysisIndexBackDOList.add(finAnalysisIndexBackDO);
        }

        return finAnalysisIndexBackDOList;
    }
}
