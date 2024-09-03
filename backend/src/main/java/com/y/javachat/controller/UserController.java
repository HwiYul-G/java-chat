package com.y.javachat.controller;

import com.y.javachat.converter.NotificaitonToNotificationResponseDtoConverter;
import com.y.javachat.converter.UserDtoToUserConverter;
import com.y.javachat.converter.UserToUserDtoConverter;
import com.y.javachat.dto.ChatRoomResponseDto;
import com.y.javachat.dto.FriendResponseDto;
import com.y.javachat.dto.NotificationResponseDto;
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
    private final NotificaitonToNotificationResponseDtoConverter notificaitonToNotificationResponseDtoConverter;


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

    @GetMapping("/{userId}/notifications")
    public Result getNotifications(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = userService
                .getAllNotifications(userId)
                .stream()
                .map(this.notificaitonToNotificationResponseDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "알림 목록 조회 성공", notifications);
    }

    @GetMapping("/{userId}/chat-rooms")
    public Result getChatRooms(@PathVariable Long userId, @RequestParam boolean isGroup){
        List<ChatRoomResponseDto> chatRooms = userService.findChatRoomsByUserId(userId, isGroup);
        return new Result(true, StatusCode.SUCCESS, "채팅방 목록 조회 성공", chatRooms);
    }

}
