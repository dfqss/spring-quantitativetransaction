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
@TableName("mba_growth_index")
public class MbaGrowthIndexDO implements Serializable {
    private static final long serialversionUID = -146545646464646435L;

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
     *基本每股收益(同比增长率)
     */
    private String yoy_eps_basic;

    /**
     *营业总收入(同比增长率)
     */
    private String yoy_tr;

    /**
     * 营业收入(同比增长率)
     */
    private String yoy_or;

    /**
     *营业利润(同比增长率)
     */
    private String yoy_op;

    /**
     *利润总额(同比增长率)
     */
    private String yoy_ebt;

    /**
     * 净利润(同比增长率)
     */
    private String yoy_profit;

    /**
     *净资产(同比增长率)
     */
    private String yoy_equity;

    /**
     *研发费用同比增长
     */
    private String fa_yoy;

    /**
     *总负债(同比增长率)
     */
    private String yoy_debt;

    /**
     *总资产(同比增长率)
     */
    private String yoy_assets_tb;

    /**
     * 每股净资产(相对年初增长率)
     */
    private String yoy_bps;

    /**
     *资产总计(相对年初增长率)
     */
    private String yoy_assets_hb;

    /**
     *营业总收入(N年,增长率)
     */
    private String growth_gr;

    /**
     *营业总成本(N年,增长率)
     */
    private String growth_gc;

    /**
     * 营业收入(N年,增长率)
     */
    private String growth_or;

    /**
     *营业利润(N年,增长率)
     */
    private String growth_op;

    /**
     *单季度.营业收入环比增长率
     */
    private String qfa_cgr_sales;

    /**
     *单季度.营业利润环比增长率
     */
    private String qfa_cgr_op;

    /**
     *单季度.营业收入环比增长率
     */
    private String qfa_cgr_profit;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
