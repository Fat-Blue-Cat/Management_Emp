package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.modal_example.Customer;
import com.ncc.manage_emp.modal_example.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScopeTestController {
    @Autowired
    private Customer customer;

    @Autowired
    private UserSession userSession;

    @GetMapping("/testlogin")
    public String login() {
        // Mô phỏng việc đăng nhập
        userSession.setUsername("example_user");
        userSession.setLoggedIn(true);
        return "Logged in successfully!";
    }

    @GetMapping("/testgetprofile")
    public String profile() {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (userSession.isLoggedIn()) {
            return "Welcome, " + userSession.getUsername() + "!";
        } else {
            return "Please log in first!";
        }
    }

    // SCOPE REQUEST WHEN REQUEST IS CALLED, BEAN IS CREATED
    @RequestMapping("/nameRS")
    public String helloRS() {
        return customer.getDataRequestScope().getName();
    }

    // UPDATE SESSION CURRENT
    @RequestMapping("/nameSSUpdated")
    public String helloSSUpdated() {
        customer.getDataSessionScope().setName("Session Scope Updated");
        return customer.getDataSessionScope().getName();
    }

    // SCOPE SESSION WHEN REQUEST IS CALLED, BEAN IS CREATED
    @RequestMapping("/nameSS")
    public String helloSS() {
        return customer.getDataSessionScope().getName();
    }
}
