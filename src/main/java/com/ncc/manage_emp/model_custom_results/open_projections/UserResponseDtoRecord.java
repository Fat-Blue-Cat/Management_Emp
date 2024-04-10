package com.ncc.manage_emp.model_custom_results.open_projections;

import com.ncc.manage_emp.response.TimeLogsResponseDto;

import java.util.List;

public record UserResponseDtoRecord(Long id, String name,String checkinCode, String email, String roleName) {
}
