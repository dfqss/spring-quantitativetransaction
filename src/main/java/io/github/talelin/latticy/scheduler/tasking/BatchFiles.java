package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.common.util.FileUtil;
import io.github.talelin.latticy.configuration.ApplicationConfiguration;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.service.BatchFilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批量读取文件夹下的excel文件
 */
@Component
@Slf4j
public class BatchFiles extends ServiceImpl<BatchFilesMapper, BatchFilesDO> {

    @Autowired
    private ApplicationConfiguration fileConfig;

    @Autowired
    private BatchFilesService mbaBatchFilesService;

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void readFile() {
        String filePath = null;
        List<String> readingFiles = null;
        List<String> filesPaths = fileConfig.getBatchFilesPath();
        for (String path : filesPaths) {
            filePath = FileUtil.joinPath(path, CommonUtil.getFormatNowTime(FileLogoConstant.yyyyMMdd));
            try {
                List<String> okFiles = FileUtil.getFileNameList(filePath, FileLogoConstant.SUFFIX_OK);
                log.info("--------start 批量文件入库：" + okFiles);
                log.info("当前批量文件地址: " + filePath);
                if(okFiles.size() > 0) {
                    readingFiles = FileUtil.renameFilesSuffix(filePath, okFiles,
                            FileLogoConstant.SUFFIX_OK, FileLogoConstant.SUFFIX_READING);
                    createBatchFiles(filePath, okFiles);
                    List<String> f = FileUtil.renameFilesSuffix(filePath, readingFiles,
                            FileLogoConstant.SUFFIX_READING, FileLogoConstant.SUFFIX_SUCCESS);
                    log.info("--------end 批量文件入库成功" + filePath);
                }else {
                    log.info("--------end 路径[" + filePath + "]下没有需要读入的批量文件");
                }
            }catch (Exception e) {
                log.error("读取批量文件失败，开始事务回滚:" + e);
                FileUtil.renameFilesSuffix(filePath, readingFiles,
                        FileLogoConstant.SUFFIX_READING, FileLogoConstant.SUFFIX_FAIL);
                throw e;
            }
        }
    }

    /**
     * 更新批量文件数据表状态:
     * 0-未读 1-失败 2-成功 随机数-读取中
     * @param fileName
     * @param status
     * @param description
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, propagation = Propagation.REQUIRES_NEW)
    public void updateBatchFilesStatus(String fileName, String status, String description) {
        BatchFilesDO batchFilesDO = new BatchFilesDO();
        batchFilesDO.setFileName(fileName);
        batchFilesDO.setStatus(status);
        batchFilesDO.setDescription(description);
        batchFilesMapper.updateById(batchFilesDO);
    }

    /**
     * 创建批量文件表数据
     * @param filePath
     * @param okFiles
     */
    private void createBatchFiles(String filePath, List<String> okFiles) {
        for(String okFile : okFiles) {
            baseMapper.createBatchFiles(okFile.replace(FileLogoConstant.SUFFIX_OK, FileLogoConstant.SUFFIX_XLSX),
                    filePath, okFile.substring(0, okFile.indexOf("_",0)));
        }
    }
}