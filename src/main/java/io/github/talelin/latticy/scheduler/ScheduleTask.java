package io.github.talelin.latticy.scheduler;


import io.github.talelin.latticy.scheduler.tasking.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 配置方法：
 *      @Scheduled(fixedRate=1000) -
 *          1秒执行一次，上次执行开始后过1秒执行下一次。若到了1秒后但上次执行还未完成，
 *          会加入worker队列，等待上一次执行完成后，马上执行下一次。
 *      @Scheduled(cron = "0/2 * * * * ?") -
 *          从0秒开始，每隔两秒执行一次
 */
@Configuration      // 1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync        // 3.开启多线程
public class ScheduleTask  {

    @Autowired
    private BatchFiles batchFiles;

    @Autowired
    private CoreIndex coreIndex;

    @Autowired
    private OtherIndex otherIndex;

    @Autowired
    private ListingIndex listingIndex;

//    @Autowired
//    private FinAnalysisIndexBack finAnalysisIndexBack;
//
//	@Autowired
//    private CoreIndexBack coreIndexBack;
//
//	@Autowired
//    private RangeRiseCommon rangeRiseCommon;

//    @Async
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void readCoreIndexExcel() {
//        batchFiles.readFile();
//    }

//    @Async
//    @Scheduled(cron = "0/60 * * * * ?")
//    public void importCoreIndexData() {
//        try {
//            coreIndex.createOrUpdateCoreIndex();
//        }catch (Exception e) {
//
//        }
//    }

//    @Async
//    @Scheduled(cron = "0/80 * * * * ?")
//    public void importOtherIndexData() {
//        try {
//            otherIndex.createOrUpdateOtherIndex();
//        }catch (Exception e) {
//
//        }
//    }

    @Async
    @Scheduled(cron = "0/20 * * * * ?")
    public void importListingData() {
        try {
            listingIndex.createOrUpdateListingDateCal();
        }catch (Exception e) {

        }
    }

    /**
     * 导入核心指标回测数据
     * 本地执行的代码，打包到生产环境的时候需要将代码注释调，防止空跑任务浪费cpu资源
     */
//    @Async
//    @Scheduled(cron = "0/20 * * * * ?")
//    public void importCoreIndexBack() throws Exception {
//        try {
//            coreIndexBack.createOrUpdateCoreIndexBack();
//        }catch (Exception e) {
//            throw e;
//        }
//    }

    /**
     * 导入财务分析指标回测数据
     * 本地执行的代码，打包到生产环境的时候需要将代码注释调，防止空跑任务浪费cpu资源
     */
//    @Async
//    @Scheduled(cron = "0/20 * * * * ?")
//    public void createOrUpdateFinAnalysisIndexBack() throws Exception {
//        try {
//            finAnalysisIndexBack.createOrUpdateFinAnalysisIndexBack();
//        }catch (Exception e) {
//            throw e;
//        }
//    }

    /**
     * 导入涨幅范围回测数据
     * 本地执行的代码，打包到生产环境的时候需要将代码注释调，防止空跑任务浪费cpu资源
     */
//    @Async
//    @Scheduled(cron = "0/25 * * * * ?")
//    public void createOrUpdateRangeRiseCommon() throws Exception {
//        try {
//            rangeRiseCommon.createOrUpdateRangeRiseCommon();
//        }catch (Exception e) {
//            throw e;
//        }
//    }
}
