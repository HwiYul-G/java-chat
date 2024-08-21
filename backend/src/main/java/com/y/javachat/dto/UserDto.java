package com.y.javachat.dto;

import jakarta.validation.constraints.NotEmpty;

// password는 포함하지 않는다!
public record UserDto(Long id,

                      @NotEmpty(message = "email이 필수로 필요합니다.")
                      String email,

                      @NotEmpty(message = "사용자 이름이 필요합니다.")
                      String username,

                      boolean enabled,

                      @NotEmpty(message = "역할이 필요합니다.")
                      String roles
) {

}
