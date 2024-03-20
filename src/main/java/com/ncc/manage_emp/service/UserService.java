package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface UserService {
    UserDto findUserByJwt(String jwt);

    List<WorkTime> getAllTimeLogByUserId(Long userID, LocalDateTime dateFilter);

    boolean checkInTime(String checkInCode, Long userId);
    boolean checkOutTime(String checkInCode, Long userId);

    List<WorkTime> getAllTimeLogFailByMonth(Long userID, LocalDate dateFilter);
}
