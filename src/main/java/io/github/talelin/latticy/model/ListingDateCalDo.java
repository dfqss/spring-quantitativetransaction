package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_listing_date_cal")
public class ListingDateCalDo extends MbaModel implements Serializable {

    private static final long serialVersionUID = -425596287547532721L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 是否新股  新股:N 非新股:F 次新:C
    private String isNewShares;

    // 上市天数:当前-上市日期
    private Integer listingDay;

    // 上市日期
    private String ipoDate;
}
