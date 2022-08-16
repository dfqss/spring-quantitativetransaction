package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_fin_analysis_index_back")
public class FinAnalysisIndexBackDO extends MbaModel implements Serializable {
    private static final long serialVersionUID = -7034646544819407L;

    // 股票编码
    private String code;

    // 股票名称
    private String codeName;

    // 期数
    private Integer periods;

    // 净资产收益率ROE(平均)
    private String roeAvg;

    // 净资产收益率ROE(加权)
    private String roeBasic;

    // 总资产净利率ROA报告期
    private String roa;

    // 销售毛利率
    private String grossProfitMargin;

    // 销售净利率
    private String netProfitMargin;

    // 营业总收入[单位]元
    private String totOpeRev;

    // 营业收入[单位]元
    private String opeRev;

    // 商誉[单位]元
    private String goodwill;

    // 开发支出[单位]元
    private String rAndDCosts;

    // 主营收入构成
    private String segmentSales;

    // 资产负债率
    private String debtToAssets;

    // 现金比率
    private String cashToCurrentDebt;

    // 市盈率PE
    private String pe;

    // 市净率PB
    private String pb;

    // 每股营业总收入
    private String grPs;

    // 每股营业收入
    private String orPs;

    // 每股现金流量净额
    private String cfPs;

    // 每股收益EPS-基本
    private String epsBasic;

    // 每股净资产BPS
    private String bps;
}
