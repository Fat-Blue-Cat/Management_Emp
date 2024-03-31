package com.ncc.manage_emp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.WorkTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

@Data

public class UserResponseDto {

    private Long id;

    private String name;

    private String email;

    private String roleName;

    private List<WorkTimeResponseDto> workTimeList;
//
    private List<TimeLogsResponseDto> timeLogsList;
}
