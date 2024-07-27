package com.y.javachat.app.controller;

import com.y.javachat.app.model.User;
import com.y.javachat.app.service.UserService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import com.y.javachat.app.converter.UserDtoToUserConverter;
import com.y.javachat.app.converter.UserToUserDtoConverter;
import com.y.javachat.app.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("${api.endpoint.base-url}/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    @GetMapping
    public Result findAllUsers() {
        List<User> foundUsers = userService.findAll();
        List<UserDto> userDtos = foundUsers.stream()
                .map(userToUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Long userId) {
        User foundUser = userService.findById(userId);
        UserDto userDto = userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

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


}
