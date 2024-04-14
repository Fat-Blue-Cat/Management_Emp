package com.ncc.manage_emp.service.impl;

import com.ncc.manage_emp.dto.TimeLogDto;
import com.ncc.manage_emp.dto.request.WorkTimeDto;
import com.ncc.manage_emp.entity.*;
import com.ncc.manage_emp.event.UpdateRoleEvent;
import com.ncc.manage_emp.mapper.MapperWithModelMapper;
import com.ncc.manage_emp.mapper.TimeLogMapperWithMapStruct;
import com.ncc.manage_emp.mapper.UserMapperWithMapstruct;
import com.ncc.manage_emp.mapper.WorkingTimeMapperWithMapstruct;
import com.ncc.manage_emp.model_custom_results.CountErrorByMonth;
import com.ncc.manage_emp.model_custom_results.closed_projections.UserView;
import com.ncc.manage_emp.model_custom_results.open_projections.UserResponseDtoRecord;
import com.ncc.manage_emp.model_custom_results.open_projections.UserViewOpen;
import com.ncc.manage_emp.repository.*;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.response.UserResponseDto;
import com.ncc.manage_emp.response.WorkTimeResponseDto;
import com.ncc.manage_emp.service.AdminService;
import com.ncc.manage_emp.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
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


    @Autowired // GET LANGUAGE FORM PROPERTY
    MessageSource messageSource;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    WorkingTimeMapperWithMapstruct workingTimeMapperWithMapstruct;

    @Autowired
    TimeLogMapperWithMapStruct timeLogMapperWithMapStruct;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScheduleRepository scheduleRepository;



    @Override
    public Page<UserResponseDto> getAllUser(int page, int size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Users> usersPage = userRepository.findAll(pageable);

        // ============= MODEL MAPPER LIST ==================
//        List<UserResponseDto> userResponseDtos = mapperWithModelMapper.mapList(usersPage.getContent(), UserResponseDto.class);

        // ============= MAPSTRUCT MAPPER LIST ==================
         List<UserResponseDto> userResponseDtos = usersPage.getContent().stream().map(i -> userMapperWithMapstruct.userToUserResponseDto(i)).collect(Collectors.toList());

        return new PageImpl<>(userResponseDtos, pageable, usersPage.getTotalElements());
    }


    @Override
    public void deleteUserbyId(Long id) {
        userRepository.deleteById(id);
    }


    public UserResponseDto findUserById(Long id) {
        Users users = userRepository.getUserById(id);
        UserResponseDto userResponseDtos = mapperWithModelMapper.mapObject(users, UserResponseDto.class);
        return userResponseDtos;
    }

    @Override
    public Page<WorkTimeResponseDto> getAllWorkTimeUser(int page, int size, String name) {

        Sort sort = Sort.by("users.name");
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<WorkTime> workTimePage = workTimeRepository.findAll(pageable,name);

        // ============= MODEL MAPPER LIST ==================
//        List<UserResponseDto> userResponseDtos = mapperWithModelMapper.mapList(usersPage.getContent(), UserResponseDto.class);

        // ============= MAPSTRUCT MAPPER LIST ==================
        List<WorkTimeResponseDto> timeLogsResponseDtoList = workingTimeMapperWithMapstruct.listWorkTimeToListWorkTimeResponseDto(workTimePage.getContent());

        return new PageImpl<>(timeLogsResponseDtoList, pageable, workTimePage.getTotalElements());
    }


    /**
     * ACTION UPDATE ROLE
     */
    @Override
    @Transactional
    public boolean updateRole(Long id,String roleName) {
        System.out.println("UPDATE: CLASS UPDATE START");
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
                // DETACH EVENT UPDATE ROLE = EMP
                // source (Nguồn phát ra) chính là class này
                System.out.println("UPDATE: THONG BAO  -> MAIL UPDATE DA THUC THI");
                applicationEventPublisher.publishEvent(new UpdateRoleEvent(this, users.get(),workTime));

                System.out.println("UPDATE: KET THUC UPDATE");
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
    @Transactional
    public void deleteWorkTimeById(Long id) throws Exception {
        if(!workTimeRepository.existsById(id)){
            throw  new Exception("Time làm việc này không tồn tại trong db!");
        }
        workTimeRepository.deleteWorkTimeById(id);


    }

    @Override
    @Transactional
    public boolean updateWorkTime(Long id,WorkTimeDto workTimeDto) throws Exception {
        System.out.println(workTimeDto);
            Optional<WorkTime> workTime =workTimeRepository.findById(id);
            if (workTime.isPresent()) {
                if(workTime.get().getIsPrimaryWorking()==true && workTimeDto.getIsPrimaryWorking()==false) return false;
                workTime.get().setCheckoutWork(workTimeDto.getCheckOutWork());
                workTime.get().setCheckinWork(workTimeDto.getCheckInWork());
                if(workTimeDto.getIsPrimaryWorking()){
                    List<WorkTime> workTimeList = workTimeRepository.findAllByUsersId(workTimeDto.getUserId());
                    for (WorkTime w: workTimeList){
                        if(!Objects.equals(w.getId(), workTime.get().getId()) && w.getIsPrimaryWorking()){
                            System.out.println("w");
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
    public Page<UserResponseDto> findUserByName(String name, String sort, int page, int size) {
        // Default sort direction if not provided
        Sort.Direction direction = Sort.Direction.ASC;
        if (sort != null && sort.equalsIgnoreCase("decrease")) {
            direction = Sort.Direction.DESC;
        }

        // Sorting by name
        Sort sortByName = Sort.by(direction, "name");

        Pageable pageable = PageRequest.of(page, size, sortByName);
        Page<Users> usersPage = userRepository.findByName(name, pageable);
        List<UserResponseDto> userResponseDtoList = mapperWithModelMapper.mapList(usersPage.getContent(),UserResponseDto.class);

        return new PageImpl<>(userResponseDtoList, pageable, usersPage.getTotalElements());
//        return usersPage.map(user -> mapperWithModelMapper.mapObject(user, UserResponseDto.class));
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
    public Page<TimeLogsResponseDto> getAllTimeLogByCheckDate(LocalDate startDate, LocalDate endDate, Integer page,Integer size,String name) {


        // Sorting by name
        Sort sortByName = Sort.by("id");

        Pageable pageable = PageRequest.of(page, size, sortByName);
//
        Page<TimeLogs> usersList= userRepository.getAllUserByTimeCheckin(startDate,endDate,pageable,name);

        List<TimeLogsResponseDto> timeLogsResponseDtoList = timeLogMapperWithMapStruct.listTimeLogToListTimeLogResponseDto(usersList.getContent());

        return new PageImpl<>(timeLogsResponseDtoList,pageable,usersList.getTotalElements());
    }


//    @Override
//    public List<Users> getAllTimeLogFailByMonth(LocalDate localDate) {
////
//        List<Users> usersList= userRepository.getAllTimeLogFailAllUser(localDate);
//
//        return usersList;
//    }

    @Override
    public Slice<TimeLogsResponseDto> getAllTimeLogFailByMonth(LocalDate localDate, int page, int size, String typeCheck,String name) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<TimeLogs> timeLogsList = userRepository.getAllTimeLogFailAllUser(localDate, typeCheck,name);
        List<TimeLogsResponseDto> timeLogsResponseDtoList = timeLogMapperWithMapStruct.listTimeLogToListTimeLogResponseDto(timeLogsList);
        int start = (int) pageRequest.getOffset();
        int end = (start + pageRequest.getPageSize()) > timeLogsList.size() ? timeLogsList.size() : (start + pageRequest.getPageSize());

        List<TimeLogsResponseDto> content = timeLogsResponseDtoList.subList(start, end);

        return new SliceImpl<>(content, pageRequest, timeLogsResponseDtoList.size() > end);
    }


//    ======================= DEMO MULTIPLE LANGUAGE =======
    @Override
    public void notifyForgetCheckIn() {
        List<TimeLogs> timeLogsList = createAllTimeLogToDay();
        List<Users> usersList = userRepository.getAllUserForgetCheckIn(LocalDate.now());
        System.out.println("Don't forget Checkin");
        // ===========  MULTIPLE LANGUAGE ==================
        Locale locale_vi =new Locale("vi", "VN");
        Locale locale_en =Locale.ENGLISH;
        Locale locale = locale_vi;

        String message1 = messageSource.getMessage("message1", null, locale);
        String message2 = messageSource.getMessage("message2", null, locale);
        String message3 = messageSource.getMessage("message3", null,locale);

        for (Users user : usersList) {
            Context context = new Context();
            String type = "CheckIn";
            context.setVariable("user_data", user);
            context.setVariable("type_mail", type);
            context.setVariable("message1", message1);
            context.setVariable("message2", message2);
            context.setVariable("message3", message3);
            emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Don't forget check in today!", "email-template-notify", context);
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


    /*============ DEMO CUSTOM RESULT WITH CONSTRUCTION ================== */
    @Override
    public List<CountErrorByMonth> countErrorByMonth() {
        return timeLogRepository.countTimeLogsErrorByMonth();
    }

    /*============ DEMO CUSTOM RESULT WITH CLOSED CONSTRUCTIONS ================== */

    @Override
    public UserView findUserByIdWithClosedProjections(Long id) {
        return userRepository.findUsersById(id);
    }

    /*============ DEMO CUSTOM RESULT WITH OPEN CONSTRUCTIONS ================== */
    @Override
    public UserViewOpen findUserByName(String name) {
        return userRepository.findUsersByName(name);

    }

    @Override
    public List<Object> dynamicProjections(String name) {
        List<Object> list = new ArrayList<>();
        Users users = userRepository.findUsersByName(name, Users.class);
        UserViewOpen userViewOpen = userRepository.findUsersByName(name, UserViewOpen.class);
        UserResponseDtoRecord userResponseDtoRecord = userRepository.findUsersByName(name,UserResponseDtoRecord.class);
        list.add(users);
        list.add(userViewOpen);
        list.add(userResponseDtoRecord);
        return list;
    }

    @Override
    @Transactional
    public boolean syncDataFromServer() throws Exception {
        String uri = "http://localhost:8080/api/auth/mockdata";
        RestTemplate restTemplate = new RestTemplate();
        MockData data = restTemplate.getForObject(uri,MockData.class);
        if(data != null){
            List<Accounts> accountsList = data.getResult();
            for (Accounts account : accountsList){
                String newEmail = 1 + account.getEmail();
                if (!accountRepository.existsByEmail(newEmail)) {
                    account.setEmail(newEmail);
                    accountRepository.save(account);
                }
            }
            System.out.println("LƯU THÀNH CÔNG");
//            throw new RuntimeException("Error");

            return true;
        }
        return false;
    }

    @Override
    public List<Schedule> getAllScheduler() {
        return scheduleRepository.findAll();
    }




}
