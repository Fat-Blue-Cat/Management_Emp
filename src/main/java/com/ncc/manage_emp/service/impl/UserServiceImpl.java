package com.ncc.manage_emp.service.impl;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.mapper.TimeLogMapperWithMapStruct;
import com.ncc.manage_emp.mapper.UserMapperWithMapstruct;
import com.ncc.manage_emp.mapper.WorkingTimeMapperWithMapstruct;
import com.ncc.manage_emp.repository.*;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.response.WorkTimeResponseDto;
import com.ncc.manage_emp.service.UserService;
import com.ncc.manage_emp.util.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@CacheConfig(cacheNames={"timeLogs"})
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapperWithMapstruct userMapperWithMapstruct;

    @Autowired
    private TimeLogMapperWithMapStruct timeLogMapperWithMapStruct;

    @Autowired
    private WorkingTimeMapperWithMapstruct workingTimeMapperWithMapstruct;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private WorkTimeRepository workTimeRepository;




    @Override
    public UserDto findUserByJwt(String jwt) {
        String userName = jwtTokenProvider.getUsername(jwt.substring(7));
//        ============== DEMO Spring Mapstruct ==========================
        Optional<Users> user = userRepository.findByUserNameOrEmail(userName,userName);
        UserDto userDto = userMapperWithMapstruct.userToUserDto(user.get());
//        Users users = userMapperWithMapstruct.userDtoToUser(userDto);
//        System.out.println(users);
        return userDto;
    }


    @Override
    @Cacheable( key = "#userID")
    public Page<TimeLogsResponseDto> getAllTimeLogByUserId(Long userID, LocalDate startDate, LocalDate endDate, Integer page, Integer size) {
        Sort sort = Sort.by("checkDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Users user = timeLogRepository.findAllTimeLogByUserId(userID, startDate, endDate);
        if (user != null) {
            List<TimeLogs> logsList = user.getTimeLogsList();
            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > logsList.size() ? logsList.size() : (start + pageable.getPageSize());
            List<TimeLogs> sublist = logsList.subList(start, end);
            Page<TimeLogs> logsPage = new PageImpl<>(sublist, pageable, logsList.size());
            List<TimeLogsResponseDto> timeLogsResponseDto = timeLogMapperWithMapStruct.listTimeLogToListTimeLogResponseDto(sublist);
            return new PageImpl<>(timeLogsResponseDto, pageable, logsList.size());
        }

        return null;
    }
    @Override
    @CachePut(key = "#userID")
    public Page<TimeLogs> getAllTimeLogFailByMonth(Long userID, LocalDate dateFilter, Integer page, Integer size) {
        Sort sort = Sort.by("checkDate").descending();
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        Page<TimeLogs> timeLogsList =  timeLogRepository.getTimeLogFail(userID,dateFilter,pageable);
        return new org.springframework.data.domain.PageImpl<>(timeLogsList.getContent(),pageable,timeLogsList.getTotalElements());
    }

    @CacheEvict(allEntries = true)
    public boolean clearCache() {

        return true;
    }


    @Override
    public boolean checkInTime(String checkInCode, Long userId) {
//        WorkTime workTime = workTimeRepository.findLastVersioning(userId);
        Optional<Users> users = userRepository.findById(userId);
        WorkTime workTime = workTimeRepository.findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(userId);
        TimeLogs timeLogCheck = timeLogRepository.findTimeLogsByCheckDate(userId,LocalDate.now());
//        if(timeLogCheck!=null) return false;
        if(users.get().getCheckinCode().equals(checkInCode)){
            if(timeLogCheck !=null) {
                if(timeLogCheck.getCheckinTime() != null){
//                    timeLogCheck.setCheckinTime(LocalTime.now());
//                    timeLogRepository.save(timeLogCheck);
                    return false;
                }else{
                    timeLogCheck.setCheckinTime(LocalTime.now());
                    timeLogRepository.save(timeLogCheck);
                    return true;
                }

            }
            else{
                TimeLogs timeLogs = new TimeLogs();
                LocalTime currenTime = LocalTime.now();

                if(currenTime.isAfter(workTime.getCheckinWork())) timeLogs.setCheckinType(false);
                else timeLogs.setCheckinType(true);
                timeLogs.setCheckinTime(currenTime);
                timeLogs.setCheckoutTime(null);
                timeLogs.setCheckDate(LocalDate.now());
                timeLogs.setUsers(users.get());
                timeLogRepository.save(timeLogs);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkOutTime(String checkOutCode, Long userId) {
//        WorkTime workTime = workTimeRepository.findLastVersioning(userId);
//        TimeLogs timeLogs = timeLogRepository.findTimeLogsByCheckDate(userId,LocalDate.now());

        Optional<Users> users = userRepository.findById(userId);
        WorkTime workTime = workTimeRepository.findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(userId);
        TimeLogs timeLogCheck = timeLogRepository.findTimeLogsByCheckDate(userId,LocalDate.now());

        if (timeLogCheck != null && checkOutCode.equals(users.get().getCheckinCode())) {
            timeLogCheck.setCheckoutTime(LocalTime.now());
            timeLogRepository.save(timeLogCheck);
            return true;
        }

//        if(workTime.getCheckinCode().equals(checkOutCode)){
//            TimeLogs timeLogs = new TimeLogs();
//            LocalTime currenTime = LocalTime.now();
//            timeLogs.setCheckinTime(currenTime);
//            timeLogs.setCheckoutTime(null);
//            timeLogs.setCheckDate(LocalDate.now());
//            timeLogs.setWorkTime(workTime);
//            return true;
//        }
        return false;
    }


    @Override
    public TimeLogs getTimeLogByUserId(Long id, LocalDate nowDate) {

        TimeLogs timeLogs = timeLogRepository.findTimeLogsByUsersIdAndCheckDate(id, nowDate);
        System.out.println(timeLogs);
        return timeLogs;
    }

    @Override
    public Page<WorkTimeResponseDto> getWorkTimeByUserId(Long userId, Integer page, Integer size) {

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<WorkTime> workTime = workTimeRepository.findWorkTimeByUsersId(userId, pageable);

        List<WorkTimeResponseDto> workTimeResponseDtoList = workingTimeMapperWithMapstruct.listWorkTimeToListWorkTimeResponseDto(workTime.getContent());
        return new PageImpl<>(workTimeResponseDtoList, pageable, workTime.getTotalElements());
    }


}
