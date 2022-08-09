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
public class StockValueDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = -57885592033604483L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 总市值1[单位]元
    private String ev;

    // 流通市值(含限售股)[单位]元
    private String mktCapFloat;

    // 自由流通市值[单位]元
    private String mktFreeShares;

    // 市净率PB(MRQ)
    private String pbMrq;

    // 市净率PB(LYR)
    private String pbLyr;

    // 营业总收入(TTM)_PIT[单位]元
    private String grTtm;

    // 营业收入(TTM)_VAL_PIT[单位]元
    private String orTtm;

    // 净利润(TTM)_PIT[单位]元
    private String profitTtm;

    // 每股收益EPS(TTM)_PIT
    private String epsTtm;

    // 每股营业收入(TTM)_PIT
    private String orPsTtm;

}
