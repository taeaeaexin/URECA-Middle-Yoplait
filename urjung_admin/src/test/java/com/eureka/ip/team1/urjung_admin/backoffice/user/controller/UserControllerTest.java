//package com.eureka.ip.team1.urjung_admin.backoffice.user.controller;
//
//import com.eureka.ip.team1.urjung_admin.backoffice.user.dto.UserDto;
//import com.eureka.ip.team1.urjung_admin.backoffice.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    public void getAllUsers_SUCCESS() throws Exception {
//        List<UserDto> users = List.of(
//                UserDto.builder()
//                        .id("test1@test.com")
//                        .name("테스트1")
//                        .build(),
//                UserDto.builder()
//                        .id("test2@test.com")
//                        .name("테스트2")
//                        .build()
//        );
//
//        given(userService.getAllUsers()).willReturn(users);
//
//        // when & then
//        mockMvc.perform(get("/admin/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].id").value("test1@test.com"))
//                .andExpect(jsonPath("$[1].subscribed").value(false));
//    }
//}
