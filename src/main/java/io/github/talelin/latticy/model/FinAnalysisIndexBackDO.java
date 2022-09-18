package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_fin_analysis_index_back")
public class FinAnalysisIndexBackDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 4622685600345099455L;

    // 股票编码
    private String code;

    // 股票名称
    private String codeName;

    // 期数
    private Integer periods;

    // 净资产收益率ROE(平均)
    private String roeAvg;
}
