package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_tec_analysis_index")
public class TecAnalysisIndexDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 8725615298616132254L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 5日乖离率_PIT
    private String techBias5;

    // 向上有效突破均线
    private String breakoutMa;

    // 向下有效突破均线
    private String breakdownMa;

    // 市均线多空头排列看涨看跌
    private String bullBearMa;

    // LON
    @TableField("LON")
    private String LON;

    // buying
    private String buying;

}
