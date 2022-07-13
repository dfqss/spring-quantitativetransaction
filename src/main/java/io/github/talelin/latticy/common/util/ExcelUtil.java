package io.github.talelin.latticy.common.util;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Slf4j
public class ExcelUtil {


    /**
     * 读取excel文件
     *
     * @param file       excel文件
     * @param keyList    每列数据的key列表（按照列的顺序排序）
     * @param sheetIndex 读取文件的sheet页的索引位置
     * @param startIndex 从当前sheet页的行索引开始读取
     * @return
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> readExcel(File file, List<String> keyList,
                                                               Integer sheetIndex, Integer startIndex) throws Exception {
        //判断文件是否存在，不存在则返回异常信息
        if (!file.exists()) {
            log.info(file.getAbsolutePath() + "==>execl文件不存在");
            return null;
        }
        //读取excel文件
        try {
            log.info("===============开始读取execl文件:" + file.getAbsolutePath());
            ArrayList<HashMap<String, String>> resultList = new ArrayList<>();
            FileInputStream in = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                    .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            /*
            //遍历所有的sheel页
            int sheetNums = wk.getNumberOfSheets();
            for (int i = 0; i < sheetNums; i++) {
                int k = 0;
                for (Row row : sheet) {
                    HashMap<String, String> map = new HashMap<>();
                    //遍历所有的列
                    for (Cell cell : row) {
                        map.put(keyList.get(k), cell.getStringCellValue());
                        k++;
                        if (k % keyList.size() == 0) {
                            k = 0;
                            resultList.add(map);
                        }
                    }
                }
            }
            */
            //获取第几个sheet
            Sheet sheet = wk.getSheetAt(sheetIndex);
            //遍历所有的行
            int k = 0;
            int flag = 0;
            for (Row row : sheet) {
                if (flag < startIndex) {
                    flag++;
                    continue;
                }
                HashMap<String, String> map = new HashMap<>();
                //遍历所有的列
                for (Cell cell : row) {
                    map.put(keyList.get(k), cell.getStringCellValue());
                    k++;
                    if (k % keyList.size() == 0) {
                        k = 0;
                        resultList.add(map);
                    }
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("读取excel文件错误:" + e.toString());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        long t1 = new Date().getTime();
        long t2 = new Date().getTime();
        System.out.println((t2 - t1) / 1000 + "秒");

    }

}
