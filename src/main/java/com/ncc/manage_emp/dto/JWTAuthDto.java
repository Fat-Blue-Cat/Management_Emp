package com.ncc.manage_emp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

}