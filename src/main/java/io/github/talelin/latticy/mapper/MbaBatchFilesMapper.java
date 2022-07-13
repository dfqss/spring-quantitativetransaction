package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.MbaBatchFilesDO;
import io.github.talelin.latticy.model.PermissionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MbaBatchFilesMapper extends BaseMapper<MbaBatchFilesDO> {

    /**
     * 新增execl文件表信息
     *
     * @param fileName  文件名称
     * @param filePath 文件路径
     * @return 用户名
     */
    int createBatchFiles(String fileName,String filePath,String type);


    /**
     * 更新批量文件数据表状态 0-未读 1-失败 2-成功 随机数-读取中
     */
    void updateBatchFilesStatus(String fileName,String status,String description);


    /**
     * 查询文件路径与文件名称
     * @param fileName 文件保护名称
     * @param status   文件状态
     * @return
     */
    List<MbaBatchFilesDO> fileMessage(String fileName, String status);

}
