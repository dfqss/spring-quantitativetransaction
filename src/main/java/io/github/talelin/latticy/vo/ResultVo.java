package io.github.talelin.latticy.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.talelin.latticy.common.enumeration.CodeMessage;
import lombok.Data;

import java.util.List;

/**
 * 通用数据返回对象
 */
@Data
public class ResultVo {

    private String code ;

    private String ret ;

    @JsonProperty("messageCode")
    private Integer messageCode ;

    private String message ;

    @JsonProperty("dataList")
    private List<Object> dataList ;

    @JsonProperty("totalNum")
    private int totalNum ;

    public ResultVo(String code, String ret) {
        this.code = code;
        this.ret = ret;
        if("0000".equals(code)) {
            this.messageCode = CodeMessage.SUCCESS.getCode();
            this.message = CodeMessage.SUCCESS.getMessage();
        }else if("9999".equals(code)) {
            this.messageCode = CodeMessage.FAILED.getCode();
            this.message = CodeMessage.FAILED.getMessage();
        }
    }

    public ResultVo(String code, String ret, int messageCode, String message) {
        this.code = code;
        this.ret = ret;
        this.messageCode = messageCode;
        this.message = message;
    }
}
