package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_batch_files")
public class MbaBatchFilesDO implements Serializable {

    private static final long serialversionUID = -1463999455544707735L;

    private Integer isDeleted;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     *文件期数
     */
    private Integer file_periods;

    /**
     *文件读取状态 0-未读 1-失败 2-成功 随机数-读取中
     */
    private String status;

    /**
     *描述
     */
    private String description;

    /**
     *计算日期
     */
    private Date calDate;

    @JsonIgnore
    @TableField(fill =  FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
