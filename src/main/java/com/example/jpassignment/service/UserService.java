package com.example.jpassignment.service;

import com.example.jpassignment.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUserData(int offset, int pageSize, String sortBy);

    void saveUser(UserDto userDto);
    void deleteUser(Long userId);

    UserDto getUserById(long userId);

    UserDto updateUser(long userId, UserDto userDto);

    List<UserDto> getUserByEmail(String email);
}
