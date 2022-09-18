package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_dupont_analysis_index")
public class DupontAnalysisIndexDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 6214278738112002420L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 净资产收益率ROE
    private String roe;

    // 总资产周转率
    private String assetsTurn;

    // 归属母公司股东的净利润/净利润
    private String dupontNp;

    // 净利润/营业总收入
    private String profitToGr;

    // 净利润/利润总额
    private String dupontTaxBurden;

    // 利润总额/息税前利润
    private String dupontIntBurden;

    // 息税前利润/营业总收入
    private String ebiToGr;

}
