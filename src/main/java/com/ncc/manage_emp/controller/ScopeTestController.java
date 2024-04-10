package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.model_example.Customer;
import com.ncc.manage_emp.model_example.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScopeTestController {
    private final Customer customer;

    private final UserSession userSession;

    @GetMapping("/testlogin")
    public String login() {
        System.out.println(userSession);
        // Mô phỏng việc đăng nhập
        userSession.setUsername("example_user");
        userSession.setLoggedIn(true);
        return "Logged in successfully!";
    }

    @GetMapping("/testgetprofile")
    public String profile() {
        System.out.println(userSession);
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
        System.out.println(customer);
        return customer.getDataRequestScope().getName();
    }

    // UPDATE SESSION CURRENT
    @RequestMapping("/nameSSUpdated")
    public String helloSSUpdated() {
        customer.getDataSessionScope().setName("Session Scope Updated");
        System.out.println(customer);
        return customer.getDataSessionScope().getName();
    }

    // SCOPE SESSION WHEN REQUEST IS CALLED, BEAN IS CREATED
    @RequestMapping("/nameSS")
    public String helloSS() {
        System.out.println(customer);
        return customer.getDataSessionScope().getName();
    }
}
