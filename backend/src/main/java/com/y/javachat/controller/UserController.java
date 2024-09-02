package com.y.javachat.controller;

import com.y.javachat.converter.UserDtoToUserConverter;
import com.y.javachat.converter.UserToUserDtoConverter;
import com.y.javachat.dto.FriendResponseDto;
import com.y.javachat.dto.UserDto;
import com.y.javachat.model.User;
import com.y.javachat.service.UserService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("${api.endpoint.base-url}/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;


    @GetMapping("/email")
    public Result findUserByEmail(@RequestParam String email){
        User foundUser = userService.findByEmail(email);
        UserDto userDto = userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody User newUser) {
        User savedUser = userService.save(newUser);
        UserDto savedUserDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        User update = userDtoToUserConverter.convert(userDto);
        User updatedUser = userService.update(userId, update);
        UserDto updatedUserDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @GetMapping("/{userId}/friends")
    public Result findFriendsByUserId(@PathVariable Long userId){
        List<FriendResponseDto> friends = this.userService.findFriendsByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "친구 찾기 성공", friends);
    }

}
