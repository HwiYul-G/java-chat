package com.y.javachat.system;

import com.y.javachat.user.User;
import com.y.javachat.user.UserRepository;
import com.y.javachat.user.UserService;
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
                .id(1L)
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin user")
                .build();

        User u2 = User.builder()
                .id(2L)
                .email("b@google.com")
                .username("b")
                .password("232345")
                .enabled(true)
                .roles("user")
                .build();

        User u3 = User.builder()
                .id(3L)
                .email("c@google.com")
                .username("c")
                .password("09876")
                .enabled(false)
                .roles("user")
                .build();

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);
    }


}
