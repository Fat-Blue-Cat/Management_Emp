package com.ncc.manage_emp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SchedulerDto {
        private String cronExpression;
        private String actionType;

}
