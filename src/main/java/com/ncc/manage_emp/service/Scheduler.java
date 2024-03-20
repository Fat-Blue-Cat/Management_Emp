package com.ncc.manage_emp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {


    public void fixedRateSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Fixed Rate scheduler:: " + strDate);
    }

//    private static final String TASK_ID = "myScheduledTask";
//
//    @Autowired
//    private TaskSchedulingService taskManager;
//
//    @Scheduled(cron = "*/2 * * * * *")
//    public void fixedRateSch() {
//        System.out.println("Fixed Rate scheduler");
//    }
//
//    public void cancelTask() {
//        taskManager.cancel(TASK_ID);
//    }
}
