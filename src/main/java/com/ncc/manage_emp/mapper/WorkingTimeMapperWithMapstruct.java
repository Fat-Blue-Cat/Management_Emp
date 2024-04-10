package com.ncc.manage_emp.mapper;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.response.WorkTimeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkingTimeMapperWithMapstruct {
    WorkTimeDto userToUserDto(WorkTime workTime);


    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "userId", source = "users.id")
    WorkTimeResponseDto workTimeToWorkTimeResponseDto(WorkTime workTime);


    List<WorkTimeResponseDto> listWorkTimeToListWorkTimeResponseDto(List<WorkTime> workTimeList);

}
