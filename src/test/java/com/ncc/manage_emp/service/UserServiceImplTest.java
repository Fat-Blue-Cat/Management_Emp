package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.TimeLogDto;
import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.mapper.TimeLogMapperWithMapStruct;
import com.ncc.manage_emp.mapper.UserMapperWithMapstruct;
import com.ncc.manage_emp.repository.TimeLogRepository;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.repository.WorkTimeRepository;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.service.impl.UserServiceImpl;
import com.ncc.manage_emp.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserMapperWithMapstruct userMapperWithMapstruct;

    @Mock
    private TimeLogMapperWithMapStruct timeLogMapperWithMapStruct;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimeLogRepository timeLogRepository;

    @Mock
    private WorkTimeRepository workTimeRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Users user;
    private UserDto userDto;
    private WorkTime workTime;
    private TimeLogs timeLog;

    private TimeLogsResponseDto timeLogsResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        initEntitiesAndDTOs();
    }

    public void initEntitiesAndDTOs() {
        user = Users.builder().userName("testUser").id(1l).checkinCode("valid_code").build();
        userDto = UserDto.builder().id(1l).userName("testUser").build();
        timeLog = TimeLogs.builder().id(1l).users(user).checkDate(LocalDate.now()).build();
        workTime = WorkTime.builder().id(1l).users(user).isPrimaryWorking(true).build();
        timeLogsResponseDto = TimeLogsResponseDto.builder().id(1l).name("testUser").checkDate1(LocalDate.now()).userId(user.getId()).build();


    }

    @Test
    void findUserByJwt_ValidJwt_ReturnsUserDto() {
        // Arrange
        String jwt = "valid_jwt";
        when(jwtTokenProvider.getUsername(any())).thenReturn("testUser");
        when(userRepository.findByUserNameOrEmail("testUser", "testUser")).thenReturn(Optional.of(user));
        when(userMapperWithMapstruct.userToUserDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.findUserByJwt(jwt);

        // Assert
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getUserName(), result.getUserName());
    }


    @Test
    void checkInTime_ValidCheckInCodeAndUserId_ReturnsTrue() {
        // Arrange
        String checkInCode = "valid_code";
        Long userId = 1L;
//
//        WorkTime workTime = new WorkTime();
//        TimeLogs timeLogCheck = null;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(workTimeRepository.findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(userId)).thenReturn(workTime);
        when(timeLogRepository.findTimeLogsByCheckDate(userId, LocalDate.now())).thenReturn(timeLog);

        // Act
        boolean result = userService.checkInTime(checkInCode, userId);

        // Assert
        assertTrue(result);
    }

    // Test cho checkOutTime()
    @Test
    void checkOutTime_ValidCheckOutCodeAndUserId_ReturnsTrue() {
        // Arrange
        String checkOutCode = "valid_code";
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(workTimeRepository.findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(userId)).thenReturn(workTime);
        when(timeLogRepository.findTimeLogsByCheckDate(userId, LocalDate.now())).thenReturn(timeLog);

        // Act
        boolean result = userService.checkOutTime(checkOutCode, userId);

        // Assert
        assertTrue(result);
    }

    // Test cho getAllTimeLogFailByMonth()
    @Test
    void getAllTimeLogByUserId_ValidInput_ReturnsTimeLogsList() {
        // Arrange
        Long userId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        int page = 0;
        int size = 2;

        List<TimeLogs> timeLogsList = new ArrayList<>();
        timeLogsList.add(timeLog);
        timeLogsList.add(timeLog);

        Users user = new Users();
        user.setTimeLogsList(timeLogsList);

        when(timeLogRepository.findAllTimeLogByUserId(userId, startDate, endDate)).thenReturn(user);
        when(timeLogMapperWithMapStruct.listTimeLogToListTimeLogResponseDto(anyList())).thenReturn(Arrays.asList(timeLogsResponseDto, timeLogsResponseDto));

        // Act
        Page<TimeLogsResponseDto> result = userService.getAllTimeLogByUserId(userId, startDate, endDate, page, size);

        // Assert
        assertEquals(2, result.getContent().size());
    }

    // Test cho getTimeLogByUserId()
    @Test
    void getTimeLogByUserId_ValidInput_ReturnsTimeLogs() {
        // Arrange
        Long userId = 1L;
        LocalDate nowDate = LocalDate.now();

        when(timeLogRepository.findTimeLogsByUsersIdAndCheckDate(userId, nowDate)).thenReturn(timeLog);

        // Act
        TimeLogs result = userService.getTimeLogByUserId(userId, nowDate);

        // Assert
        assertNotNull(result);
    }

    // Add more test cases for other methods as needed
}
