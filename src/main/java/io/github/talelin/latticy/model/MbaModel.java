package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class MbaModel {

    // 删除标志
    private Integer isDeleted;

    // 创建时间
    @JsonIgnore
    private Date createTime;

    // 更新时间
    @JsonIgnore
    private Date updateTime;

    // 删除时间
    @TableLogic
    @JsonIgnore
    private Date deleteTime;

}
