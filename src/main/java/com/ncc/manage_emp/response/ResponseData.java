package com.ncc.manage_emp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseData {
    private int status=200;
    private boolean success = true;
    private String desc;
    private Object data;
}