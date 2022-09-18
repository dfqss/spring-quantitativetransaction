package io.github.talelin.latticy.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * @param subtractDate 减去指定日期   格式：yyyy-MM-dd
     * @param type 1: 返回月  2: 返回年 其他返回天
     * @return 返回指定天数(当前日期-指定日期)
     */
    public static String subtractDateResultDays(String subtractDate,Integer type) throws ParseException {
        // 1.DateFormat类中的parse方法把字符串day解析为Date格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //到这一步可能有异常直接alt+回车抛出去，一直抛直到没有报错留给虚拟机处理
        Date targerDay = sdf.parse(subtractDate);
        // 2.把Date格式的出生日期转换为毫秒值用Date类的getTime()
        long targetTime = targerDay.getTime();
        // 3.获取当前日期，转换为毫秒值getTime()
        long todaytime = new Date().getTime();
        long time = Math.abs(targetTime - todaytime);
        // 4.用format()方法将一个 Date 格式化为日期/时间字符串 返回一个字符串。
        long resultDays = time / 1000 / 60 / 60 / 24;
        if (type == 1) {
            return resultDays / 30 + "";
        }else if (type == 2){
            return resultDays / 365 + "";
        }
        return resultDays + "";
    }
}
