//package com.ncc.manage_emp.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.support.CronTrigger;
//import java.util.concurrent.ScheduledFuture;
//
//@Configuration
//public class DynamicSchedulerService {
//
//    private final TaskScheduler scheduler = new ThreadPoolTaskScheduler();
//
//    // Schedule task with provided CronTrigger
//    public ScheduledFuture<?> scheduleTask(CronTrigger cronTrigger) {
//        return scheduler.schedule(new ScheduledTask(), cronTrigger);
//    }
//
//    // Get current Cron expression
//
//    @Value("${cron.expression}")
//    private String defaultCronExpression;
//
////    @Value("${cron.expression.default}")
////    private String defaultCronExpression;
//
//    // Get current Cron expression
//    public String getCronExpression() {
//        return defaultCronExpression;
//    }
////    public String getCronExpression() {
////        return defaultCronExpression; // Example default Cron expression
////    }
//
//    private static class ScheduledTask implements Runnable {
//        @Override
//        public void run() {
//            // Your scheduled task logic here
//            System.out.println("Dynamic scheduled task is running...");
//        }
//    }
//
//    @Bean
//    public TaskScheduler taskScheduler() {
//        return scheduler;
//    }
//}
