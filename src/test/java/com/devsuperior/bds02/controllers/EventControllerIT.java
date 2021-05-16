package com.devsuperior.bds02.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EventControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
	}
	
	@Test
	public void findAllShouldReturnPagedEventsOrderByDate() throws Exception {
		
		ResultActions result =
				mockMvc.perform(get("/events")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].date").value(LocalDate.of(2021, 4, 13)));
		result.andExpect(jsonPath("$.content[1].date").value(LocalDate.of(2021, 5, 3)));
		result.andExpect(jsonPath("$.content[2].date").value(LocalDate.of(2021, 5, 16)));
		result.andExpect(jsonPath("$.content[3].date").value(LocalDate.of(2021, 5, 23)));		
	}
	
	@Test
	public void insertShouldInsertEvent() throws Exception {

		EventDTO dto = new EventDTO(null, "Congresso Teste", LocalDate.of(2021, 5, 16), "https://teste.com", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(post("/events")
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Congresso Teste"));
	}
	
	@Test
	public void deleteShouldReturnNoContentEventWhenIdExists() throws Exception {
		
		ResultActions result =
				mockMvc.perform(delete("/events/{id}", existingId)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ResultActions result =
				mockMvc.perform(delete("/events/{id}", nonExistingId)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
}
