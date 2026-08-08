package com.github.vitordalvi.ucloan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.services.EquipmentModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentModelController.class)
public class EquipmentModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipmentModelService equipmentModelService;

    @Autowired
    private ObjectMapper objectMapper; // converter objetos java -> json

    @Test
    void shouldReturn400WhenFieldsAreBlank() throws Exception {
        // Arrange
        CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("", "");

        // Act & Assert
        mockMvc.perform(post("api/v1/equipment-models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
