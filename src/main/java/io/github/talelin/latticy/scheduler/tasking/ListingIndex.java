package io.github.talelin.latticy.scheduler.tasking;

import io.github.talelin.latticy.common.util.DateUtils;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.ListingDateCalDo;
import io.github.talelin.latticy.service.BatchFilesService;

import io.github.talelin.latticy.service.ListingDateCalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.File;
import java.text.ParseException;
import java.util.*;

@Component
@Slf4j
public class ListingIndex {

    @Autowired
    private BatchFilesService mbaBatchFilesService;

    @Autowired
    private ListingDateCalService mbaListingDateCalService;


    /**
     * 批量创建或更新上市日期表
     */
    public void createOrUpdateListingDateCal() {
//        //获取要读取文件的信息
//        List<BatchFilesDO> fileMessages = getFileNames("SSRQ", "0");
//        System.out.println(fileMessages);
//        //1.没有查询到要读取的文件直接返回
//        if (fileMessages.size() <= 0) {
//            log.info("没有需要更新的上市日期数据");
//            log.info("自动更新表字段【上市日期天数 &&是否新股】");
//            this.updateDaysANDIsnew();
//            return;
//        }
//
//        //2.获取文件路径、文件名称
//        String fileName = fileMessages.get(0).getFileName();
//        String filePath = fileMessages.get(0).getFilePath();
//
//        //获取毫秒值
//        String dateTime = new Date().getTime() + "";
//        //3.将数据更新为：毫秒值-[读取中]
//        mbaBatchFilesService.updateBatchFilesStatus(fileName, dateTime, "正在读取中");
//        try {
//            log.info("------start 开始计算上市日期: " + fileName);
//            // 4.读取excel中上市日期的数据
//            List<String> keyList = new ArrayList<>();
//            Collections.addAll(keyList, "code", "name", "ipoDate");
//            String absolutePath = filePath + File.separator + fileName;
////            List<HashMap<String, String>> execlDate = ExcelUtil.readExcel(new File(absolutePath), 0, 2, keyList);
//            //5.计算上市天数，是否新股
////            this.computeDaysANDIsnew(execlDate);
//            //5.封装DO对象
//            List<ListingDateCalDo> mbaListingDateCalDoList = encapsulationDo(execlDate);
//            log.info("start-----------【开始插入或更新表数据】");
//            mbaListingDateCalService.saveOrUpdateBatch(mbaListingDateCalDoList);
//            log.info("end-----------【插入或更新表数据成功】");
//            //6.将数据更新为：读取文件成功
//            mbaBatchFilesService.updateBatchFilesStatus(fileName, "1", "成功");
//        } catch (Exception e) {
//            log.error(e.toString());
//            mbaBatchFilesService.updateBatchFilesStatus(fileName, "2", "失败");
//        }
    }

    /**
     * 查询mba_batch_files表中需要写入的上市时间文件的路径和文件名
     */
    private List<BatchFilesDO> getFileNames(String fileName, String status) {
//        return mbaBatchFilesService.fileMessage(fileName, status);
        return null;
    }


    /**
     * 计算上市天数，是否新股
     *
     * @param execlDate execl数据
     * @return 最后封装的dao对象(List < Map >)
     */
    private List<HashMap<String, String>> computeDaysANDIsnew(ArrayList<HashMap<String, String>> execlDate) throws ParseException {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        for (int i = 0; i < execlDate.size(); i++) {
            HashMap<String, String> rowMap = execlDate.get(i);
            String ipoDate = rowMap.get("ipoDate");
            String subtractDays = DateUtils.subtractDateResultDays(ipoDate, 3);
            rowMap.put("listingDay", subtractDays);
            String subtractMonth = DateUtils.subtractDateResultDays(ipoDate, 1);
            int intSubtractMonth = Integer.parseInt(subtractMonth);
            if (intSubtractMonth <= 6) {
                rowMap.put("isNewShares", "N");
            } else if (intSubtractMonth > 12) {
                rowMap.put("isNewShares", "F");
            } else {
                rowMap.put("isNewShares", "C");
            }
            resultList.add(rowMap);
        }
        return resultList;
    }


    /**
     * 更新表中的上市天数，是否新股
     */
    private void updateDaysANDIsnew() {
        try {
            //更新表数据
            List<ListingDateCalDo> listingDateCalDoList = mbaListingDateCalService.list();
            for (ListingDateCalDo dao : listingDateCalDoList) {
                String ipoDate = dao.getIpoDate();
                ipoDate = ipoDate.split(" ")[0];
                String subtractDays = DateUtils.subtractDateResultDays(ipoDate, 3);
                dao.setListingDay(Integer.valueOf(subtractDays));
                String subtractMonth = DateUtils.subtractDateResultDays(ipoDate, 1);
                int intSubtractMonth = Integer.parseInt(subtractMonth);
                if (intSubtractMonth <= 6) {
                    dao.setIsNewShares("N");
                } else if (intSubtractMonth > 12) {
                    dao.setIsNewShares("F");
                } else {
                    dao.setIsNewShares("C");
                }
                dao.setUpdateTime(new java.sql.Date(new Date().getTime()));
            }
            mbaListingDateCalService.saveOrUpdateBatch(listingDateCalDoList);
        } catch (ParseException e) {
            log.error("更新表中的上市天数，是否新股----失败");
            e.printStackTrace();
        }

    }


    /**
     * 封装DO
     * @param sourceList
     * @return
     */
    private List<ListingDateCalDo> encapsulationDo(List<HashMap<String,String>> sourceList){
        List<ListingDateCalDo> resultList = new ArrayList<>();
        for (HashMap<String, String> map : sourceList) {
            ListingDateCalDo mbaListingDateCalDo = new ListingDateCalDo();
            mbaListingDateCalDo.setCode(map.get("code"));
            mbaListingDateCalDo.setListingDay(Integer.valueOf(map.get("listingDay")));
            mbaListingDateCalDo.setIpoDate(map.get("ipoDate"));
            mbaListingDateCalDo.setIsNewShares(map.get("isNewShares"));
            mbaListingDateCalDo.setCreateTime(new Date());
            mbaListingDateCalDo.setUpdateTime(new Date());
            resultList.add(mbaListingDateCalDo);
        }
        return resultList;
    }

    public static void main(String[] args) {
        System.out.println(new java.sql.Date(new Date().getTime()));
    }
}
