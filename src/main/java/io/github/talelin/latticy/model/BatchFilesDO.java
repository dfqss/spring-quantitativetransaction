package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_batch_files")
public class BatchFilesDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 232082085143960750L;

    // 文件名称
    @TableId(value = "file_name",type = IdType.INPUT)
    private String fileName;

    // 文件路径
    private String filePath;

    // 文件期数
    private Integer filePeriods;

    // 文件读取状态 0-未读 1-失败 2-成功 随机数-读取中
    private String status;

    // 描述
    private String description;

    // 计算日期
    private Date calDate;

}
