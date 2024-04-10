package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    // DEMO CONSTRUCTOR INJECTION , USE FINAl TO MAKE IT IMMUTABLE BECAUSE SPRING WILL NOT CHANGE IT
    private final AdminService adminService;


    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(adminService.getAllUser(0,2));
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
            responseData.setData("Delete success");
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoleUser(@PathVariable Long id, @RequestParam String roleName) throws Exception{
        ResponseData responseData = new ResponseData();

        try {
            adminService.updateRole(id, roleName);
            responseData.setData("Change role success");
        }catch (Exception e){
            throw new  Exception("Change role fail");
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/worktime")
    public ResponseEntity<?> getAllWorkTime(@RequestParam Integer page, @RequestParam Integer size, String name){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(adminService.getAllWorkTimeUser(page, size,name));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/worktime")
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
    public ResponseEntity<?> updateWorkTime(@PathVariable Long id,@RequestBody WorkTimeDto workTimeDto){
        ResponseData responseData = new ResponseData();
        try {
            if(adminService.updateWorkTime(id,workTimeDto)){
                responseData.setData("Change success!!");
            }else{
              throw new Exception("Change fail");
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
    public ResponseEntity<?> findUserByName(@RequestParam String name,@RequestParam(required = false) String sort,@RequestParam Integer page, @RequestParam Integer size){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(adminService.findUserByName(name,sort,page,size));
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
    public ResponseEntity<?> getAllUserToDayCheckInOrCheckOut(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate, @RequestParam Integer page, @RequestParam Integer size, @RequestParam String name){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(adminService.getAllTimeLogByCheckDate(startDate,endDate,page, size,name));
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getAllTimeLogFailByMoth")
    public ResponseEntity<?> getAllUserFailCheckInByMonth(@RequestParam LocalDate checkDate, @RequestParam(defaultValue = "0") Integer pageNo,  @RequestParam(defaultValue = "2") Integer pageSize, @RequestParam(defaultValue = "ALL") String typeCheck, @RequestParam String name){
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(adminService.getAllTimeLogFailByMonth(checkDate,pageNo,pageSize,typeCheck,name));
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
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /* DEMO CUSTOM RESULTS WITH CONSTRUCTION */
    @GetMapping("/count-error-by-month")
    public ResponseEntity<?> countErrorByMonth() throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setData(adminService.countErrorByMonth());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /* DEMO CUSTOM RESULT WITH CLOSED PROJECTIONS */
    @GetMapping("/find-user-by-id-closed-projection/{id}")
    public ResponseEntity<?> findUserByIdWithClosedProject(@PathVariable Long id) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setData(adminService.  findUserByIdWithClosedProjections(id));

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /* DEMO CUSTOM RESULT WITH OPEN PROJECTIONS */
    @GetMapping("/find-user-by-id-open-projection/{name}")
    public ResponseEntity<?>  findUserByNameWithOpenProject(@PathVariable String name) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setData(adminService.findUserByName(name));

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /*DYNAMIC OPEN PROJECTIONS*/
    @GetMapping("/find-user-by-id-dynamic-open-projection/{name}")
    public ResponseEntity<?>  findUserByNameWithDynamicOpenProject(@PathVariable String name) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setData(adminService.dynamicProjections(name));

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @GetMapping("/account")
    private ResponseEntity<?> SyncData() throws Exception {
        ResponseData responseData = new ResponseData();
        if(adminService.syncDataFromServer()){
                responseData.setData("Đồng bộ dữ liệu thành công");
        }else{
            responseData.setData("Đồng bộ dữ liệu thất bại");
        }
//        responseData.setData(adminService.syncDataFromServer());
//        try {
//            if(adminService.syncDataFromServer()){
//                responseData.setData("Đồng bộ dữ liệu thành công");
//            }else{
//                responseData.setData("Đồng bộ dữ liệu thất bại");
//            }
//        }catch (Exception e){
//            responseData.setStatus(400);
//            responseData.setDesc(e.getMessage());
//            responseData.setSuccess(false);
//            responseData.setData(null);
//        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }






}
