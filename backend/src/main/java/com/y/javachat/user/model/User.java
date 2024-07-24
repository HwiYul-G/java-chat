package com.y.javachat.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
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
