package com.y.javachat.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y.javachat.system.StatusCode;
import com.y.javachat.system.exception.ObjectNotFoundException;
import com.y.javachat.user.dto.UserDto;
import com.y.javachat.user.model.User;
import com.y.javachat.user.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    List<User> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();

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
                .enabled(true)
                .roles("user")
                .build();

        users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);

    }

    @Test
    void findAllUsers_success() throws Exception {
        // given
        given(userService.findAll()).willReturn(this.users);
        // when and then
        mockMvc.perform(get(baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(users.size())))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("a"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].username").value("b"))
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].username").value("c"));
    }

    @Test
    void findUserById_success() throws Exception {
        // given
        given(userService.findById(2L)).willReturn(users.get(1));
        // when and then
        mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("b"))
                .andExpect(jsonPath("$.data.email").value("b@google.com"));
    }

    @Test
    void findUserById_notFound() throws Exception {
        // given
        given(userService.findById(5L)).willThrow(new ObjectNotFoundException("user", 5L));
        // when and then
        mockMvc.perform(get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("id: 5를 가진 user을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void adduser_success() throws Exception {
        User user = User.builder()
                .id(4L)
                .username("d")
                .password("12345d")
                .enabled(true)
                .roles("admin")
                .email("d@google.com")
                .build();

        String json = objectMapper.writeValueAsString(user);

        // given
        given(userService.save(Mockito.any(User.class))).willReturn(user);

        // when and then
        mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("d"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin"))
                .andExpect(jsonPath("$.data.email").value("d@google.com"));


    }

    @Test
    void updateUser_success() throws Exception {
        UserDto userDto = new UserDto(2L, "b@google.com", "b - update", true, "user");

        User updatedUser = User.builder()
                .id(2L)
                .email("b@google.com")
                .username("b - update")
                .enabled(true)
                .roles("user").build();

        String json = objectMapper.writeValueAsString(userDto);

        // given
        given(userService.update(eq(2L), Mockito.any(User.class))).willReturn(updatedUser);
        // when and then
        mockMvc.perform(put(baseUrl + "/users/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.email").value("b@google.com"))
                .andExpect(jsonPath("$.data.username").value("b - update"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("user"));

    }

    @Test
    void updateUser_notFound() throws Exception {
        // given
        given(userService.update(eq(5L), Mockito.any(User.class))).willThrow(new ObjectNotFoundException("user", 5L));

        UserDto userDto = new UserDto(5L, "b@google.com", "b", false, "user");

        String json = objectMapper.writeValueAsString(userDto);

        // when and then
        mockMvc.perform(put(baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("id: 5를 가진 user을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUser_success() throws Exception {
        // given
        doNothing().when(userService).delete(2L);
        // when and then
        mockMvc.perform(delete(baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    void deleteUser_notFound() throws Exception {
        // given
        doThrow(new ObjectNotFoundException("user", 5L)).when(userService).delete(5L);
        // when and then
        mockMvc.perform(delete(baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("id: 5를 가진 user을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}