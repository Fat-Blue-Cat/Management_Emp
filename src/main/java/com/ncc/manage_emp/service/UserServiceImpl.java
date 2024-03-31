package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.mapper.TimeLogMapperWithMapStruct;
import com.ncc.manage_emp.mapper.UserMapperWithMapstruct;
import com.ncc.manage_emp.repository.*;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.util.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapperWithMapstruct userMapperWithMapstruct;

    @Autowired
    private TimeLogMapperWithMapStruct timeLogMapperWithMapStruct;


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
    public  List<TimeLogsResponseDto> getAllTimeLogByUserId(Long userID, LocalDate startDate, LocalDate endDate) {
        Users user = timeLogRepository.findAllTimeLogByUserId(userID,startDate,endDate );
        List<TimeLogs> logsList = user.getTimeLogsList();
        List<TimeLogsResponseDto> timeLogsResponseDto = timeLogMapperWithMapStruct.timeLogToTimeLogResponseDto(logsList);
//        if(dateFilter == null) return timeLogRepository.findAllTimeLogByUserId(userID, LocalDate.now());
        return timeLogsResponseDto;
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
                }

            }
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
    public List<TimeLogs> getAllTimeLogFailByMonth(Long userID, LocalDate dateFilter) {
        return timeLogRepository.getTimeLogFail(userID,dateFilter);
    }
}
