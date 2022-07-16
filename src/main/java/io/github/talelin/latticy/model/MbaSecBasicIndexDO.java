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
@TableName("mba_sec_basic_index")
public class MbaSecBasicIndexDO implements Serializable {
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
     *总股本[单位]股
     */
    private String total_shares;

    /**
     *自由流通股本[单位]股
     */
    private String free_float_shares;

    /**
     * 流通股本[单位]股
     */
    private String share_issuing_mkt;

    /**
     *总市值
     */
    private String rt_mkt_cap;

    /**
     *流通市值
     */
    private String rt_float_mkt_cap;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;
}
