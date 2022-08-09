package io.github.talelin.latticy.common.util;

import com.monitorjbl.xlsx.StreamingReader;
import io.github.talelin.latticy.model.CoreIndexDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Slf4j
public class ExcelUtil extends BaseUtil {

    /**
     * 读取excel文件
     * @param file          excel文件对象
     * @param sheetIndex    读取文件的sheet页的索引位置
     * @param startIndex    从当前sheet页的行索引开始读取
     * @param methods       每列数据的获取方法名的数据列表（按照列的顺序排序）
     * @param returnClass   list集合中数据的对象类型
     * @return
     * @throws Exception
     */
    public static List<Object> readExcel(File file, Integer sheetIndex, Integer startIndex,
                                                      String[] methods, Class returnClass) throws Exception {
        // 判断文件对象是否为空，为空则返回异常信息
        if(null == file) {
            log.error("文件对象不能为空");
            throw new Exception("文件对象不能为空");
        }
        // 判断文件是否存在，不存在则返回异常信息
        if (!file.exists()) {
            log.error("文件" + file.getAbsolutePath() + "不存在");
            throw new Exception("文件" + file.getAbsolutePath() + "不存在");
        }
        // 读取文件对象
        List<Object> excelList = new ArrayList<>();
        FileInputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(inputStream);
            // 获取指定的工作薄
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            //遍历所有的行
            int count = 0;
            for(Row row : sheet) {
                if(count < startIndex) {
                    count++;
                    continue;
                }
                Object obj = zipLongest(methods, returnClass, row);
                excelList.add(obj);
                count++;
            }
        }catch (Exception e) {
            log.error("读取excel文件异常:" + e);
            throw e;
        }finally {
            if(null != workbook) workbook.close();
            if(null != inputStream) inputStream.close();
        }
        return excelList;
    }
}
