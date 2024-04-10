package com.ncc.manage_emp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeDto {
    private LocalTime checkInWork;
    private LocalTime checkOutWork;
    private Boolean isPrimaryWorking;
    private Long userId;
}