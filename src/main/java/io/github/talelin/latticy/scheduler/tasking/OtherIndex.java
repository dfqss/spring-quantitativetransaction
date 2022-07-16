package io.github.talelin.latticy.scheduler.tasking;

import com.alibaba.fastjson.JSON;
import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.common.util.ExcelUtil;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.model.*;
import io.github.talelin.latticy.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * 上传其他文件类型
 */
@Component
@Slf4j
public class OtherIndex {

    @Autowired
    private MbaBatchFilesService mbaBatchFilesService;

    @Autowired
    private MbaFinAnalysisIndexService mbaFinAnalysisIndexService;

    @Autowired
    private MbaSecBasicIndexService mbaSecBasicIndexService;

    @Autowired
    private MbaGrowthIndexService mbaGrowthIndexService;

    @Autowired
    private MbaDupontAnalysisIndexService mbaDupontAnalysisIndexService;

    @Autowired
    private MbaStockValueService mbaStockValueService;

    @Autowired
    private MbaTecAnalysisIndexService mbaTecAnalysisIndexService;

    @Autowired
    private MbaIndustryClassService mbaIndustryClassService;


    public void createOrUpdateOtherIndex() {
        //1.初始化字段映射关系
        HashMap<String, Object> initMap = initMap();
        for (Map.Entry entry : initMap.entrySet()) {
            String entryKey = (String) entry.getKey();
            //2.查询需要更新的文件类型
            List<MbaBatchFilesDO> fileMessages = getFileNames(entryKey, "0");
            //3.没有查询到要读取的文件直接返回
            if (fileMessages.size() <= 0) {
                log.info("没有需要更新的excel文件:" + entryKey);
                continue;
            }
            //4.获取文件路径、文件名称
            String fileName = fileMessages.get(0).getFileName();
            String filePath = fileMessages.get(0).getFilePath();

            //5.将数据更新为：毫秒值-[读取中]
            String dateTime = new Date().getTime() + ""; //获取毫秒值
            mbaBatchFilesService.updateBatchFilesStatus(fileName, dateTime, "正在读取中");
            try {
                log.info("------start 开始计算excel文件: " + fileName);
                // 4.读取excel中上市日期的数据
                List<String> entryValue = (List<String>) entry.getValue();

                String absolutePath = filePath + File.separator + fileName;
                ArrayList<HashMap<String, String>> execlDate = ExcelUtil.readExcel(new File(absolutePath), entryValue, 0, 2);
                System.out.println("=====================================");
                System.out.println("=====================================");
                System.out.println(execlDate);
                log.info("start-----------【开始插入或更新表数据】");
                Boolean aBoolean = MapTOBean(entryKey, execlDate);
                log.info(absolutePath + "文件已经计算插入完成");
                log.info("end-----------【插入或更新表数据成功】");
                //6.将数据更新为：读取文件成功
                mbaBatchFilesService.updateBatchFilesStatus(fileName, "1", "成功");
            } catch (Exception e) {
                log.error(e.toString());
                mbaBatchFilesService.updateBatchFilesStatus(fileName, "2", "失败");
            }
        }
    }

    private Boolean MapTOBean(String entryKey, ArrayList<HashMap<String, String>> execlDate) {
        boolean resultFlag = false;
        switch (entryKey) {
            case "CWFXZB":
                List<MbaFinAnalysisIndexDO> mbaFinAnalysisIndexDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaFinAnalysisIndexDO mbaFinAnalysisIndexDO = JSON.parseObject(JSON.toJSONString(map), MbaFinAnalysisIndexDO.class);
                    mbaFinAnalysisIndexDO.setIsDeleted(0);
                    mbaFinAnalysisIndexDO.setCreateTime(new Date());
                    mbaFinAnalysisIndexDO.setUpdateTime(new Date());
                    mbaFinAnalysisIndexDOList.add(mbaFinAnalysisIndexDO);
                }
                resultFlag = mbaFinAnalysisIndexService.saveOrUpdateBatch(mbaFinAnalysisIndexDOList);
                return resultFlag;
            case "ZQJCZB":
                List<MbaSecBasicIndexDO> mbaSecBasicIndexDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaSecBasicIndexDO mbaSecBasicIndexDO = JSON.parseObject(JSON.toJSONString(map), MbaSecBasicIndexDO.class);
                    mbaSecBasicIndexDO.setIsDeleted(0);
                    mbaSecBasicIndexDO.setCreateTime(new Date());
                    mbaSecBasicIndexDO.setUpdateTime(new Date());
                    mbaSecBasicIndexDOList.add(mbaSecBasicIndexDO);
                }
                resultFlag = mbaSecBasicIndexService.saveOrUpdateBatch(mbaSecBasicIndexDOList);
                return resultFlag;
            case "CZZB":
                List<MbaGrowthIndexDO> mbaGrowthIndexDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaGrowthIndexDO mbaGrowthIndexDO = JSON.parseObject(JSON.toJSONString(map), MbaGrowthIndexDO.class);
                    mbaGrowthIndexDO.setIsDeleted(0);
                    mbaGrowthIndexDO.setCreateTime(new Date());
                    mbaGrowthIndexDO.setUpdateTime(new Date());
                    mbaGrowthIndexDOList.add(mbaGrowthIndexDO);
                }
                resultFlag = mbaGrowthIndexService.saveOrUpdateBatch(mbaGrowthIndexDOList);
                return resultFlag;
            case "DBFXZB":
                List<MbaDupontAnalysisIndexDO> mbaDupontAnalysisIndexDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaDupontAnalysisIndexDO mbaDupontAnalysisIndexDO = JSON.parseObject(JSON.toJSONString(map), MbaDupontAnalysisIndexDO.class);
                    mbaDupontAnalysisIndexDO.setIsDeleted(0);
                    mbaDupontAnalysisIndexDO.setCreateTime(new Date());
                    mbaDupontAnalysisIndexDO.setUpdateTime(new Date());
                    mbaDupontAnalysisIndexDOList.add(mbaDupontAnalysisIndexDO);
                }
                resultFlag = mbaDupontAnalysisIndexService.saveOrUpdateBatch(mbaDupontAnalysisIndexDOList);
                return resultFlag;
            case "GPGZ":
                List<MbaStockValueDO> mbaStockValueDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaStockValueDO mbaStockValueDO = JSON.parseObject(JSON.toJSONString(map), MbaStockValueDO.class);
                    mbaStockValueDO.setIsDeleted(0);
                    mbaStockValueDO.setCreateTime(new Date());
                    mbaStockValueDO.setUpdateTime(new Date());
                    mbaStockValueDOList.add(mbaStockValueDO);
                }
                resultFlag = mbaStockValueService.saveOrUpdateBatch(mbaStockValueDOList);
                return resultFlag;
            case "JSFXZB":
                List<MbaTecAnalysisIndexDO> mbaTecAnalysisIndexDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaTecAnalysisIndexDO mbaTecAnalysisIndexDO = JSON.parseObject(JSON.toJSONString(map), MbaTecAnalysisIndexDO.class);
                    mbaTecAnalysisIndexDO.setIsDeleted(0);
                    mbaTecAnalysisIndexDO.setCreateTime(new Date());
                    mbaTecAnalysisIndexDO.setUpdateTime(new Date());
                    mbaTecAnalysisIndexDOList.add(mbaTecAnalysisIndexDO);
                }
                resultFlag = mbaTecAnalysisIndexService.saveOrUpdateBatch(mbaTecAnalysisIndexDOList);
                return resultFlag;
            case "HYFL":
                List<MbaIndustryClassDO> mbaIndustryClassDOList = new ArrayList<>();
                //将 Map 转换为 实体类
                for (HashMap map : execlDate) {
                    MbaIndustryClassDO mbaIndustryClassDO = JSON.parseObject(JSON.toJSONString(map), MbaIndustryClassDO.class);
                    mbaIndustryClassDO.setIsDeleted(0);
                    mbaIndustryClassDO.setCreateTime(new Date());
                    mbaIndustryClassDO.setUpdateTime(new Date());
                    mbaIndustryClassDOList.add(mbaIndustryClassDO);
                }
                resultFlag = mbaIndustryClassService.saveOrUpdateBatch(mbaIndustryClassDOList);
                return resultFlag;
        }
        return null;
    }

    /**
     * 初始化字段映射关系
     */
    private HashMap<String, Object> initMap() {
        HashMap<String, Object> initMap = new HashMap<>();
        //财务分析指标
        ArrayList<String> CWFXZBList = new ArrayList<>();
        Collections.addAll(CWFXZBList, "code", "code_name", "roe_avg", "roe_basic", "roa", "gross_profit_margin", "net_profit_margin", "tot_ope_rev", "ope_rev", "goodwill", "r_and_d_costs", "segment_sales", "debt_to_assets", "cash_to_current_debt", "pe", "pb", "gr_ps", "or_ps", "cf_ps", "eps_basic", "bps");
        initMap.put(FileLogoConstant.CWFXZB, CWFXZBList);
        //成长指标
        ArrayList<String> CZZBList = new ArrayList<>();
        Collections.addAll(CZZBList, "code", "code_name", "yoy_eps_basic", "yoy_tr", "yoy_or", "yoy_op", "yoy_ebt", "yoy_profit", "yoy_equity", "fa_yoy", "yoy_debt", "yoy_assets_tb", "yoy_bps", "yoy_assets_hb", "growth_gr", "growth_gc", "growth_or", "growth_op", "qfa_cgr_sales", "qfa_cgr_op", "qfa_cgr_profit");
        initMap.put(FileLogoConstant.CZZB, CZZBList);
        //杜邦分析指标
        ArrayList<String> DBFXZBList = new ArrayList<>();
        Collections.addAll(DBFXZBList, "code", "code_name", "roe", "assets_turn", "dupont_np", "profit_to_gr", "dupont_tax_burden", "dupont_int_burden", "ebi_to_gr");
        initMap.put(FileLogoConstant.DBFXZB, DBFXZBList);
        //股票估值
        ArrayList<String> GPGZList = new ArrayList<>();
        Collections.addAll(GPGZList, "code", "code_name", "ev", "mkt_cap_float", "mkt_free_shares", "pb_lyr", "gr_ttm", "or_ttm", "profit_ttm", "eps_ttm", "or_ps_ttm");
        initMap.put(FileLogoConstant.GPGZ, GPGZList);
        //技术分析指标
        ArrayList<String> JSFXZBList = new ArrayList<>();
        Collections.addAll(JSFXZBList, "code", "code_name", "tech_bias5", "breakout_ma", "breakdown_ma", "bull_bear_ma");
        initMap.put(FileLogoConstant.JSFXZB, JSFXZBList);
        //行业分类
        ArrayList<String> HYFList = new ArrayList<>();
        Collections.addAll(HYFList, "code", "code_name", "industry_sw_code", "industry_sw", "industry_cit_code", "industry_cit");
        initMap.put(FileLogoConstant.HYFL, HYFList);
        //证券基础指标
        ArrayList<String> ZQJCZBList = new ArrayList<>();
        Collections.addAll(ZQJCZBList, "code", "code_name", "total_shares", "free_float_shares", "share_issuing_mkt", "rt_mkt_cap", "rt_float_mkt_cap");
        initMap.put(FileLogoConstant.ZQJCZB, ZQJCZBList);
        return initMap;
    }

    /**
     * 查询mba_batch_files表中需要写入的上市时间文件的路径和文件名
     */
    private List<MbaBatchFilesDO> getFileNames(String fileName, String status) {
        return mbaBatchFilesService.fileMessage(fileName, status);
    }

}
