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
@TableName("mba_dupont_analysis_index")
public class MbaDupontAnalysisIndexDO implements Serializable {
    private static final long serialversionUID = -1465454643452279735L;

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
     *净资产收益率ROE
     */
    private String roe;

    /**
     *总资产周转率
     */
    private String assets_turn;

    /**
     * 归属母公司股东的净利润/净利润
     */
    private String dupont_np;

    /**
     *净利润/营业总收入
     */
    private String profit_to_gr;

    /**
     *净利润/利润总额
     */
    private String dupont_tax_burden;

    /**
     * 利润总额/息税前利润
     */
    private String dupont_int_burden;

    /**
     *息税前利润/营业总收入
     */
    private String ebi_to_gr;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;

}
