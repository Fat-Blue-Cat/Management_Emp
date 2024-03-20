package com.ncc.manage_emp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> jobsMap;

    @Autowired
    public TaskSchedulingService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        this.jobsMap = new HashMap<>();
    }

//    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
//        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
//        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getDefault()));
////        jobsMap.put(jobId, scheduledTask);
//    }

    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);

        // Check if the job already exists in the jobsMap
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            // If the job exists, cancel the current task and reschedule it with the new cron expression
            scheduledTask.cancel(true);
        }

        scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getDefault()));
        jobsMap.put(jobId, scheduledTask);
    }

    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);
        }
    }

    public void cancelAllTasks() {
        for (ScheduledFuture<?> task : jobsMap.values()) {
            task.cancel(false); // Hủy tất cả các công việc đã lên lịch
        }
        jobsMap.clear(); // Xóa tất cả các công việc khỏi danh sách
    }




//    private final TaskScheduler taskScheduler;
//    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
//
//    public TaskSchedulingService(TaskScheduler taskScheduler) {
//        this.taskScheduler = taskScheduler;
//    }
//
//    public void schedule(String taskId, Runnable task, String cronExpression) {
//        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new CronTrigger(cronExpression));
//        scheduledTasks.put(taskId, scheduledTask);
//    }
//
//    public void cancel(String taskId) {
//        ScheduledFuture<?> task = scheduledTasks.get(taskId);
//        if (task != null) {
//            task.cancel(false);
//            scheduledTasks.remove(taskId);
//        }
//    }
}
