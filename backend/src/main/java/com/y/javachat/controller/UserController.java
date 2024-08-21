package com.y.javachat.controller;

import com.y.javachat.dto.FriendRequestDto;
import com.y.javachat.dto.FriendResponseDto;
import com.y.javachat.model.FriendRequest;
import com.y.javachat.model.User;
import com.y.javachat.service.UserService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import com.y.javachat.converter.UserDtoToUserConverter;
import com.y.javachat.converter.UserToUserDtoConverter;
import com.y.javachat.dto.UserDto;
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

    @GetMapping("/{userId}/friends")
    public Result findFriendsByUserId(@PathVariable Long userId){
        List<FriendResponseDto> friends = this.userService.findFriendsByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "친구 찾기 성공", friends);
    }

    @PostMapping("/invite-friend")
    public Result inviteFriend(@Valid @RequestBody FriendRequestDto friendRequestDto){
        userService.createFriendRequest(friendRequestDto.userId(), friendRequestDto.friendEmail());
        return new Result(true, StatusCode.SUCCESS, "친구 초대 요청 성공");
    }

    @PostMapping("/friend-requests/{friendRequestId}/accept")
    public Result acceptFriendRequest(@PathVariable Long friendRequestId){
        userService.acceptFriendRequest(friendRequestId);
        return new Result(true, StatusCode.SUCCESS, "친구 초대 요청 수락 성공");
    }

    @DeleteMapping("/friend-requests/{friendRequestId}")
    public Result declineFriendRequest(@PathVariable Long friendRequestId){
        userService.declineFriendRequest(friendRequestId);
        return new Result(true, StatusCode.SUCCESS, "친구 초대 요청 거절 성공");
    }

    @GetMapping("/{userId}/request-friends")
    public Result findFriendRequests(@PathVariable Long userId){
        List<FriendRequest> friendRequests = userService.findFriendRequestsByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "친구 초대 요청 목록 조회 성공",friendRequests);
    }


}
