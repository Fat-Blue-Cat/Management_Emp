package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.repository.TimeLogRepository;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.repository.WorkTimeRepository;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AdminService;
import com.ncc.manage_emp.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    AdminService adminService;

    WorkTimeRepository workTimeRepository;

    TimeLogRepository timeLogRepository;

    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser(){
        ResponseData responseData = new ResponseData();

        try {

            responseData.setData(adminService.getAllUser());
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        ResponseData responseData = new ResponseData();

        try {
            adminService.deleteUserbyId(id);
            responseData.setData("Xóa thành công");
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoleUser(@PathVariable Long id, @RequestParam String roleName){
        ResponseData responseData = new ResponseData();

        try {
            adminService.updateRole(id, roleName);

            responseData.setData("Sửa quyền thành công");
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> createWorkTime(@RequestBody WorkTimeDto workTimeDto){
        ResponseData responseData = new ResponseData();

        try {
                adminService.createWorkTime(workTimeDto);
                responseData.setData("Cấp tài khoản thành công!!");


        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/worktime/update/{id}")
    public ResponseEntity<?> createWorkTime(@PathVariable Long id,@RequestBody WorkTimeDto workTimeDto){
        ResponseData responseData = new ResponseData();

        try {
            if(adminService.updateWorkTime(id,workTimeDto)){
                responseData.setData("Sửa thông tin làm việc của tài khoản thành công!!");
            }else{
                responseData.setData("Sửa thông tin làm việc của tài khoản thất bại!!");
            }

        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @DeleteMapping("/worktime/{id}")
    public ResponseEntity<?> deleteWorkTimeById(@PathVariable Long id){
        ResponseData responseData = new ResponseData();

        try {

            adminService.deleteWorkTimeById(id);
//            workTimeRepository.deleteById(id);
            responseData.setData("Xóa thành công");
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/findByName")
    public ResponseEntity<?> findUserByName(@RequestParam String name,@RequestParam(required = false) String sort){
        ResponseData responseData = new ResponseData();

        try {

            responseData.setData(adminService.findUserByName(name,sort));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/create-all-timelogs-today")
    public ResponseEntity<?> createAllTimeLogToDay(){
        ResponseData responseData = new ResponseData();

        try {


//            responseData.setData(workTimeRepository.findMaxVersionForEachUser());
            responseData.setData( adminService.createAllTimeLogToDay());
//            responseData.setData( timeLogRepository.getAllWorkTimeNotExistToDay(LocalDate.now()));

        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/getAll1")
    public ResponseEntity<?> getAllUserToDayCheckInOrCheckOut(){
        ResponseData responseData = new ResponseData();

        try {

//            responseData.setData(userRepository.getAllUserByTimeCheckin());
            responseData.setData(userRepository.getAllUserByTimeCheckin(LocalDate.now()));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }



}
