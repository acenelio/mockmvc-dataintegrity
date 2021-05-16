package com.devsuperior.bds02.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CityControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	private Long dependentId;
	private Long independentId;
	private Long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
		dependentId = 1L;
		independentId = 5L;
		nonExistingId = 1000L;
	}
	
	@Test
	public void deleteShouldReturnNoContentEventWhenIndependentId() throws Exception {
		
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", independentId)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent()); // 204
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ResultActions result =
				mockMvc.perform(delete("/events/{id}", nonExistingId)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound()); // 404
	}

	@Test
	public void deleteShouldReturnBadRequestEventWhenDependentId() throws Exception {
		
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", dependentId)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest()); // 400
	}
}
