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
@TableName("mba_stock_value")
public class MbaStockValueDO implements Serializable {
    private static final long serialversionUID = -146545488415679735L;

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
     *总市值1[单位]元
     */
    private String ev;

    /**
     *流通市值(含限售股)[单位]元
     */
    private String mkt_cap_float;

    /**
     * 自由流通市值[单位]元
     */
    private String mkt_free_shares;

    /**
     *市净率PB(MRQ)
     */
    private String pb_mrq;
    /**
     *市净率PB(LYR)
     */
    private String pb_lyr;

    /**
     * 营业总收入(TTM)_PIT[单位]元
     */
    private String gr_ttm;

    /**
     *营业收入(TTM)_VAL_PIT[单位]元
     */
    private String or_ttm;

    /**
     *净利润(TTM)_PIT[单位]元
     */
    private String profit_ttm;

    /**
     * 每股收益EPS(TTM)_PIT
     */
    private String eps_ttm;

    /**
     *每股营业收入(TTM)_PIT
     */
    private String or_ps_ttm;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
