package io.github.talelin.latticy.common.util;

import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.model.CoreIndexDO;
import io.github.talelin.latticy.model.DupontAnalysisIndexDO;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BaseUtil {

    /**
     * 将String数组和Row对象压缩到同一个map中（非格式化类型）
     * @param keys
     * @param row
     * @return
     */
    public static Map<String, String> zipLongest(String[] keys, Row row) {
        if(null == keys || keys.length <= 0) return null;
        if(null == row) return null;
        Map<String, String> zipMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            zipMap.put(keys[i], row.getCell(i).getStringCellValue());
        }
        return zipMap;
    }

    /**
     * 将Row对象压缩到实体bean对象中
     * @param methods
     * @param returnClass
     * @param row
     * @return
     * @throws Exception
     */
    public static Object zipLongest(String[] methods, Class returnClass, Row row) throws Exception {
        if(null == methods || methods.length <= 0) return null;
        if(null == row) return null;
        if(returnClass.equals(Map.class)) {
            return zipLongest(methods, row);
        }
        // 获取类的对象
        Object obj = returnClass.getConstructor().newInstance();
        // 反射调用methods中的方法：只能是单入参的set方法
        for (int i = 0; i < methods.length; i++) {
            Method[] invokeMethods = returnClass.getMethods();
            for(Method invokeMethod : invokeMethods) {
                if(methods[i].equals(invokeMethod.getName())) {
                    Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
                    Method method = returnClass.getMethod(methods[i], parameterTypes);
                    method.invoke(obj, formatValue(row.getCell(i).getStringCellValue(), parameterTypes[0]));
                }
            }
        }
        return obj;
    }

    /**
     * 格式化传入的String值
     * @param value
     * @param parameterType
     * @return
     * @throws ParseException
     */
    private static Object formatValue(String value, Class parameterType) throws ParseException {
        if(parameterType == Integer.class || parameterType == int.class) {
            return Integer.valueOf(value);
        }else if(parameterType == Short.class || parameterType == short.class) {
            return Short.valueOf(value);
        }else if(parameterType == Long.class || parameterType == long.class) {
            return Long.valueOf(value);
        }else if(parameterType == Date.class) {
            return new SimpleDateFormat(FileLogoConstant.yyyy_MM_dd).parse(value);
        }
        return value;
    }

    /**
     * 首字母变大写
     * @param str
     * @return
     */
    public static String upperFirstCase(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母变小写
     * @param str
     * @return
     */
    public static String lowerFirstCase(String str){
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
