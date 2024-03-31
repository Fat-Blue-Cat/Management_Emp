package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.mapper.MapperWithModelMapper;
import com.ncc.manage_emp.mapper.UserMapperWithMapstruct;
import com.ncc.manage_emp.repository.TimeLogRepository;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.repository.WorkTimeRepository;
import com.ncc.manage_emp.response.UserResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    MapperWithModelMapper mapperWithModelMapper;

    @Autowired
    UserMapperWithMapstruct userMapperWithMapstruct;




    @Override
    public List<UserResponseDto> getAllUser() {
        List<Users> users=  userRepository.findAll();
        // ============= MODEL MAPPER LIST ==================
        List<UserResponseDto> userResponseDtos = mapperWithModelMapper.mapList(users,UserResponseDto.class);

        // ============= MAPSTRUCT MAPPER LIST ==================
//        List<UserResponseDto> userResponseDtos = users.stream().map(i -> userMapperWithMapstruct.userToUserResponseDto(i)).collect(Collectors.toList());

        return userResponseDtos;
    }

    @Override
    public void deleteUserbyId(Long id) {
        userRepository.deleteById(id);
    }


    public UserResponseDto findUserById(Long id) {
//        System.out.println(userRepository.getUserById(id).getTimeLogsList());
        Users users = userRepository.getUserById(id);
//        System.out.println(userRepository.getUserById(id).getTimeLogsList());
//        Optional<Users> users = userRepository.findById(id);

//        System.out.println(users);
//        Users users = entityManager.find(Users.class,id);
//        System.out.println(users.getTimeLogsList());
        UserResponseDto userResponseDto = mapperWithModelMapper.mapObject(users, UserResponseDto.class );

        return userResponseDto;
    }

    @Override
    @Transactional
    public boolean updateRole(Long id,String roleName) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setRoleName(roleName);
            if(user.getRoleName().equals("EMPLOYEE")){
                user.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));
            }
            userRepository.save(user);
            Optional<Users> users= userRepository.findById(id);
            WorkTime workTime = workTimeRepository.findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(id);
            if(users.get().getRoleName().equals("EMPLOYEE")){

                Context context = new Context();
                context.setVariable("work_time", workTime);
                context.setVariable("user_data", users);
                emailService.sendEmailWithHtmlTemplate(users.get().getEmail(), "Tạo tài khoản checkin cho nhân viên", "email-template", context);
            }


            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void createWorkTime(WorkTimeDto workTimeDto) throws Exception{
        try {
            WorkTime newWorkTime = new WorkTime();
            Optional<Users> users = userRepository.findById(workTimeDto.getUserId());
            if (!workTimeRepository.existsByUsersId(workTimeDto.getUserId())) {

                newWorkTime.setCheckoutWork(workTimeDto.getCheckOutWork());
                newWorkTime.setCheckinWork(workTimeDto.getCheckInWork());
                newWorkTime.setIsPrimaryWorking(true);
                newWorkTime.setCreateAt(new Date());

//                newWorkTime.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));

                Optional<Users> user = userRepository.findById(workTimeDto.getUserId());
                newWorkTime.setUsers(user.get());
                workTimeRepository.save(newWorkTime);

//                Context context = new Context();
//                context.setVariable("work_time", newWorkTime);
//                context.setVariable("user_data", users);
//                emailService.sendEmailWithHtmlTemplate(users.get().getEmail(), "Tạo tài khoản checkin cho nhân viên", "email-template", context);

            }else{
//                newWorkTime.setCheckoutWork(workTimeDto.getCheckOutWork());
//                newWorkTime.setCheckinWork(workTimeDto.getCheckInWork());
//                Long lastVer = workTimeRepository.findLastVersioning(workTimeDto.getUserId()).getVersion();
//                newWorkTime.setVersion(lastVer+1L);
//                newWorkTime.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));
//
//                Optional<Users> user = userRepository.findById(workTimeDto.getUserId());
//                newWorkTime.setUsers(user.get());
//                workTimeRepository.save(newWorkTime);

                newWorkTime.setCheckoutWork(workTimeDto.getCheckOutWork());
                newWorkTime.setCheckinWork(workTimeDto.getCheckInWork());
                newWorkTime.setIsPrimaryWorking(false);
                newWorkTime.setCreateAt(new Date());

//                newWorkTime.setCheckinCode(String.valueOf(new Random().nextInt(9000) + 1000));

                Optional<Users> user = userRepository.findById(workTimeDto.getUserId());
                newWorkTime.setUsers(user.get());
                workTimeRepository.save(newWorkTime);
//
//                Context context = new Context();
//                context.setVariable("work_time", newWorkTime);
//                context.setVariable("user_data", users);
//                emailService.sendEmailWithHtmlTemplate(users.get().getEmail(), "Tạo tài khoản checkin cho nhân viên", "email-template", context);

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
        workTimeRepository.deleteById(id);}


    }

    @Override
    @Transactional
    public boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception {
        System.out.println(workTimeDto);
            Optional<WorkTime> workTime =workTimeRepository.findById(id);
            if (workTime.isPresent()) {
                workTime.get().setCheckoutWork(workTimeDto.getCheckOutWork());
                workTime.get().setCheckinWork(workTimeDto.getCheckInWork());
                if(workTimeDto.getIsPrimaryWorking() == true){
                    List<WorkTime> workTimeList = workTimeRepository.findAllByUsersId(workTimeDto.getUserId());
                    for (WorkTime w: workTimeList){
                        if(w.getId() != workTime.get().getId() && w.getIsPrimaryWorking() == true){
                            w.setIsPrimaryWorking(false);
                            workTimeRepository.save(w);
                        }
                    }
                }
                workTime.get().setIsPrimaryWorking(workTimeDto.getIsPrimaryWorking());
                workTimeRepository.save(workTime.get());
                return true;
            }
            return false;

    }

    @Override
    public List<Users> findUserByName(String name, String sort) {
        if (sort==null) sort="increase";
        if(sort.equals("increase") ) return userRepository.findByName(name, Sort.by("name").ascending());
        else      return userRepository.findByName(name, Sort.by("name").descending());

    }

    @Transactional
    public List<TimeLogs> createAllTimeLogToDay(){
        List<Long> workTimeIdList = timeLogRepository.getAllWorkTimeNotExistToDay(LocalDate.now());
        System.out.println(workTimeIdList);
        List<TimeLogs> timeLogsList = new ArrayList<>();

        for (Long id : workTimeIdList){

            TimeLogs timeLogs = new TimeLogs();
            timeLogs.setCheckinTime(null);
            timeLogs.setCheckoutTime(null);
            timeLogs.setCheckDate(LocalDate.now());
            timeLogs.setCheckinType(false);

            Users users =new Users();
            users.setId(id);
            timeLogs.setUsers(users);
            timeLogsList.add(timeLogs);
        }

        timeLogRepository.saveAll(timeLogsList);

        return timeLogsList;

    }

    @Override
    public List<Users> getAllTimeLogByCheckDate(LocalDate startDate, LocalDate endDate) {
//
        List<Users> usersList= userRepository.getAllUserByTimeCheckin(startDate,endDate);

        return usersList;
    }


    @Override
    public List<Users> getAllTimeLogFailByMonth(LocalDate localDate) {
//
        List<Users> usersList= userRepository.getAllTimeLogFailAllUser(localDate);

        return usersList;
    }

    @Override
    public void notifyForgetCheckIn() {
        List <Users> usersList = userRepository.getAllUserForgetCheckIn(LocalDate.now());
        System.out.println("Don't forget Checkin");
        for (Users user : usersList){
            Context context = new Context();
            String type = "CheckIn";
            context.setVariable("user_data", user);
            context.setVariable("type_mail",type);
            emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Đừng quên checkin hôm nay<3", "email-template-notify", context);

        }

    }

    @Override
    public void notifyForgetCheckOut() {
        List <Users> usersList = userRepository.getAllUserForgetCheckout(LocalDate.now());
        for (Users user : usersList){
            Context context = new Context();
            String type = "Check Out";
            context.setVariable("user_data", user);
            context.setVariable("type_mail",type);
            emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Don't forget check out today!", "email-template-notify", context);

        }
    }


}
