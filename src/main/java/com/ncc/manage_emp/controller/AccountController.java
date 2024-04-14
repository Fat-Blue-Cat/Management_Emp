package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAccount(@RequestParam Integer page, @RequestParam Integer size, String email) {
        ResponseData responseData = new ResponseData();
        try {
            responseData.setData(accountService.getAllAccount(page, size, email));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setDesc("Get all account failed!");
        }
        return new ResponseEntity<>(responseData,  HttpStatus.OK);

    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllAccount() {
        ResponseData responseData = new ResponseData();
        try {
            accountService.deleteAllAccount();
            responseData.setDesc("Delete all account success!");
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setDesc("Delete all account failed!");
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
