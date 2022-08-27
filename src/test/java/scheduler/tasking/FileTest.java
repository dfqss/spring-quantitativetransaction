package scheduler.tasking;


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTest {


    @Test
    public void fileRenameIndexCoreBack() throws IOException {

        //遍历文件夹下所有文件
        String inputPath = "C:\\Users\\29686\\Desktop\\core";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅半年";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅季度";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\财务指标";
        File file = new File(inputPath);      //获取其file对象
        File[] fs = file.listFiles();     //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                //遍历File[]数组

            String fileName = f.getName();  //获取文件和目录名

            //1.更改文件名称
            String[] split = fileName.split("_");
            String newFileName = split[0] + "-" + split[1] + "_" + split[3].substring(0, split[3].indexOf(".")) + "_" + split[2] + ".xlsx";
//            String newFileName = split[0] + "-" + split[1] + "_" + split[2];//财务指标
//            String newFileName = split[0] + "-" + split[1] + "-" + split[2] + "_" + split[3];//区间涨幅
            String absolutePath = inputPath + File.separator + newFileName;
            f.renameTo(new File(absolutePath));


            //2.创建OK文件
            String okFilePath = absolutePath.replaceAll("xlsx", "ok");
            File okFile = new File(okFilePath);
            //如果文件不存在即创建文件
            if (!okFile.exists()) {
                okFile.createNewFile();
            }
        }
    }









    @Test
    public void fileRenameFinAnalysisIndexBac() throws IOException {

        //遍历文件夹下所有文件
        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\财务指标";
        File file = new File(inputPath);      //获取其file对象
        File[] fs = file.listFiles();     //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                //遍历File[]数组

            String fileName = f.getName();  //获取文件和目录名 HC_JZCSYL_1.xlsx

            //1.更改文件名称
            String[] split = fileName.split("_");
            String newFileName = split[0] + "-" + split[1] + "_" + split[2];
            String absolutePath = inputPath + File.separator + newFileName;
            f.renameTo(new File(absolutePath));

            //2.创建OK文件
            String okFilePath = absolutePath.replaceAll("xlsx", "ok");
            File okFile = new File(okFilePath);
            //如果文件不存在即创建文件
            if (!okFile.exists()) {
                okFile.createNewFile();
            }
        }
    }

    @Test
    public void fileRenameRangeRise() throws IOException {

        //遍历文件夹下所有文件
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅半年";
        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅季度";
        File file = new File(inputPath);      //获取其file对象
        File[] fs = file.listFiles();     //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                //遍历File[]数组

            String fileName = f.getName();  //获取文件和目录名 HC_JZCSYL_1.xlsx
            System.out.println(fileName);
            //1.更改文件名称
            String[] split = fileName.split("_");
            for (String s : split) {
                System.out.println(s);
            }
            String newFileName = split[0] + "-" + split[1] + "-" + split[2] + "_" + split[3];
            System.out.println("==========" + newFileName);
            String absolutePath = inputPath + File.separator + newFileName;
            f.renameTo(new File(absolutePath));

            //2.创建OK文件
            String okFilePath = absolutePath.replaceAll("xlsx", "ok");
            File okFile = new File(okFilePath);
            //如果文件不存在即创建文件
            if (!okFile.exists()) {
                okFile.createNewFile();
            }
        }
    }

    @Test
    public void fileRename(){

        //遍历文件夹下所有文件
        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\core\\20220823";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅半年";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\区间涨幅季度";
//        String inputPath = "C:\\Users\\29686\\Desktop\\股票回测\\财务指标";
        File file = new File(inputPath);      //获取其file对象
        File[] fs = file.listFiles();     //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                //遍历File[]数组

            String fileName = f.getName();  //获取文件和目录名

            //1.更改文件名称
            String newFileName = fileName.replaceAll("success","ok");
            String absolutePath = inputPath + File.separator + newFileName;
            f.renameTo(new File(absolutePath));

        }
    }

    @Test
    public void test1() throws ParseException {
        String reportDate = "20221003";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        Date date = simpleDateFormat.parse(reportDate);
        System.out.println(date);
    }
}
