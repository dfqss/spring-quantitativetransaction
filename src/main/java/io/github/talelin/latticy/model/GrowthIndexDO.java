package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_growth_index")
public class GrowthIndexDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 6268871982023617179L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 基本每股收益(同比增长率)
    private String yoyEpsBasic;

    // 营业总收入(同比增长率)
    private String yoyTr;

    // 营业收入(同比增长率)
    private String yoyOr;

    // 营业利润(同比增长率)
    private String yoyOp;

    // 利润总额(同比增长率)
    private String yoyEbt;

    // 净利润(同比增长率)
    private String yoyProfit;

    // 净资产(同比增长率)
    private String yoyEquity;

    // 研发费用同比增长
    private String faYoy;

    // 总负债(同比增长率)
    private String yoyDebt;

    // 总资产(同比增长率)
    private String yoyAssetsTb;

    // 每股净资产(相对年初增长率)
    private String yoyBps;

    // 资产总计(相对年初增长率)
    private String yoyAssetsHb;

    // 营业总收入(N年,增长率)
    private String growthGr;

    // 营业总成本(N年,增长率)
    private String growthGc;

    // 营业收入(N年,增长率)
    private String growthOr;

    // 营业利润(N年,增长率)
    private String growthOp;

    // 单季度.营业收入环比增长率
    private String qfaCgrSales;

    // 单季度.营业利润环比增长率
    private String qfaCgrOp;

    // 单季度.营业收入环比增长率
    private String qfaCgrProfit;

}
