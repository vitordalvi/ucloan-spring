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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        org.mockito.Mockito.doAnswer(invocation -> {
            jakarta.servlet.FilterChain filterChain = invocation.getArgument(2);
            filterChain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void shouldReturn400WhenFieldsAreBlank() throws Exception {
        CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("", "");

        mockMvc.perform(post("/api/v1/equipment-models")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
