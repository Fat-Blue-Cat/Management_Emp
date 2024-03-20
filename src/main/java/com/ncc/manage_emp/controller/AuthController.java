package com.ncc.manage_emp.controller;

import com.ncc.manage_emp.dto.JWTAuthDto;

import com.ncc.manage_emp.dto.request.LoginDto;
import com.ncc.manage_emp.dto.request.SignupDto;
import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.response.ResponseData;
import com.ncc.manage_emp.service.AuthService;
import com.ncc.manage_emp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        ResponseData responseData = new ResponseData();

        try {
//            String token = authService.login(loginDto);
            JWTAuthDto jwtAuthDto = authService.login(loginDto);
//            JWTAuthDto jwtAuthDto = new JWTAuthDto();
//            jwtAuthDto.setAccessToken(token);
            responseData.setData(jwtAuthDto);
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupDto signupDto) throws Exception {
        ResponseData responseData = new ResponseData();
        try {
            JWTAuthDto jwtAuthDto = authService.signUp(signupDto);
//            String refreshToken = authService.
//            JWTAuthDto jwtAuthDto = new JWTAuthDto();
//            jwtAuthDto.setAccessToken(token);
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



    @GetMapping("/sigingoogle1")
    public ResponseEntity<?> loginWithGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken){
        ResponseData responseData = new ResponseData();
        System.out.println(oAuth2AuthenticationToken);

        try {
//            String token = authService.loginWithGoogle(oAuth2AuthenticationToken);
//            JWTAuthDto jwtAuthDto = new JWTAuthDto();
//            jwtAuthDto.setAccessToken(token);
//            responseData.setData(token);
        }catch (Exception e){
            responseData.setStatus(400);
            responseData.setDesc(e.getMessage());
            responseData.setSuccess(false);
            responseData.setData(null);
        }

        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }



    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/signingoogle")
    public ResponseEntity<?> login1(@AuthenticationPrincipal OAuth2User principal){
        ResponseData responseData = new ResponseData();
//        System.out.println(oAuth2AuthenticationToken);

        try {
            JWTAuthDto jwtAuthDto = authService.loginWithGoogle(principal);
//            JWTAuthDto jwtAuthDto = new JWTAuthDto();
//            jwtAuthDto.setAccessToken(token);
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
        System.out.println(token);

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

    @GetMapping("/say-hello1")
    private List<Object> getHello1(){
        String uri = "https://jsonplaceholder.typicode.com/todos";
        RestTemplate restTemplate = new RestTemplate();
        Object[] data = restTemplate.getForObject(uri,Object[].class);
//        String result = "Gello";
//        return data;
        return Arrays.asList(data);
    }

    @GetMapping("/say-hello")
    @ResponseBody
    private Flux<Object> getHello() {
        String uri = "https://stg-api-hrmv2.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn";
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

//        tweetFlux.subscribe(tweet -> log.info(tweet.toString()));
//        log.info("Exiting NON-BLOCKING Controller!");
        return tweetFlux;
    }




}