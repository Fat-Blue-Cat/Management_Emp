package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;

import java.util.List;

public interface AdminService {

    List<Users> getAllUser();

    void deleteUserbyId(Long id);

    boolean  updateRole(Long id,String roleName);

    boolean createWorkTime(WorkTimeDto workTimeDto) throws Exception;

    void deleteWorkTimeById(Long id) throws Exception;

    boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception;

    List<Users> findUserByName(String name, String sort);

     List<TimeLogs> createAllTimeLogToDay();
}
