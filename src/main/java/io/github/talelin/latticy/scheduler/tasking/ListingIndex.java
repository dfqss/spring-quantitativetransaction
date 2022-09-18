package io.github.talelin.latticy.scheduler.tasking;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.DateUtils;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.ListingDateCalMapper;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.model.ListingDateCalDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ListingIndex extends ServiceImpl<ListingDateCalMapper, ListingDateCalDo> {

    private static String[] methods = {"setCode","setCodeName","setIpoDate"};

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Autowired
    private BatchFiles batchFiles ;

    @Autowired
    private ListingDateCalMapper listingDateCalMapper ;

    /**
     * 批量创建或更新上市日期表
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateListingDateCal() throws Exception {
        // 获取要读取文件的信息
        BatchFilesDO batchFilesDO = getBatchFile();
        // 查询数据库的上市日期数据
        List<ListingDateCalDo> listingDateCalList = listingDateCalMapper.selectList(null);
        if(null == batchFilesDO) {
            log.info("没有需要读取的上市日期文件");
            log.info("------start 开始更新上市日期数据");
            calculation(listingDateCalList, new ArrayList<>());
            this.saveOrUpdateBatch(listingDateCalList);
            log.info("end------更新上市日期完成");
            return;
        }
        // 获取文件路径、文件名称
        String filePath = batchFilesDO.getFilePath();
        String fileName = batchFilesDO.getFileName();
        // 将数据更新为：毫秒值-[读取中]
        batchFiles.updateBatchFilesStatus(fileName,
                String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);
        log.info("------start 开始计算上市日期数据: " + fileName);
        String fullName = filePath + File.separator + fileName;
        try {
            // 读取excel中上市日期的数据
            List<Object> excelDataList = ExcelUtil.
                    readExcel(new File(fullName), 0, 2, methods, ListingDateCalDo.class);
            // 计算上市天数和是否新股
            calculation(listingDateCalList, excelDataList);
            // 批量更新和插入数据
            this.saveOrUpdateBatch(listingDateCalList);
            // 更新数据读取状态为：2-成功
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
            log.info("end------计算上市日期完成: " + fileName);
        }catch (Exception e) {
            log.error("读取核心指标文件失败: " + e);
            // 更新数据读取状态为：1-失败
            batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE, FileLogoConstant.ERROR);
            throw e;
        }
    }

    /**
     * 获取要读取文件的信息
     * @return
     */
    private BatchFilesDO getBatchFile() {
        List<BatchFilesDO> batchFilesList = batchFilesMapper.
                queryBatchFilesByFileType(FileLogoConstant.SSRQ, FileLogoConstant.STATUS_ZERO);
        if(CollectionUtils.isEmpty(batchFilesList)) {
            return null;
        }
        return batchFilesList.get(0);
    }

    /**
     * 计算上市天数和是否新股
     * @param listingDateCalList
     * @param excelDataList
     * @return
     */
    private void calculation(List<ListingDateCalDo> listingDateCalList, List<Object> excelDataList)
            throws ParseException {
        // 数据为空则跳出任务
        if(CollectionUtils.isEmpty(listingDateCalList) && CollectionUtils.isEmpty(excelDataList)) {
            log.info("excel文件和数据库的上市日期数据都为空！");
            return;
        }
        // listingDateCalList为空则创建空对象
        if(null == listingDateCalList) {
            log.info("listingDateCalList为空");
            listingDateCalList = new ArrayList<>();
        }
        // excelDataList为空则创建空对象
        if(null == excelDataList) {
            log.info("excelDataList为空");
            excelDataList = new ArrayList<>();
        }
        Map<String, ListingDateCalDo> listingDateCalMap = listingDateCalList.stream().
                collect(Collectors.toMap(ListingDateCalDo::getCode, listingDateCalDo->listingDateCalDo));
        // 向集合中添加excel中新增的数据
        log.info("上市日期EXCEL文件的数据条数:[" + excelDataList.size() + "]");
        for(Object excelData : excelDataList) {
            String code = ((ListingDateCalDo) excelData).getCode();
            String ipoDate = ((ListingDateCalDo) excelData).getIpoDate();
            if(!listingDateCalMap.containsKey(code)) {
                ListingDateCalDo listingDateCalDo = new ListingDateCalDo();
                listingDateCalDo.setCode(code);
                listingDateCalDo.setIpoDate(ipoDate);
                listingDateCalDo.setCreateTime(new Date());
                listingDateCalDo.setIsDeleted(0);
                listingDateCalList.add(listingDateCalDo);
            }
        }
        // 计算上市天数和是否新股
        log.info("开始计算上市天数和是否新股");
        for(ListingDateCalDo listingDateCal : listingDateCalList) {
            // 计算上市天数
            Integer listingDay = Integer.
                    valueOf(DateUtils.subtractDateResultDays(listingDateCal.getIpoDate(), 3));
            listingDateCal.setListingDay(listingDay);
            // 计算是否新股
            Integer listingMonth = Integer.
                    valueOf(DateUtils.subtractDateResultDays(listingDateCal.getIpoDate(), 1));
            if (listingMonth <= 6) {
                listingDateCal.setIsNewShares(FileLogoConstant.NEW_SHARES_N);
            } else if (listingMonth > 12) {
                listingDateCal.setIsNewShares(FileLogoConstant.NEW_SHARES_F);
            } else {
                listingDateCal.setIsNewShares(FileLogoConstant.NEW_SHARES_C);
            }
            listingDateCal.setUpdateTime(new Date());
        }
    }
}
