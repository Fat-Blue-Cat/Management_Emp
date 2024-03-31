package com.ncc.manage_emp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncc.manage_emp.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalTime;
import java.util.Date;

@Data

public class WorkTimeResponseDto {

    private Long id;

//    private Boolean checkinType;

    private LocalTime checkinWork;

    private LocalTime checkoutWork;

    private Boolean isPrimaryWorking;

    private Date createAt;
//    private UserResponseDto users;
}
