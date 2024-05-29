package com.y.javachat.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "이메일이 필요합니다.")
    private String email;

    @NotEmpty(message = "사용자 이름이 필요합니다.")
    private String username;

    @NotEmpty(message = "비밀번호가 필요합니다.")
    private String password;

    private boolean enabled;

    @NotEmpty(message = "역할이 필요합니다.")
    private String roles;


}
