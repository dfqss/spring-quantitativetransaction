package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("mba_industry_class")
public class IndustryClassDO extends MbaModel implements Serializable {

    private static final long serialVersionUID = 4394457444916114225L;

    // 股票编码
    @TableId(value = "code",type = IdType.INPUT)
    private String code;

    // 股票名称
    private String codeName;

    // 所属申万行业代码
    private String industrySwCode;

    // 所属申万行业名称
    private String industrySw;

    // 所属中信行业代码
    private String industryCitCode;

    // 所属中信行业名称
    private String industryCit;

}
