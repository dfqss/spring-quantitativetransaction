package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_range_rise_rare")
public class RangeRiseRareDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = -8034646544846465407L;

    // 股票编码
    private String code;

    // 期数
    private Integer periods;

    // 近三年涨幅
    private String recentThreeYears;

    // 近五年涨幅
    private String recentFiveYears;

    // 近十年涨幅
    private String recentTenYears;

    // 预留涨幅1
    private String reservedYear1;


    // 预留涨幅2
    private String reservedYear2;

    // 预留涨幅3
    private String reservedYear3;
}
