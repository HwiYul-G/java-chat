package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User extends BaseModel {
    @NotEmpty(message = "이메일이 필요합니다.")
    @Column(name = "email", length = 256)
    private String email;

    @NotEmpty(message = "사용자 이름이 필요합니다.")
    @Column(name = "email", length = 256)
    private String username;

    @NotEmpty(message = "비밀번호가 필요합니다.")
    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @NotEmpty(message = "역할이 필요합니다.")
    @Column(name = "roles", length = 256)
    private String roles;

}
