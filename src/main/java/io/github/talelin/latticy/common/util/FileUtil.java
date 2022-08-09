package io.github.talelin.latticy.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    /**
     * 获取某一个指定目录下的所有指定文件名
     * @param path
     * @param suffix
     * @return
     */
    public static List<String> getFileNameList(String path, String suffix){
        List<String> fileNameList = new ArrayList<>();
        File file = new File(path);
        // 判断文件路径是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        // 获取path目录下后缀为suffix的文件名称
        for(String filename : file.list()) {
            if(filename.endsWith(suffix)) {
                fileNameList.add(filename);
            }
        }
        return fileNameList;
    }

    /**
     * 根据系统的不同获取文件路径的分隔符
     * @param path
     * @param joinPath
     * @return
     */
    public static String joinPath(String path, String joinPath) {
        return path.replace("/", File.separator)
                + File.separator + joinPath.replace("/", File.separator);
    }

    /**
     * 批量修改文件名后缀：修改源文件(同时返回修改后的文件名称)
     * @param path
     * @param fileNameList
     * @param renameFromSuffix
     * @param renameToSuffix
     * @return
     */
    public static List<String> renameFilesSuffix(String path,List<String> fileNameList,
                                                 String renameFromSuffix, String renameToSuffix){
        List<String> returnList = new ArrayList<>();
        for (String fileName : fileNameList) {
            String oldFileName = path + File.separator + fileName;
            String newFileName = path + File.separator + fileName.replace(renameFromSuffix, renameToSuffix);
            new File(oldFileName).renameTo(new File(newFileName));
            returnList.add(fileName.replace(renameFromSuffix, renameToSuffix));
        }
        return returnList;
    }
}
