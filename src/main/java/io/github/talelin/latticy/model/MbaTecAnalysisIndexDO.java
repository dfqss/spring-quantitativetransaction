package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_tec_analysis_index")
public class MbaTecAnalysisIndexDO implements Serializable {
    private static final long serialversionUID = -187964648415679735L;

    /**
     * python逻辑删除
     */
    private Integer isDeleted;

    /**
     * 股票编码
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    /**
     * 股票名称
     */
    private String code_name;

    /**
     *5日乖离率_PIT
     */
    private String tech_bias5;

    /**
     *向上有效突破均线
     */
    private String breakout_ma;


    /**
     * 向下有效突破均线
     */
    private String breakdown_ma;

    /**
     *市均线多空头排列看涨看跌
     */
    private String bull_bear_ma;

    /**
     *LON
     */
    @TableField("LON")
    private String LON;

    /**
     * buying
     */
    private String buying;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
