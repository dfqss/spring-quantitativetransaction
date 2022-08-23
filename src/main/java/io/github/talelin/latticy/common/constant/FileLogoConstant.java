package io.github.talelin.latticy.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件标识常量
 * @author pedro@TaleLin
 */
public class FileLogoConstant {

    /**
     * 文件后缀
     */
    public static final String SUFFIX_OK = ".ok"; // .ok文件后缀

    public static final String SUFFIX_READING = ".reading"; // .reading文件后缀

    public static final String SUFFIX_FAIL = ".fail"; // .fail文件后缀

    public static final String SUFFIX_SUCCESS = ".success"; // .success文件后缀

    public static final String SUFFIX_XLSX = ".xlsx"; // .xlsx文件后缀

    /**
     * 文件类型
     */
    public static final String CWFXZB = "CWFXZB"; // 财务分析指标：CWFXZB

    public static final String CZZB = "CZZB"; // 成长指标：CZZB

    public static final String DBFXZB = "DBFXZB"; // 杜邦分析指标：DBFXZB

    public static final String GPGZ = "GPGZ"; // 股票估值：GPGZ

    public static final String JSFXZB = "JSFXZB"; // 技术分析指标：JSFXZB

    public static final String HYFL = "HYFL"; // 行业分类：HYFL

    public static final String ZQJCZB = "ZQJCZB"; // 证券基础指标：ZQJCZB

    public static final String SSRQ = "SSRQ"; // 上市日期：SSRQ

    public static final String HXZB = "HXZB"; // 核心指标：HXZB

    public static final String HC_HXZB = "HC_HXZB"; //回测核心指标

    public static final String HC_JZCSYL = "HC_JZCSYL"; //回测财务指标：HC_JZCSYL

    /**
     * 文件读取状态 0-未读 1-读取失败 2-读取成功
     */
    public static final String STATUS_ZERO = "0"; // 未读

    public static final String STATUS_ONE = "1"; // 读取失败

    public static final String STATUS_TWO = "2"; // 读取成功

    /**
     * 文件读取状态描述
     */
    public static final String ERROR = "ERROR"; // 读取失败

    public static final String SUCCESS = "SUCCESS"; // 读取成功

    public static final String READING = "READING"; // 读取中

    /**
     * 核心指标文件类型
     */
    public static final String REC8 = "REC8"; // 重算文件


    /**
     * 展示状态 0-展示  1-不展示
     */
    public static final String SHOW_STATUS_ZERO = "0"; // 展示

    public static final String SHOW_STATUS_ONE = "1"; // 不展示

    /**
     * 策略ID
     */
    public static final String S_100000 = "100000"; // 核心策略

    public static final String S_100001 = "100001"; // 连续减少策略

    /**
     * 策略类型
     */
    public static final String S_CORE = "coreStrategy"; // 核心策略

    public static final String S_CONTINUE_REDUCTION = "continueReductionStrategy"; // 连续减少策略

    /**
     * 时间格式
     */
    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 前缀
     */
    public static final String PREFIX_SET = "set";

    /**
     * 投资指标表名
     */
    public static final String MBA_FIN_ANALYSIS_INDEX = "mba_fin_analysis_index";

    public static final String MBA_GROWTH_INDEX = "mba_growth_index";

    public static final String MBA_DUPONT_ANALYSIS_INDEX = "mba_dupont_analysis_index";

    public static final String MBA_STOCK_VALUE = "mba_stock_value";

    public static final String MBA_TEC_ANALYSIS_INDEX = "mba_tec_analysis_index";

    public static final String MBA_INDUSTRY_CLASS = "mba_industry_class";

    public static final String MBA_SEC_BASIC_INDEX = "mba_sec_basic_index";

    public static final String MBA_FIN_ANALYSIS_INDEX_BACK = "mba_fin_analysis_index_back";

    public static final String MBA_RANGE_RISE_COMMON = "mba_range_rise_common";

    /**
     * 投资指标的所有set方法（按excel的读取顺序排序）
     */
    public static String[] FIN_ANALYSIS_INDEX_ARR = { // 财务分析指标
            "setCode", "setCodeName", "setRoeAvg", "setRoeBasic", "setRoa",
            "setGrossProfitMargin", "setNetProfitMargin", "setTotOpeRev", "setOpeRev", "setGoodwill",
            "setRAndDCosts", "setSegmentSales", "setDebtToAssets", "setCashToCurrentDebt", "setPe",
            "setPb", "setGrPs", "setOrPs", "setCfPs", "setEpsBasic", "setBps"
    };

    public static String[] GROWTH_INDEX_ARR = { // 成长指标
            "setCode", "setCodeName", "setYoyEpsBasic", "setYoyTr", "setYoyOr",
            "setYoyOp", "setYoyEbt", "setYoyProfit", "setYoyEquity", "setFaYoy",
            "setYoyDebt", "setYoyAssetsTb", "setYoyBps", "setYoyAssetsHb", "setGrowthGr",
            "setGrowthGc", "setGrowthOr", "setGrowthOp", "setQfaCgrSales", "setQfaCgrOp", "setQfaCgrProfit"
    };

    public static String[] DUPONT_ANALYSIS_INDEX_ARR = { // 杜邦分析指标
            "setCode", "setCodeName", "setRoe", "setAssetsTurn", "setDupontNp",
            "setProfitToGr", "setDupontTaxBurden", "setDupontIntBurden", "setEbiToGr"
    };

    public static String[] STOCK_VALUE_ARR = { // 股票估值
            "setCode", "setCodeName", "setEv", "setMktCapFloat", "setMktFreeShares",
            "setPbMrq", "setPbLyr", "setGrTtm", "setOrTtm", "setProfitTtm", "setEpsTtm", "setOrPsTtm"
    };

    public static String[] TEC_ANALYSIS_INDEX_ARR = { // 技术分析指标
            "setCode", "setCodeName", "setTechBias5", "setBreakoutMa", "setBreakdownMa",
            "setBullBearMa", "setLON", "setBuying"
    };

    public static String[] INDUSTRY_CLASS_ARR = { // 行业分类
            "setCode", "setCodeName", "setIndustrySw", "setIndustrySwCode", "setIndustryCit", "setIndustryCitCode"
    };

    public static String[] SEC_BASIC_INDEX_ARR = { // 证券基础指标
            "setCode", "setCodeName", "setTotalShares", "setFreeFloatShares", "setShareIssuingMkt",
            "setRtMktCap", "setRtFloatMktCap"
    };

    public static String[] FIN_ANALYSIS_INDEX_BACK_ARR = { // 回测财务分析指标
            "setCode", "setCodeName", "setRoeAvg"
    };
}
