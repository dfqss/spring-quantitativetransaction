package io.github.talelin.latticy.scheduler.tasking;

import com.alibaba.fastjson.JSON;
import io.github.talelin.latticy.bo.EntityBo;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.BaseUtil;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.configuration.ApplicationConfiguration;
import io.github.talelin.latticy.mapper.BatchFilesMapper;
import io.github.talelin.latticy.mapper.OtherIndexMapper;
import io.github.talelin.latticy.model.*;
import io.github.talelin.latticy.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 上传其他文件类型
 */
@Component
@Slf4j
public class OtherIndex {

    @Autowired
    private ApplicationConfiguration config ;

    @Autowired
    private BatchFilesMapper batchFilesMapper ;

    @Autowired
    private BatchFiles batchFiles ;

    @Autowired
    private OtherIndexMapper otherIndexMapper ;

    private static Map<String, EntityBo> entityMap = new HashMap<>();

    private int batchCommit = 500;

    static {
        // 财务分析指标
        entityMap.put(FileLogoConstant.CWFXZB,
                new EntityBo(FileLogoConstant.MBA_FIN_ANALYSIS_INDEX, FinAnalysisIndexDO.class));
        // 成长指标
        entityMap.put(FileLogoConstant.CZZB,
                new EntityBo(FileLogoConstant.MBA_GROWTH_INDEX, GrowthIndexDO.class));
        // 杜邦分析指标
        entityMap.put(FileLogoConstant.DBFXZB,
                new EntityBo(FileLogoConstant.MBA_DUPONT_ANALYSIS_INDEX, DupontAnalysisIndexDO.class));
        // 股票估值
        entityMap.put(FileLogoConstant.GPGZ,
                new EntityBo(FileLogoConstant.MBA_STOCK_VALUE, StockValueDO.class));
        // 技术分析指标
        entityMap.put(FileLogoConstant.JSFXZB,
                new EntityBo(FileLogoConstant.MBA_TEC_ANALYSIS_INDEX, TecAnalysisIndexDO.class));
        // 行业分类
        entityMap.put(FileLogoConstant.HYFL,
                new EntityBo(FileLogoConstant.MBA_INDUSTRY_CLASS, IndustryClassDO.class));
        // 证券基础指标
        entityMap.put(FileLogoConstant.ZQJCZB,
                new EntityBo(FileLogoConstant.MBA_SEC_BASIC_INDEX, SecBasicIndexDO.class));
    }

    /**
     * 创建投资指标数据
     * @throws Exception
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createOrUpdateOtherIndex() throws Exception {
        for(String fileType : config.getFileTypes()) {
            log.info("--------start 开始读取批量指标文件-类型为：" + fileType);
            // 获取要读取文件的信息
            List<BatchFilesDO> file = batchFilesMapper.
                    queryBatchFilesByFileType(fileType, FileLogoConstant.STATUS_ZERO);
            // 没有查询到要读取的文件直接返回
            if(CollectionUtils.isEmpty(file)) {
                log.info("该文件类型下没有需要读取的数据文件" + fileType);
                continue;
            }
            // 获取文件路径、文件名称
            String filePath = file.get(0).getFilePath();
            String fileName = file.get(0).getFileName();
            // 将数据更新为：毫秒值-[读取中]
            batchFiles.updateBatchFilesStatus(fileName,
                    String.valueOf(System.currentTimeMillis()), FileLogoConstant.READING);
            try{
                EntityBo entity = entityMap.get(fileType);
                // 读取excel中的指标数据
                String fullName = filePath + File.separator + fileName;
                List<Object> excelDataList = ExcelUtil.readExcel(new File(fullName), 0, 2,
                        entity.getEntitySetMethods(), entity.getEntityClass());
                // 批量新增或更新数据
                batchCreateOrUpdate(excelDataList, entity);
                // 更新数据读取状态为：2-成功
                batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_TWO, FileLogoConstant.SUCCESS);
            }catch (Exception e) {
                log.error("读取批量指标文件失败[" + e + "]");
                // 更新数据读取状态为：1-失败
                batchFiles.updateBatchFilesStatus(fileName, FileLogoConstant.STATUS_ONE,
                        FileLogoConstant.ERROR + ": " + e);
                throw e;
            }
        }
    }

    /**
     * 批量新增或更新数据
     * @param excelDataList
     * @param entity
     */
    private void batchCreateOrUpdate(List<Object> excelDataList, EntityBo entity) {
        if(excelDataList.size() <= 0) return;
        // 计算批量提交次数
        int commitTimes = excelDataList.size() / batchCommit;
        if(excelDataList.size() % batchCommit != 0) {
            commitTimes += 1;
        }
        // 每batchCommit个数据提交一次
        int startIndex = 0;
        for (int i = startIndex; i < commitTimes; i++) {
            int endIndex = startIndex + batchCommit;
            if(endIndex > excelDataList.size()) {
                endIndex = excelDataList.size();
            }
            // 批量新增或更新数据
            otherIndexMapper.saveOrUpdateBatch(excelDataList.subList(startIndex, endIndex), entity);
            startIndex = endIndex;
        }
    }


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        Class returnClass = FinAnalysisIndexDO.class;
//        Class returnClass = GrowthIndexDO.class;
//        Class returnClass = DupontAnalysisIndexDO.class;
//        Class returnClass = StockValueDO.class;
//        Class returnClass = TecAnalysisIndexDO.class;
//        Class returnClass = IndustryClassDO.class;
        Class returnClass = SecBasicIndexDO.class;


        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        Method[] invokeMethods = returnClass.getMethods();
        for(Method invokeMethod : invokeMethods) {
            if(invokeMethod.getName().startsWith("set")) {
                String fieldName = BaseUtil.lowerFirstCase(invokeMethod.getName().substring(3));
                String otherIndexDO = "#{otherIndexDO." + BaseUtil.lowerFirstCase(invokeMethod.getName().substring(3)) + "}";
                list1.add(fieldName);
                list2.add(otherIndexDO);
                list3.add(fieldName + "= values(" + fieldName + ")");
                list4.add(invokeMethod.getName());
            }
        }
        String[] s = list1.toArray(new String[list1.size()]);
        System.out.println(s[1]);
        System.out.println(list1);System.out.println(list2);System.out.println(list3);
        System.out.println(list4);

    }
}
