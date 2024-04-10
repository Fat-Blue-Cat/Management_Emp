package com.ncc.manage_emp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.response.TimeLogsResponseDto;
import com.ncc.manage_emp.service.AuthService;
import com.ncc.manage_emp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;



    private Users user;
    private UserDto userDto;
    private WorkTime workTime;
    private TimeLogs timeLog;

    private TimeLogsResponseDto timeLogsResponseDto;

    private List<TimeLogsResponseDto> listTimeLogs = new ArrayList<>();

    private List<TimeLogs> listTimeLogError = new ArrayList<>();

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
        listTimeLogs.add(timeLogsResponseDto);
        listTimeLogError.add(timeLog);

    }




    @Test
    public void testGetAllTimeLog() throws Exception {

        Page<TimeLogsResponseDto> page = new PageImpl<>(listTimeLogs);
        when(userService.getAllTimeLogByUserId(1L, null,null,0,3)).thenReturn(page);

        // First call to getAllTimeLogByUserId, result should be cached
        mockMvc.perform(get("/api/user/")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data.content[0].id").value(timeLogsResponseDto.getId()));
        // Rest of your test code...
    }



    @Test
    public void testCheckInReq() throws Exception {
        when(userService.checkInTime("valid_code", 1L)).thenReturn(true);

        mockMvc.perform(get("/api/user/checkin")
                        .param("CheckInCode", "valid_code")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testCheckOutReq() throws Exception {
        when(userService.checkOutTime("valid_code", 1L)).thenReturn(true);

        mockMvc.perform(get("/api/user/checkout")
                        .param("checkOutCode", "valid_code")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testCheckInError() throws Exception {
        Page<TimeLogs> page = new PageImpl<>(listTimeLogError);
        when(userService.getAllTimeLogFailByMonth(1L, LocalDate.now(),0,3)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/checkin_error")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data.content[0].id").value(timeLogsResponseDto.getId()));
    }

    @Test
    public void testGetTimeLogCurrentDateByUserId() throws Exception {
        when(userService.getTimeLogByUserId(1L, LocalDate.now())).thenReturn(timeLog);

        mockMvc.perform(get("/api/user/timelog/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data.id").value(timeLog.getId()));
    }
}