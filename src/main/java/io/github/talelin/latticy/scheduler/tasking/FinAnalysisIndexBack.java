package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.FinAnalysisIndexBackMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.FinAnalysisIndexBackDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class FinAnalysisIndexBack extends ServiceImpl<FinAnalysisIndexBackMapper, FinAnalysisIndexBackDO> {

    @Autowired
    private BatchFiles batchFiles ;

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    private static String fileType = "HC_JZCSYL";

    private static String[] methods = {"setCode", "setCodeName", "setRoeAvg"};

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateFinAnalysisIndexBack() throws Exception {
        log.info("--------start 开始读取批量回测财务指标数据");
        // 获取要读取文件的信息
        List<BatchFilesDO> file = batchFilesMapper.
                queryBatchFilesByFileType(fileType, FileLogoConstant.STATUS_ZERO);
        // 没有查询到要读取的文件直接返回
        if(CollectionUtils.isEmpty(file)) {
            log.info("该文件类型下没有需要读取的数据文件" + fileType);
            return;
        }
        // 获取文件路径、文件名称、文件期数
        String filePath = file.get(0).getFilePath();
        String fileName = file.get(0).getFileName();
        String periods = fileName.substring(10).replace(FileLogoConstant.SUFFIX_XLSX,"");
        // 将数据更新为：毫秒值-[读取中]
        batchFiles.updateBatchFilesStatus(fileName,
                String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);
        log.info("-------- 读取期数：" + periods);
        try{
            // 读取excel中的指标数据
            String fullName = filePath + File.separator + fileName;
            List<Object> excelDataList = ExcelUtil.readExcel(new File(fullName), 0, 2,
                    methods, FinAnalysisIndexBackDO.class);
            // 新增数据
            this.saveBatch(getFinAnalysisIndexBackDOList(excelDataList, periods));
            // 更新数据读取状态为：2-成功
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
        }catch (Exception e) {
            log.error("读取批量指标文件失败[" + e + "]");
            // 更新数据读取状态为：1-失败
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE,
                    FileLogoConstant.ERROR + ": " + e);
            throw e;
        }
        log.info("--------end 读取批量回测财务指标数据结束");
    }

    private List<FinAnalysisIndexBackDO> getFinAnalysisIndexBackDOList(List<Object> excelDataList, String periods) {
        List<FinAnalysisIndexBackDO> finAnalysisIndexBackDOList = new ArrayList<>();
        for (Object excelData : excelDataList) {
            FinAnalysisIndexBackDO finAnalysisIndexBackDO = (FinAnalysisIndexBackDO) excelData;
            finAnalysisIndexBackDO.setPeriods(Integer.valueOf(periods));
            finAnalysisIndexBackDO.setIsDeleted(0);
            finAnalysisIndexBackDO.setCreateTime(new Date());
            finAnalysisIndexBackDO.setUpdateTime(new Date());
            finAnalysisIndexBackDOList.add(finAnalysisIndexBackDO);
        }
        return finAnalysisIndexBackDOList;
    }
}
