package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.response.WorkTimeResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;


public interface UserService {
    UserDto findUserByJwt(String jwt);

//    List<Users> getAllTimeLogByUserId(Long userID, LocalDate startDate, LocalDate endDate);
    Page<TimeLogsResponseDto> getAllTimeLogByUserId(Long userID, LocalDate startDate, LocalDate endDate, Integer page, Integer size);

    boolean checkInTime(String checkInCode, Long userId);
    boolean checkOutTime(String checkInCode, Long userId);

    Page<TimeLogs> getAllTimeLogFailByMonth(Long userID, LocalDate dateFilter,Integer page, Integer size);

    boolean clearCache();
//    @Query("SELECT tl FROM TimeLogs tl WHERE tl.users.id = :id AND tl.checkDate = :nowDate")
    TimeLogs getTimeLogByUserId(Long id, LocalDate nowDate);

    Page<WorkTimeResponseDto> getWorkTimeByUserId(Long userId ,Integer page, Integer size);
}
