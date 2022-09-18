package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_sec_basic_index")
public class SecBasicIndexDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 6763150348616686482L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 总股本[单位]股
    private String totalShares;

    // 自由流通股本[单位]股
    private String freeFloatShares;

    // 流通股本[单位]股
    private String shareIssuingMkt;

    // 总市值
    private String rtMktCap;

    // 流通市值
    private String rtFloatMktCap;

}
