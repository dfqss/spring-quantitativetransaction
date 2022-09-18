package io.github.talelin.latticy.common.enumeration;

public enum CodeMessage {

    /**
     * 码值-成功
     */
    SUCCESS(0, "成功"),

    READ_FILE_SUCCESS(19, "文件入库成功"),

    CORE_INDEX_IMPORT_SUCCESS(20, "读取核心指标文件数据成功"),

    LISTING_DATE_CAL_SUCCESS(22, "读取上市日期文件数据成功"),

    OTHER_INDEX_IMPORT_SUCCESS(21, "读取批量指标文件数据成功"),

    /**
     * 码值-失败
     */
    FAILED(10200, "失败"),

    READ_FILE_ERR(10214, "文件入库失败"),

    CORE_INDEX_IMPORT_ERR(10217, "读取核心指标文件数据失败"),

    LISTING_DATE_CAL_ERR(10219, "读取上市日期文件数据失败"),

    OTHER_INDEX_IMPORT_ERR(10216, "读取批量指标文件数据失败");

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
