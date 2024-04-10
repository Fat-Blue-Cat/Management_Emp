package com.ncc.manage_emp.service.impl;

import com.ncc.manage_emp.entity.Schedule;
import com.ncc.manage_emp.repository.ScheduleRepository;
import com.ncc.manage_emp.service.AdminService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private final ScheduleRepository scheduleRepository;
    private final Map<String, ScheduledFuture<?>> jobsMap;

    private final AdminService adminService;

    @Autowired
    public TaskSchedulingService(TaskScheduler taskScheduler, ScheduleRepository scheduleRepository, AdminService adminService) {
        this.taskScheduler = taskScheduler;
        this.scheduleRepository = scheduleRepository;
        this.jobsMap = new HashMap<>();
        this.adminService = adminService;
    }


    @PostConstruct
    public void initScheduledTasks() {
//         Load all scheduled tasks from the database and schedule them
        List<Schedule> allScheduledTasks = scheduleRepository.findAll();
        for (Schedule task : allScheduledTasks) {
            scheduleATask(task.getJobId(), createRunnableForJob(task.getJobId()), task.getCronExpression());
        }
    }




    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);

        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }

        scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getDefault()));
        jobsMap.put(jobId, scheduledTask);

        Schedule existingSchedule = scheduleRepository.findByJobId(jobId);
        if (existingSchedule !=null) {
            existingSchedule.setCronExpression(cronExpression);
            scheduleRepository.save(existingSchedule);
        } else {
            Schedule schedule = new Schedule();
            schedule.setJobId(jobId);
            schedule.setCronExpression(cronExpression);
            scheduleRepository.save(schedule);
        }
    }

    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);

            // Remove the scheduled task from the database
            Schedule scheduledTaskEntity = scheduleRepository.findByJobId(jobId);
            if (scheduledTaskEntity != null) {
                scheduleRepository.delete(scheduledTaskEntity);
            }
        }
    }

    public void cancelAllTasks() {
        for (ScheduledFuture<?> task : jobsMap.values()) {
            task.cancel(false); // Hủy tất cả các công việc đã lên lịch
        }
        jobsMap.clear(); // Xóa tất cả các công việc khỏi danh sách

        scheduleRepository.deleteAll();
    }

    public Runnable createRunnableForJob(String jobId) {


        if (jobId.equals("FORGET_CHECKIN")) {
            return () -> adminService.notifyForgetCheckIn();

        }
        if (jobId.equals("FORGET_CHECKOUT")) {
            return () -> adminService.notifyForgetCheckOut();
        }
        return null;
    }


}
