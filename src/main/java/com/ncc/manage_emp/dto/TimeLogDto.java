package com.ncc.manage_emp.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TimeLogDto {

    private Long id;
    private String checkinTime;
    private boolean checkinType;
    private String checkoutTime;
    private String checkDate;

}
