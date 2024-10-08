package com.y.javachat.system;

import com.y.javachat.model.User;
import com.y.javachat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        initUser();
    }

    private void initUser() {
        User u1 = User.builder()
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin user")
                .build();

        User u2 = User.builder()
                .email("b@google.com")
                .username("b")
                .password("12345")
                .enabled(true)
                .roles("user")
                .build();

        User u3 = User.builder()
                .email("c@google.com")
                .username("c")
                .password("12345")
                .enabled(true)
                .roles("user")
                .build();

        User u4 = User.builder()
                .email("d@google.com")
                .username("d")
                .password("12345")
                .enabled(false)
                .roles("user")
                .build();

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);
        userService.save(u4);
    }


}
