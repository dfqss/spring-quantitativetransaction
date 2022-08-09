package io.github.talelin.latticy.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    public static String getFormatNowTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

}
