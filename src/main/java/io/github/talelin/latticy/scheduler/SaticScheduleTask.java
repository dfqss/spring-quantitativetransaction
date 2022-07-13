package io.github.talelin.latticy.scheduler;


import io.github.talelin.latticy.configuration.ApplicatinConfiguration;
import io.github.talelin.latticy.scheduler.tasking.BatchFiles;
import io.github.talelin.latticy.scheduler.tasking.ListingIndex;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync        // 3.开启多线程
public class SaticScheduleTask  {

    @Autowired
    private ApplicatinConfiguration contextConfig;

    @Autowired
    private BatchFiles batchFiles;

    @Autowired
    private ListingIndex listingIndex;

    //4.添加定时任务
    @Async //异步执行
    @Scheduled(cron = "0/60 * * * * ?")//或直接指定时间间隔，例如：5秒 @Scheduled(fixedRate=5000)
    public void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        batchFiles.readFile();
    }


    @Async
    @Scheduled(cron = "0/20 * * * * ?")
    public void importListingData() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        listingIndex.createOrUpdateListingDateCal();
    }


}
