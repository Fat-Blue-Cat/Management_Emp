package com.ncc.manage_emp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncc.manage_emp.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

//@Data
@Getter
@Setter
@Builder
public class TimeLogsResponseDto {
    private String name;
    private Long userId;
    private Long id;
    private LocalTime checkinTime;
    private boolean checkinType;
    private LocalTime checkoutTime;
    private LocalDate checkDate1;
//    private UserResponseDto userResponseDto;


}
