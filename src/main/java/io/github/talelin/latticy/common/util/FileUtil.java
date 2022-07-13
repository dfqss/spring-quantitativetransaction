package io.github.talelin.latticy.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {


    /**
     * 获取某一个指定目录下的所有指定文件名
     * @return
     */
    public static List<String> getFileNameList(String path,String suffix){
        List<String> fileNameList = new ArrayList<>();
        //获取文件绝对路径
        path = new File(path).getAbsolutePath();
        File file = new File(path);
        // 判断文件路径是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        // 获取path目录下后缀为suffix的文件名称
        String[] fileArray = file.list();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].contains(suffix)) {
                fileNameList.add(fileArray[i]);
            }
        }
        System.out.println(fileNameList);
        return fileNameList;
    }


    /**
     * 处理文件路径
     * File.separator:根据系统的不同获取文件路径的分隔符
     *
     * @return filePath
     */
    public static String joinFilePath(String filePath) {

        //1.获取当前系统时间并格式化
        Date dNow = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFormat = simpleDateFormat.format(dNow);

        //2.将文件路径转为所运行操作系统下的路径
        String replace = filePath.replace("/", File.separator);

        //3.拼接文件路径
        filePath = replace + File.separator + dateFormat;

        return filePath;
    }

    
    /**
     * 批量修改文件名后缀：修改源文件(同时返回修改后的文件名称)
     * @return
     * path 文件路径
     * fileNameList 文件名称集合
     * renameFromSuffix 更改文件名后缀
     * renameToSuffix  更改后的文件名后缀
     */
    public static List<String> renameFilesSuffix(String path,List<String> fileNameList,String renameFromSuffix,String renameToSuffix){
        List<String> returnList = new ArrayList<>();
        for (String fileName : fileNameList) {
            String oldFileName = path + File.separator + fileName;
            String newFileName = path + File.separator + fileName.replace(renameFromSuffix,renameToSuffix);
            new File(oldFileName).renameTo(new File(newFileName));
            returnList.add(newFileName);
        }
        return returnList;
    }
}
