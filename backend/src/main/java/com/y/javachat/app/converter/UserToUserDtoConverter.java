package com.y.javachat.app.converter;

import com.y.javachat.app.model.User;
import com.y.javachat.app.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        return new UserDto(
                source.getId(),
                source.getEmail(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles()
        );
    }
}
