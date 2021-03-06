package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mba_listing_date_cal")
public class MbaListingDateCalDo  implements Serializable {

    private static final long serialversionUID = -146545436346707735L;

    private Integer isDeleted;

    /**
     * 股票编码
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    /**
     * 是否新股  新股:N 非新股:F 次新:C
     */
    private String isNewShares;

    /**
     *上市天数:当前-上市日期
     */
    private Integer listingDay;

    /**
     *上市日期
     */
    private String ipoDate;

    @JsonIgnore
    @TableField(fill =  FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @TableField(fill =  FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;

}
