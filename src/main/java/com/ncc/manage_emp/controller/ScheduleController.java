package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.request.SchedulerDto;
import com.ncc.manage_emp.service.AdminService;
import com.ncc.manage_emp.service.TaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private TaskSchedulingService taskSchedulingService;



    @Autowired
    private AdminService adminService;


    @PostMapping(path = "/taskdef", consumes = "application/json")
    public void scheduleATask(@RequestBody SchedulerDto schedulerDto) {

        System.out.println("Scheduling a task...");
        taskSchedulingService.scheduleATask(schedulerDto.getActionType(),
            taskSchedulingService.createRunnableForJob(schedulerDto.getActionType())
        , schedulerDto.getCronExpression());
    }

    @PostMapping(path = "/taskdef2", consumes = "application/json")
    public void scheduleATask2(@RequestBody SchedulerDto schedulerDto) {
        System.out.println("Scheduling a task...");
        taskSchedulingService.scheduleATask(schedulerDto.getActionType(), () -> {
//            System.out.println("Executing task at: " + new Date());
            System.out.println("HIHIHIH");
            // Your task logic goes here
        }, schedulerDto.getCronExpression());
    }

    @GetMapping("/remove/{jobId}")
    public void removeJob(@PathVariable String jobId) {
        taskSchedulingService.removeScheduledTask(jobId);
    }

    @GetMapping("/remove/all")
    public void removeJob() {
        taskSchedulingService.cancelAllTasks();
    }

    @PostMapping("/cron")
    public void updateDefaultCron(@RequestBody SchedulerDto schedulerDto) {
//        ScheduledFuture<?> scheduledTask = null;
//        scheduledTask.cancel(true);
//        scheduler.setDefaultCronExpression(schedulerDto.getCronExpression());
//        scheduler.fixedRateSch();
//        scheduler.cancelTask();
    }

}
