package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AdminService;
import com.ncc.manage_emp.service.EmailService;
import com.ncc.manage_emp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final EmailService emailService;
    private final UserService userService;


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


            responseData.setData( adminService.createAllTimeLogToDay());

        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/getAllByTimeWeek")
    public ResponseEntity<?> getAllUserToDayCheckInOrCheckOut(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate){
        System.out.println(startDate);
        System.out.println(endDate);
        ResponseData responseData = new ResponseData();

        try {
            responseData.setData(adminService.getAllTimeLogByCheckDate(startDate,endDate));

        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getAllTimeLogFailByMoth")
    public ResponseEntity<?> getAllUserFailCheckInByMonth(@RequestParam LocalDate checkDate){
        ResponseData responseData = new ResponseData();
        System.out.println(checkDate +">>>>>>>>>");

        try {
//            responseData.setData(userRepository.getAllUserByTimeCheckin(checkDate));
            responseData.setData(adminService.getAllTimeLogFailByMonth(checkDate));

        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/notify-all-forget-checkin")
    public ResponseEntity<?> getAllUserForgetCheckIn() throws Exception {
        ResponseData responseData = new ResponseData();
        try{
            adminService.notifyForgetCheckIn();
            responseData.setData("All member received mail!");
        }catch (Exception e){
            throw new Exception("Lỗi gửi mail");
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/notify-all-forget-checkout")
    public ResponseEntity<?> getAllUserForgetCheckOut() throws Exception {
        ResponseData responseData = new ResponseData();
        try{
            adminService.notifyForgetCheckOut();
            responseData.setData("All member received mail!");
        }catch (Exception e){
            throw new Exception("Lỗi gửi mail");
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setData(adminService.findUserById(id));
//        try{
//            adminService.notifyForgetCheckOut();
//            responseData.setData("All member received mail!");
//        }catch (Exception e){
//            throw new Exception("Lỗi gửi mail");
//        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }




}
