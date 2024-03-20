package com.ncc.manage_emp.service;


import com.ncc.manage_emp.dto.JWTAuthDto;
import com.ncc.manage_emp.dto.request.LoginDto;
import com.ncc.manage_emp.dto.request.SignupDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    JWTAuthDto login(LoginDto loginDto);
    JWTAuthDto signUp(SignupDto signupDto) throws Exception;

    JWTAuthDto loginWithGoogle(OAuth2User principal);

    JWTAuthDto refreshToken(String refreshToken);
}