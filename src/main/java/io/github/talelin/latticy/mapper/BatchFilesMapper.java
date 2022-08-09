package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchFilesMapper extends BaseMapper<BatchFilesDO> {

    /**
     * 新增execl文件表信息
     * @param fileName
     * @param filePath
     * @param type
     * @return
     */
    int createBatchFiles(String fileName, String filePath, String type);

    /**
     * 更新批量文件数据表状态 0-未读 1-失败 2-成功 随机数-读取中
     * @param fileName
     * @param status
     * @param description
     */
    void updateBatchFilesStatus(String fileName, String status, String description);

    /**
     * 根据批量文件类型查询最远期数的数据
     * @param fileType
     * @param status
     * @return
     */
    List<BatchFilesDO> queryBatchFilesByFileType(String fileType, String status);

}
