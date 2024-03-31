package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.response.UserResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<UserResponseDto> getAllUser();

    void deleteUserbyId(Long id);

    boolean  updateRole(Long id,String roleName);


    UserResponseDto findUserById(Long id);

    void createWorkTime(WorkTimeDto workTimeDto) throws Exception;

    void deleteWorkTimeById(Long id) throws Exception;

    boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception;

    List<Users> findUserByName(String name, String sort);

     List<TimeLogs> createAllTimeLogToDay();

     List<Users> getAllTimeLogByCheckDate(LocalDate startDate, LocalDate endDate);

    List<Users> getAllTimeLogFailByMonth(LocalDate localDate);

    void notifyForgetCheckIn();
    void notifyForgetCheckOut();
}
