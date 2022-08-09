package io.github.talelin.latticy.common.enumeration;

public enum CodeMessage {

    SUCCESS(0, "成功"),

    CORE_INDEX_IMPORT_SUCCESS(20, "核心指标文件入库成功"),







    FAILED(10200, "失败"),

    CORE_INDEX_IMPORT_ERR(10217, "核心指标文件入库失败");



    private int code ;

    private String message ;

    private CodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
