package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.repository.TimeLogRepository;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.repository.WorkTimeRepository;
import com.ncc.manage_emp.util.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImpl implements UserService{


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

        Optional<Users> user = userRepository.findByUserNameOrEmail(userName,userName);

        UserDto userDto = new UserDto();
        userDto.setUserName(user.get().getUserName());
        userDto.setRole(user.get().getRoleName());
        userDto.setEmail(user.get().getEmail());
        return userDto;
    }

    @Override
    public List<WorkTime> getAllTimeLogByUserId(Long userID, LocalDateTime dateFilter) {

        if(dateFilter == null) return timeLogRepository.findAllTimeLogByUserId(userID, LocalDateTime.now());
        return timeLogRepository.findAllTimeLogByUserId(userID,dateFilter );
    }

    @Override
    public boolean checkInTime(String checkInCode, Long userId) {
        WorkTime workTime = workTimeRepository.findLastVersioning(userId);
        TimeLogs timeLogCheck = timeLogRepository.findTimeLogsByCheckDate(userId,LocalDate.now());
//        if(timeLogCheck!=null) return false;
        if(workTime.getCheckinCode().equals(checkInCode)){
            if(timeLogCheck !=null) {
                timeLogCheck.setCheckinTime(LocalTime.now());
                timeLogRepository.save(timeLogCheck);
                return true;
            }
            TimeLogs timeLogs = new TimeLogs();
            LocalTime currenTime = LocalTime.now();

            if(currenTime.isAfter(workTime.getCheckinWork())) timeLogs.setCheckinType(false);
            else timeLogs.setCheckinType(true);
            timeLogs.setCheckinTime(currenTime);
            timeLogs.setCheckoutTime(null);
            timeLogs.setCheckDate(LocalDate.now());
            timeLogs.setWorkTime(workTime);
            timeLogRepository.save(timeLogs);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkOutTime(String checkOutCode, Long userId) {
        WorkTime workTime = workTimeRepository.findLastVersioning(userId);
        TimeLogs timeLogs = timeLogRepository.findTimeLogsByCheckDate(userId,LocalDate.now());

        if (timeLogs != null && checkOutCode.equals(workTime.getCheckinCode())) {
            timeLogs.setCheckoutTime(LocalTime.now());
            timeLogRepository.save(timeLogs);
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
    public List<WorkTime> getAllTimeLogFailByMonth(Long userID, LocalDate dateFilter) {
        return timeLogRepository.getTimeLogFail(userID,dateFilter);
    }
}
