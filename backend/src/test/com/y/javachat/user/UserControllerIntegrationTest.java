package com.y.javachat.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.y.javachat.system.StatusCode;
import com.y.javachat.model.User;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("User & Auth API 통합 테스트")
@Tag("integration")
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        // a는 모든 permissions를 가진다.
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/auth/login").with(httpBasic("a@google.com", "12345")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("[GET] 전체 사용자 조회")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllUsers_success() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("[GET] id로 개별 사용자 조회")
    void findUserById_success() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.email").value("b@google.com"))
                .andExpect(jsonPath("$.data.username").value("b"));
    }

    @Test
    @DisplayName("[GET] 존재하지 않는 id로 개별 사용자 조회")
    void whenNonExistentId_findUserById_notfound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[POST] 새로운 사용자를 추가")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addUser_success() throws Exception {
        User user = User.builder()
                .email("e@google.com")
                .username("e")
                .password("eeeee1")
                .enabled(true)
                .roles("admin user").build();

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value("e@google.com"));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("[POST] 잘못된 입력값으로 사용자 생성")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserErrorWithInvalidInput() throws Exception {
        User user = User.builder()
                .email("")
                .username("")
                .password("")
                .roles("")
                .build();

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.data.email").value("이메일이 필요합니다."))
                .andExpect(jsonPath("$.data.username").value("사용자 이름이 필요합니다."))
                .andExpect(jsonPath("$.data.password").value("비밀번호가 필요합니다."))
                .andExpect(jsonPath("$.data.roles").value("역할이 필요합니다."));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("[PUT] 사용자 업데이트")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateUser_success() throws Exception {
        User user = User.builder()
                .email("c@google.com")
                .username("c - update")
                .enabled(false)
                .roles("user")
                .build();

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.email").value("c@google.com"))
                .andExpect(jsonPath("$.data.username").value("c - update"))
                .andExpect(jsonPath("$.data.enabled").value(false))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    @DisplayName("[PUT] 존재하지 않는 사용자를 업데이트한다.")
    void whenNonExistentId_updateUser_notFound() throws Exception {
        User user = User.builder()
                .email("e@google.com")
                .username("e - update")
                .enabled(false)
                .roles("user")
                .build();

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(put(this.baseUrl + "/users/7").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[PUT] 타당하지 않은 입력으로 사용자를 업데이트한다.")
    void whenInvalidInput_updateUser_invalidArgument() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("")
                .username("")
                .roles("")
                .build();

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(put(this.baseUrl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT));
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("[DELETE] 사용자 삭제")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteUser_success() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[DELETE] 존재하지 않는 id를 가진 사용자 삭제")
    void whenNonExistentId_deleteUser_notFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/100").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[DELETE] permission을 가지지 않은 상태로 사용자 삭제 불가")
    void whenNoAccessAsRoleUser_deleteUser_noPermission() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/auth/login").with(httpBasic("b@google.com", "232345"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

}
