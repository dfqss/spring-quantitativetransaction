package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 顺序排序计数器实体
 */
@Data
@TableName("seq_sort_counter")
public class SeqSortCounterDO implements Serializable {

    private static final long serialVersionUID = 5450647508329116848L;

    // 计数器注册名称
    @TableId(value = "register_name",type = IdType.INPUT)
    private String registerName;

    // 计数值
    private Integer countNum;

    // 自增数值（增长策略：每次取数增长多少）
    private Integer increaseNum;

    // 获取数值大小
    private Integer acquireSize;

    // 备用数（用来防止取数重复）
    private Integer standby;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;
}
