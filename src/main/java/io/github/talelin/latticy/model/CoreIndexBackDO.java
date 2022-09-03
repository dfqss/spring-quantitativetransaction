package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_core_index_back")
public class CoreIndexBackDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = -2224304896652755356L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 最新核心指标
    private String currentCore;

    // 次新核心指标
    private String periodCore;

    // 最终计算核心指数
    private String finalCalCore;

    // 期数
    private Integer periods;

    // 展示状态：0-展示 1-不展示
    private String status;

    // 展示次数
    private Integer showTimes;

    // 获取日期（年月日）
    private Date calDate;

    // 报告日期（年月日）
    private Date reportDate;
}
