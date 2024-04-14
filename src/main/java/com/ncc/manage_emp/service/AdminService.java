package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.Schedule;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.model_custom_results.CountErrorByMonth;
import com.ncc.manage_emp.model_custom_results.closed_projections.UserView;
import com.ncc.manage_emp.model_custom_results.open_projections.UserViewOpen;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.response.UserResponseDto;
import com.ncc.manage_emp.response.WorkTimeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    Page<UserResponseDto> getAllUser(int page, int size);


    void deleteUserbyId(Long id);

    boolean  updateRole(Long id,String roleName);


    UserResponseDto findUserById(Long id);


    Page<WorkTimeResponseDto> getAllWorkTimeUser(int page, int size, String name);

    void createWorkTime(WorkTimeDto workTimeDto) throws Exception;

    void deleteWorkTimeById(Long id) throws Exception;

    boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception;

    Page<UserResponseDto> findUserByName(String name, String sort,int page, int size);

     List<TimeLogs> createAllTimeLogToDay();

     Page<TimeLogsResponseDto> getAllTimeLogByCheckDate(LocalDate startDate, LocalDate endDate, Integer page, Integer size, String name);

    Slice<TimeLogsResponseDto> getAllTimeLogFailByMonth(LocalDate localDate, int page, int size, String typeCheck,String name);

//    List<Users> getAllTimeLogFailByMonth(LocalDate localDate);

    void notifyForgetCheckIn();
    void notifyForgetCheckOut();

    List<CountErrorByMonth> countErrorByMonth();

    UserView findUserByIdWithClosedProjections(Long id);

    UserViewOpen findUserByName(String name);

    List<Object> dynamicProjections(String name);

    boolean syncDataFromServer() throws Exception;

    List<Schedule> getAllScheduler();


}
