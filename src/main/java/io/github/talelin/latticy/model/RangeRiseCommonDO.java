package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_range_rise_common")
public class RangeRiseCommonDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = -80344864854819407L;

    // 股票编码
    private String code;

    // 股票名称
    private String codeName;

    // 期数
    private Integer periods;

    // 天涨幅
    private String dayRise;

    // 周涨幅
    private String weekRise;

    // 月涨幅
    private String monthRise;

    // 季度涨幅
    private String quarterRise;


    // 半年涨幅
    private String halfYearRise;

    // 年涨幅
    private String yearRise;

}
