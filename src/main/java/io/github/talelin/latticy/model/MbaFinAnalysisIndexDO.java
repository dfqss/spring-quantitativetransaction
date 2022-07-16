package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_fin_analysis_index")
public class MbaFinAnalysisIndexDO implements Serializable {

    private static final long serialversionUID = -146549349545437735L;

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
     *净资产收益率ROE(平均)
     */
    private String roe_avg;

    /**
     *净资产收益率ROE(加权)
     */
    private String roe_basic;

    /**
     * 总资产净利率ROA报告期
     */
    private String roa;

    /**
     *销售毛利率
     */
    private String gross_profit_margin;

    /**
     *销售净利率
     */
    private String net_profit_margin;

    /**
     * 营业总收入[单位]元
     */
    private String tot_ope_rev;

    /**
     *营业收入[单位]元
     */
    private String ope_rev;

    /**"ope_rev","goodwill","r_and_d_costs","segment_sales",""
     *商誉[单位]元
     */
    private String goodwill;

    /**
     *开发支出[单位]元
     */
    private String r_and_d_costs;

    /**
     *主营收入构成
     */
    private String segment_sales;

    /**
     * 资产负债率
     */
    private String debt_to_assets;

    /**
     *现金比率
     */
    private String cash_to_current_debt;

    /**
     *市盈率PE
     */
    private String pe;

    /**
     *市净率PB
     */
    private String pb;

    /**
     *每股营业总收入
     */
    private String gr_ps;

    /**
     * 每股营业收入
     */
    private String or_ps;

    /**
     *每股现金流量净额
     */
    private String cf_ps;

    /**
     *每股收益EPS-基本
     */
    private String eps_basic;

    /**
     *每股净资产BPS
     */
    private String bps;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
