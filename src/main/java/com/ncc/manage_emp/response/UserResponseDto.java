package com.ncc.manage_emp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.WorkTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String roleName;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<WorkTimeResponseDto> workTimeList;
////
//    private List<TimeLogsResponseDto> timeLogsList;
}
