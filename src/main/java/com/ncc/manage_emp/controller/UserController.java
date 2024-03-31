package com.ncc.manage_emp.controller;


import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AuthService;
import com.ncc.manage_emp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller // RETURN VIEW, HTML
@ResponseBody // RETURN JSON, XML
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AuthService authService;

    private UserService userService;

    @Autowired // Example Setter Injection
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/")
    public ResponseEntity<?> getAllTimeLog(@RequestParam Long userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate ){
//
        ResponseData responseData = new ResponseData();
//        dateFilter = dateFilter!=null ? dateFilter: LocalDate.now();
        try {
            responseData.setData(userService.getAllTimeLogByUserId(userId, startDate,endDate));
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
//
        ResponseData responseData = new ResponseData();

        try {
//            userService.checkInTime(CheckInCode,userId);
            
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
//
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





    @PostMapping("/checkin_error")
    public ResponseEntity<?> checkInError(@RequestParam Long userId, @RequestParam(required = false) LocalDate dateFilter ){
//
        ResponseData responseData = new ResponseData();
        dateFilter = dateFilter!=null ? dateFilter: LocalDate.now();
        try {
            System.out.println(dateFilter);
            responseData.setData(userService.getAllTimeLogFailByMonth(userId, dateFilter));
//            responseData.setData(timeLogRepository.findError());

            System.out.println(dateFilter + ">>>>>>>>>");
//            List<TimeLogs> a=timeLogRepository.findTimeLogsByCheckinType(1);
//            System.out.println(a);
//            responseData.setData(a);
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }









}
