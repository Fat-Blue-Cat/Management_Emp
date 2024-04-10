package com.ncc.manage_emp.mapper;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.response.UserResponseDto;
import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapperWithMapstruct {
    @Mapping(target = "userName", source = "users.userName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "role", source = "users.roleName")
    @Mapping(target = "name", source = "users.name")
    UserDto userToUserDto(Users users);


    @Mapping(target = "userName", source = "userDto.userName")
    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "roleName", source = "userDto.role")
    Users userDtoToUser(UserDto userDto);



    UserResponseDto userToUserResponseDto(Users users);



}
