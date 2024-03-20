package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.repository.TimeLogRepository;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.repository.WorkTimeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkTimeRepository workTimeRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TimeLogRepository timeLogRepository;

    @Override
    public List<Users> getAllUser() {
        List<Users> users=  userRepository.findAll();
        return users;
    }

    @Override
    public void deleteUserbyId(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean updateRole(Long id,String roleName) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setRoleName(roleName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean createWorkTime(WorkTimeDto workTimeDto) throws Exception{
        try {
            WorkTime newWorkTime = new WorkTime();
            Optional<Users> users = userRepository.findById(workTimeDto.getUserId());
            if (!workTimeRepository.existsByUsersId(workTimeDto.getUserId())) {

                newWorkTime.setCheckoutWork(workTimeDto.getCheckOutWork());
                newWorkTime.setCheckinWork(workTimeDto.getCheckInWork());
                newWorkTime.setVersion(0L);
                newWorkTime.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));

                Optional<Users> user = userRepository.findById(workTimeDto.getUserId());
                newWorkTime.setUsers(user.get());
                workTimeRepository.save(newWorkTime);

                Context context = new Context();
                context.setVariable("work_time", newWorkTime);
                context.setVariable("user_data", users);
                emailService.sendEmailWithHtmlTemplate(users.get().getEmail(), "Tạo tài khoản checkin cho nhân viên", "email-template", context);

                System.out.println("SAVE DONE");
                return true;
            }else{
                newWorkTime.setCheckoutWork(workTimeDto.getCheckOutWork());
                newWorkTime.setCheckinWork(workTimeDto.getCheckInWork());
                Long lastVer = workTimeRepository.findLastVersioning(workTimeDto.getUserId()).getVersion();
                newWorkTime.setVersion(lastVer+1L);
                newWorkTime.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));

                Optional<Users> user = userRepository.findById(workTimeDto.getUserId());
                newWorkTime.setUsers(user.get());
                workTimeRepository.save(newWorkTime);

                Context context = new Context();
                context.setVariable("work_time", newWorkTime);
                context.setVariable("user_data", users);
                emailService.sendEmailWithHtmlTemplate(users.get().getEmail(), "Tạo tài khoản checkin cho nhân viên", "email-template", context);

                System.out.println("SAVE DONE");
                return true;
            }
        } catch (Exception e) {

            throw new Exception(e.getMessage());

        }
    }

    @Override
    public void deleteWorkTimeById(Long id) throws Exception {
        if(!workTimeRepository.existsById(id)){
            throw  new Exception("Time làm việc này không tồn tại trong db!");
        }else{
        System.out.println(id + "IIIIIIIIII");
        workTimeRepository.deleteById(id);}


    }

    @Override
    @Transactional
    public boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception {
            Optional<WorkTime> workTime =workTimeRepository.findById(id);
            if (workTime.isPresent()) {
                workTime.get().setCheckoutWork(workTimeDto.getCheckOutWork());
                workTime.get().setCheckinWork(workTimeDto.getCheckInWork());
                workTimeRepository.save(workTime.get());
                return true;
            }
            return false;

    }

    @Override
    public List<Users> findUserByName(String name, String sort) {

        if(sort.equals("increase") ) return userRepository.findByName(name, Sort.by("name").ascending());
        else      return userRepository.findByName(name, Sort.by("name").descending());

    }

    @Transactional
    public List<TimeLogs> createAllTimeLogToDay(){
//        List<Object[]> userList = workTimeRepository.findMaxVersionForEachUser();
        List<Long> workTimeIdList = timeLogRepository.getAllWorkTimeNotExistToDay(LocalDate.now());
        List<TimeLogs> timeLogsList = new ArrayList<>();

        for (Long id : workTimeIdList){

            TimeLogs timeLogs = new TimeLogs();
            timeLogs.setCheckinTime(null);
            timeLogs.setCheckoutTime(null);
            timeLogs.setCheckDate(LocalDate.now());
            timeLogs.setCheckinType(false);

            WorkTime workTime = new WorkTime();
            workTime.setId(id);
            timeLogs.setWorkTime(workTime);

            timeLogsList.add(timeLogs);
        }

        timeLogRepository.saveAll(timeLogsList);


//       .

        return timeLogsList;

    }


}
