package com.y.javachat.converter;

import com.y.javachat.model.User;
import com.y.javachat.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto source) {
        return User.builder()
                .email(source.email())
                .username(source.username())
                .enabled(source.enabled())
                .roles(source.roles())
                .build();
    }
}
