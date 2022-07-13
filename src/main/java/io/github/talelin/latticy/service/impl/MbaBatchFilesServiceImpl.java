package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.MbaBatchFilesMapper;
import io.github.talelin.latticy.model.MbaBatchFilesDO;
import io.github.talelin.latticy.service.MbaBatchFilesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
public class MbaBatchFilesServiceImpl extends ServiceImpl<MbaBatchFilesMapper, MbaBatchFilesDO> implements MbaBatchFilesService {


    /**
     * 新增execl文件表信息
     *
     * @param fileName  文件名称
     * @param filePath 文件路径
     * @return 用户名
     */
    @Override
    @Transactional
    public void createBatchFiles(String fileName, String filePath) {
        try {
            String replaceFileName = fileName.replace(".ok", ".xlsx");
            String fileType = fileName.substring(fileName.indexOf("_"));
            this.baseMapper.createBatchFiles(replaceFileName,filePath,fileType);
        }catch (Exception e){
            log.error("创建批量文件表数据" + e.toString());
        }
    }


    /**
     * 更新批量文件数据表状态 0-未读 1-失败 2-成功 随机数-读取中
     */
    @Override
    @Transactional
    public void updateBatchFilesStatus(String fileName, String status, String description) {
        try {
            this.baseMapper.updateBatchFilesStatus(fileName,status,description);
        }catch (Exception e){
            log.error("更新批量文件数据表状态失败" + e.toString());
        }
    }

    /**
     *  查询文件路径与文件名称
     * @param fileName 模糊文件名称
     * @param status   文件状态
     * @return
     */
    @Override
    public List<MbaBatchFilesDO> fileMessage(String fileName, String status) {
        return this.baseMapper.fileMessage(fileName, status);
    }


}
