package io.github.talelin.latticy.scheduler;


import io.github.talelin.latticy.scheduler.tasking.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Autowired
    private CoreIndexBack coreIndexBack;

//    @Async
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void readCoreIndexExcel() {
//        batchFiles.readFile();
//    }

//    @Async
//    @Scheduled(fixedRate=30000)
//    public void importCoreIndexData() {
//        try {
//            coreIndex.createOrUpdateCoreIndex();
//        }catch (Exception e) {
//
//        }
//    }

    @Async
    @Scheduled(cron = "0/6000 * * * * ?")
    public void importOtherIndexData() {
        try {
            otherIndex.createOrUpdateOtherIndex();
        }catch (Exception e) {

        }
    }

//    @Async
//    @Scheduled(cron = "0/2000 * * * * ?")
//    public void importListingData() {
//        listingIndex.createOrUpdateListingDateCal();
//    }

    @Async
    @Scheduled(cron = "0/300 * * * * ?")
    public void importCoreIndexBack() {
        try {
            coreIndexBack.createOrUpdateCoreIndexBack();
        }catch (Exception e) {

        }
    }
}
