package com.ncc.manage_emp.controller;


import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller // RETURN VIEW, HTML
@ResponseBody // RETURN JSON, XML
//@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    // EXAMPLE SETTER-BASED INJECTION
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllTimeLog(@RequestParam Long userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, @RequestParam Integer page, @RequestParam Integer size ){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.getAllTimeLogByUserId(userId, startDate,endDate, page, size));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/checkin")
    public ResponseEntity<?> checkInReq(@RequestParam String CheckInCode,@RequestParam Long userId ){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.checkInTime(CheckInCode,userId));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public ResponseEntity<?> checkOutReq(@RequestParam String checkOutCode,@RequestParam Long userId ){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.checkOutTime(checkOutCode,userId));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/checkin_error")
    public ResponseEntity<?> checkInError(@RequestParam Long userId, @RequestParam(required = false) LocalDate dateFilter, Integer page, Integer size){
        ResponseData responseData = new ResponseData();
        dateFilter = dateFilter!=null ? dateFilter: LocalDate.now();
        try {
            System.out.println(dateFilter);
            responseData.setData(userService.getAllTimeLogFailByMonth(userId, dateFilter,page,size));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/timelog/{id}")
    public  ResponseEntity<?> getTimeLogCurrentDateByUserId(@PathVariable Long id){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.getTimeLogByUserId(id, LocalDate.now()));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/timelog/clear-cache")
    public ResponseEntity<?> clearCache(){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.clearCache());
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/worktime/{id}")
    public  ResponseEntity<?> getWorkTimeCurrentDateByUserId(@PathVariable Long id, @RequestParam Integer page,@RequestParam Integer size){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(userService.getWorkTimeByUserId(id,page,size));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }





}
