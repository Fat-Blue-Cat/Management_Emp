package com.ncc.manage_emp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncc.manage_emp.dto.JWTAuthDto;

import com.ncc.manage_emp.dto.request.LoginDto;
import com.ncc.manage_emp.dto.request.SignupDto;
import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.MockData;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AuthService;
import com.ncc.manage_emp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // EXAMPLE CONSTRUCTOR-BASED INJECTION
    private final AuthService authService;

    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        ResponseData responseData = new ResponseData();
            JWTAuthDto jwtAuthDto = authService.login(loginDto);
            responseData.setData(jwtAuthDto);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupDto signupDto) throws Exception {
        ResponseData responseData = new ResponseData();
        try {
            JWTAuthDto jwtAuthDto = authService.signUp(signupDto);
            responseData.setData(jwtAuthDto);
            return new ResponseEntity<>(responseData,HttpStatus.OK);
        } catch (Exception e) {
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setStatus(400);
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "logoutsucess";
    }



    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/signingoogle")
    public ResponseEntity<?> login1(@AuthenticationPrincipal OAuth2User principal){
        ResponseData responseData = new ResponseData();

        try {
            JWTAuthDto jwtAuthDto = authService.loginWithGoogle(principal);
            responseData.setData(jwtAuthDto);
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    ResponseEntity<?> refreshToken(@RequestParam String token){
        ResponseData responseData = new ResponseData();
        try {
            JWTAuthDto jwtAuthDto = authService.refreshToken(token);
            responseData.setData(jwtAuthDto);
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }
        return new ResponseEntity<>(responseData,HttpStatus.OK);

    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUSerProfileHandler(
            @RequestHeader(value = "Authorization") String jwt
    )throws Exception{
        UserDto user = userService.findUserByJwt(jwt);
        return new ResponseEntity<UserDto>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/mockdata")
    public MockData getUserData() {
        try {
            // Đọc dữ liệu từ file JSON vào đối tượng UserData
            ObjectMapper objectMapper = new ObjectMapper();
            MockData mockData = objectMapper.readValue(new File("src/main/resources/data.json"), MockData.class);
            return mockData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/say-hello1")
    private ResponseEntity<?> getHello1(){
        String uri = "http://localhost:8080/api/auth/mockdata";
        RestTemplate restTemplate = new RestTemplate();
        MockData data = restTemplate.getForObject(uri,MockData.class);
//        String result = "Gello";
//        return data;
        return new ResponseEntity<>(data.getResult(),HttpStatus.OK);
    }

    @GetMapping("/say-hello")
    @ResponseBody
    private Flux<Object> getHello() {
        String uri = "http://localhost:8080/api/auth/mockdata";
        WebClient client = WebClient.create();

        return client.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Object.class);
    }

    @GetMapping("say-goodbye")
    public Flux<Object> getTweetsNonBlocking() {
//        log.info("Starting NON-BLOCKING Controller!");
        String uri = "https://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn";
        Flux<Object> tweetFlux = WebClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Object.class);


        return tweetFlux;
    }




}