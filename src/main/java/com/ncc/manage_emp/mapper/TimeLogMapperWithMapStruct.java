package com.ncc.manage_emp.mapper;

import com.ncc.manage_emp.dto.TimeLogDto;
import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeLogMapperWithMapStruct {

    @Mapping(target = "checkinTime", source = "timeLogs.checkinTime")
    @Mapping(target = "checkinType", source = "timeLogs.checkinType")
    @Mapping(target = "checkoutTime", source = "timeLogs.checkoutTime")
    @Mapping(target = "checkDate1", source = "timeLogs.checkDate")
    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "userId", source = "users.id")
    TimeLogsResponseDto timeLogToTimeLogResponseDto(TimeLogs timeLogs);
    List<TimeLogsResponseDto> listTimeLogToListTimeLogResponseDto(List<TimeLogs> timeLogs);


    @Mapping(target = "checkinTime", source = "timeLogsResponseDto.checkinTime")
    @Mapping(target = "checkinType", source = "timeLogsResponseDto.checkinType")
    @Mapping(target = "checkoutTime", source = "timeLogsResponseDto.checkoutTime")
    @Mapping(target = "checkDate", source = "timeLogsResponseDto.checkDate1")
    TimeLogs timeLogResponseDtoToTimeLog(TimeLogsResponseDto timeLogsResponseDto);

    @Mapping(target = "checkinTime", source = "timeLogDto.checkinTime",dateFormat="dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "checkinType", source = "timeLogDto.checkinType")
    @Mapping(target = "checkoutTime", source = "timeLogDto.checkoutTime",dateFormat="dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "checkDate", source = "timeLogDto.checkDate", dateFormat="dd-MM-yyyy HH:mm:ss")
    TimeLogs timeLogToTimeLogDto(TimeLogDto timeLogDto);



}
