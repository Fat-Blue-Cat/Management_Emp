package com.ncc.manage_emp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MockData {
    private List<Accounts> result;
    private String targetUrl;
    private boolean success;
    private String error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    // Getters and setters
}