package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.request.SchedulerDto;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AdminService;
import com.ncc.manage_emp.service.impl.TaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    //EXAMPLE FIELD-BASED INJECTION
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


    @GetMapping("/")
    public ResponseEntity<?> schedule() {
        ResponseData responseData = new ResponseData();
        try{
            responseData.setData(adminService.getAllScheduler());
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
