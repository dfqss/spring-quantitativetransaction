package io.github.talelin.latticy.common.util;

import io.github.talelin.latticy.vo.ResultVo;

/**
 * 返回参数工具类
 */
public class ResultUtil {

    public static ResultVo getSuccessVo() {
        return new ResultVo("0000", "S");
    }

    public static ResultVo getSuccessVo(int messageCode, String message) {
        return new ResultVo("0000", "S", messageCode, message);
    }

    public static ResultVo getFailedVo() {
        return new ResultVo("9999", "F");
    }

    public static ResultVo getFailedVo(int messageCode, String message) {
        return new ResultVo("9999", "F", messageCode, message);
    }
}
