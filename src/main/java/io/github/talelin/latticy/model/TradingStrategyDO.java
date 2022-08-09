package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_trading_strategy")
public class TradingStrategyDO implements Serializable {

    private static final long serialVersionUID = -2054129517676392037L;

    // 策略id
    private String strategyId;

    // 股票代码
    private String code;

    // 策略类型名称
    private String strategyType;

    // 策略内容
    private String strategy;

    // 标记1
    private String remark1;

    // 标记2
    private String remark2;

    // 标记3
    private String remark3;

    // 描述
    private String description;

    // 删除标志
    private Integer isDeleted;

    // 创建时间
    @JsonIgnore
    private Date createTime;

    // 更新时间
    @JsonIgnore
    private Date updateTime;

}
