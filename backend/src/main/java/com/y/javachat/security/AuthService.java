package com.y.javachat.security;

import com.y.javachat.model.MyUserPrincipal;
import com.y.javachat.converter.UserToUserDtoConverter;
import com.y.javachat.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info.
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        UserDto userDto = userToUserDtoConverter.convert(principal.getUser());

        // Create a JWT.
        String token = jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
