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
@TableName("mba_industry_class")
public class MbaIndustryClassDO implements Serializable {
    private static final long serialversionUID = -146545456352439735L;

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
     *所属申万行业代码
     */
    private String industry_sw_code;

    /**
     *所属申万行业名称
     */
    private String industry_sw;

    /**
     * 所属中信行业代码
     */
    private String industry_cit_code;

    /**
     *所属中信行业名称
     */
    private String industry_cit;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
