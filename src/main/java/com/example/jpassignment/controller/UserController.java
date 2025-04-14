package com.example.jpassignment.controller;


import com.example.jpassignment.dto.UserDto;
import com.example.jpassignment.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * GET API - Get users by pagination
     * @param offset
     * @param pageSize
     * @param sortBy
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value="offset", required = false, defaultValue = "0") Integer offset,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "sortBy", required = false ) String sortBy){
        sortBy = StringUtils.isNotBlank(sortBy)? sortBy: "userId";
        List<UserDto> userList = userService.getUserData(offset, pageSize,sortBy);
        return new ResponseEntity(userList, HttpStatus.OK);
    }

    /**
     * Create API - create new user
     * @param userDto
     * @return
     */
    @PostMapping("/users")
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserDto userDto){
        userService.saveUser(userDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Delete API - Delete user profile by Id
     * @param userId
     * @return
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable(value = "userId") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     *  Update API - update user profile for given ID
     * @param userId
     * @param userDto
     * @return
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userId, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     *  Get API - Get user details with UserId
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable(value = "userId") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Get API - Get user details by email ID
     * @param emailId
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity getUserByEmail(@RequestParam(value = "emailId") String emailId){
        List<UserDto> users =  userService.getUserByEmail(emailId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
