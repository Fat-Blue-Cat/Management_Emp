package com.ncc.manage_emp.service;

import com.ncc.manage_emp.dto.JWTAuthDto;
import com.ncc.manage_emp.dto.request.LoginDto;
import com.ncc.manage_emp.dto.request.SignupDto;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.repository.UserRepository;
import com.ncc.manage_emp.util.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    private CustomUserDetailsService customUserDetailsService;



    public AuthServiceImpl(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService customUserDetailsService
     ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public JWTAuthDto login(LoginDto loginDto) {


        Authentication authentication = authenticate(loginDto.getUsernameOrEmail(),loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(new HashMap<>(),authentication);
        JWTAuthDto jwtAuthDto = new JWTAuthDto();
        jwtAuthDto.setAccessToken(token);
        jwtAuthDto.setRefreshToken(refreshToken);
        return jwtAuthDto;
    }

    @Override
    public JWTAuthDto signUp(SignupDto signupDto) throws Exception {

        boolean isExistMail = userRepository.existsByEmail(signupDto.getEmail());
        boolean isExistUserName = userRepository.existsByUserName(signupDto.getUsername());

        if(isExistMail || isExistUserName) throw new Exception("Email or UserName is Already used with another account");

        Users user = new Users();
        user.setName(signupDto.getName());
        System.out.println(signupDto.getPassword());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setEmail(signupDto.getEmail());
        user.setRoleName("USER");
        user.setUserName(signupDto.getUsername());
        user.setCreateAt(new Date());

        Users savedUSer  = userRepository.save(user);

        Authentication authentication = authenticate(savedUSer.getUserName(),signupDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(new HashMap<>(),authentication);
        JWTAuthDto jwtAuthDto = new JWTAuthDto();
        jwtAuthDto.setAccessToken(token);
        jwtAuthDto.setRefreshToken(refreshToken);

        return jwtAuthDto;
    }

    @Override
    public JWTAuthDto loginWithGoogle(OAuth2User principal) {

        // Extract information from OAuth2 authentication token
        String email = (String) principal.getAttributes().get("email");
        String googleId = (String) principal.getAttributes().get("sub");
        String name = (String) principal.getAttributes().get("name");


        Optional<Users> userOptional = userRepository.findByUserNameOrEmail(email,email);
        Users user;
        if (userOptional.isPresent()) {
            user = userOptional.get();

        } else {
            user = new Users();
            user.setEmail(email);
            user.setGoogleId(googleId);
            user.setName(name);
            user.setRoleName("USER");
            user.setUserName(email);

            userRepository.save(user);
        }


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(new HashMap<>(),authentication);
        JWTAuthDto jwtAuthDto = new JWTAuthDto();
        jwtAuthDto.setAccessToken(token);
        jwtAuthDto.setRefreshToken(refreshToken);

        return jwtAuthDto;
    }


    public JWTAuthDto refreshToken(String refreshToken){
        JWTAuthDto jwtAuthDto = new JWTAuthDto();
        String userName = jwtTokenProvider.getUsername(refreshToken);
//        Optional<Users> user = userRepository.findByUserNameOrEmail(userName,userName);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        if(jwtTokenProvider.validateToken(refreshToken)  && authentication !=null){
            String jwt  = jwtTokenProvider.generateToken(authentication);

            jwtAuthDto.setRefreshToken(refreshToken);
            jwtAuthDto.setAccessToken(jwt);

        }
        return jwtAuthDto;
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null){

            throw new BadCredentialsException("Invalid Username");
        }

        if (!passwordEncoder.matches(password,userDetails.getPassword())){

            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }




}