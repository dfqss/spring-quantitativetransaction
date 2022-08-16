package io.github.talelin.latticy.common.enumeration;

public enum CodeMessage {

    /**
     * 码值-成功
     */
    SUCCESS(0, "成功"),

    CORE_INDEX_IMPORT_SUCCESS(20, "核心指标文件入库成功"),

    OTHER_INDEX_IMPORT_SUCCESS(21, "批量指标文件入库成功"),

    /**
     * 码值-失败
     */
    FAILED(10200, "失败"),

    CORE_INDEX_IMPORT_ERR(10217, "核心指标文件入库失败"),

    OTHER_INDEX_IMPORT_ERR(10216, "批量指标文件入库失败");

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
