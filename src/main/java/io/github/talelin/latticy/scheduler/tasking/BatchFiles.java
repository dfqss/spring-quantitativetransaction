package io.github.talelin.latticy.scheduler.tasking;

import io.github.talelin.latticy.common.util.FileUtil;
import io.github.talelin.latticy.configuration.ApplicatinConfiguration;
import io.github.talelin.latticy.service.MbaBatchFilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 批量读取文件夹下的excel文件
 */
@Component
@Slf4j
public class BatchFiles {

    @Autowired
    private ApplicatinConfiguration contextConfig;

    @Autowired
    private MbaBatchFilesService mbaBatchFilesService;

    public void readFile() {
        List<String> filespaths = contextConfig.getFilespaths();
        String filePath = null;
        List<String> readingFiles = null;
        try {
            for (String path : filespaths) {
                filePath = FileUtil.joinFilePath(path);
                log.info("文件路径为：" + filePath);
                List<String> okFiles = FileUtil.getFileNameList(filePath, "ok");
                log.info("--------start 当前文件读取地址：" + filePath);
                log.info("开始读取批量文件：" + okFiles);
                if (okFiles.size() > 0) {
                    readingFiles = FileUtil.renameFilesSuffix(filePath, okFiles, ".ok", ".reading");
                    this.createBatchFiles(filePath, okFiles);
                    FileUtil.renameFilesSuffix(filePath, readingFiles, ".reading", ".success");
                    log.info("--------end 批量文件读取成功" + filePath);
                } else {
                    log.info("--------end 路径[" + filePath + "]下没有需要读入的批量文件");
                }
            }
        } catch (Exception e) {
            log.error("读取批量文件失败，开始事务回滚:" + e.toString());
            FileUtil.renameFilesSuffix(filePath, readingFiles, ".reading", ".fail");
        }


    }

    /**
     * 创建批量文件表数据
     */
    private void createBatchFiles(String filePath, List<String> okFiles) {
        for (String fileName : okFiles) {
            mbaBatchFilesService.createBatchFiles(fileName, filePath);
            log.info("创建批量文件表数据成功:" + fileName);
        }
    }
}

