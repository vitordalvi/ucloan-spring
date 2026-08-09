package com.github.vitordalvi.ucloan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.services.EquipmentModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentModelController.class)
@ActiveProfiles("test")
public class EquipmentModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipmentModelService equipmentModelService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private com.github.vitordalvi.ucloan.config.JwtService jwtService;

    @MockitoBean
    private com.github.vitordalvi.ucloan.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private org.springframework.security.web.authentication.logout.LogoutHandler logoutHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn400WhenFieldsAreBlank() throws Exception {
        CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("", "");

        mockMvc.perform(post("/api/v1/equipment-models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
