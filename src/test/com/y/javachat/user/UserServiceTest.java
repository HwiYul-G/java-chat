package com.y.javachat.user;

import com.y.javachat.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp() {
        User u1 = User.builder()
                .id(1L)
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin")
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
                .enabled(true)
                .roles("user")
                .build();

        users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll_success() {
        // given
        given(userRepository.findAll()).willReturn(users);
        // when
        List<User> actualUsers = userService.findAll();
        // then
        assertThat(actualUsers.size()).isEqualTo(users.size());
        // verify userepository.findAll() is called exactly once
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void findById_success() {
        // given
        User u1 = User.builder()
                .id(1L)
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin")
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(u1));
        // when
        User returnedUser = userService.findById(1L);
        // then
        assertThat(returnedUser.getId()).isEqualTo(u1.getId());
        assertThat(returnedUser.getEmail()).isEqualTo(u1.getEmail());
        assertThat(returnedUser.getPassword()).isEqualTo(u1.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(u1.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(u1.getRoles());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        // given
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());
        // when
        Throwable thrown = catchThrowable(() -> {
            User returnedUser = userService.findById(100L);
        });
        // then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("id: 100를 가진 user을 찾을 수 없습니다.");
        verify(userRepository, times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    void save_success() {
        // given
        User newUser = User.builder()
                .email("d@google.com")
                .password("34345")
                .enabled(true)
                .username("d")
                .roles("user")
                .build();

        given(userRepository.save(newUser)).willReturn(newUser);
        // when
        User returnedUser = userService.save(newUser);
        // then
        assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(returnedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void update_success() {
        // given
        User oldUser = User.builder()
                .id(1L)
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin")
                .build();

        User update = User.builder()
                .email("a@google.com")
                .username("a - update")
                .password("12345")
                .enabled(true)
                .roles("admin")
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        // when
        User updatedUser = userService.update(1L, update);

        // then
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(oldUser);
    }

    @Test
    void update_notFound() {
        // given
        User update = User.builder()
                .email("a@google.com")
                .username("a - update")
                .password("12345")
                .enabled(true)
                .roles("admin")
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        // when
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            userService.update(1L, update);
        });
        // then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("id: 1를 가진 user을 찾을 수 없습니다.");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void delete_success() {
        // given
        User user = User.builder()
                .id(1L)
                .email("a@google.com")
                .username("a")
                .password("12345")
                .enabled(true)
                .roles("admin")
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);
        // when
        userService.delete(1L);
        // then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        // when
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            userService.delete(1L);
        });
        // then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("id: 1를 가진 user을 찾을 수 없습니다.");
        verify(userRepository, times(1)).findById(1L);
    }
}