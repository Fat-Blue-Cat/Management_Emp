package com.ncc.manage_emp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncc.manage_emp.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeLogsResponseDto {

    private Long id;
    private LocalTime checkinTime;
    private boolean checkinType;
    private LocalTime checkoutTime;
    private LocalDate checkDate;


}
