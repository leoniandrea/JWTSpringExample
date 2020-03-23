package com.andrea.controllers;

import com.andrea.Application;
import com.andrea.config.SecurityProperties;
import com.andrea.dto.rest.GetBookListResponse;
import com.andrea.testUtils.IntegrationTestConfiguration;
import com.andrea.testUtils.TestRequestExecutorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {Application.class, IntegrationTestConfiguration.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class BookRetrievalControllerEndToEndTest {
	@Autowired
	private MockMvc mvc;


	@Autowired
	private TestRequestExecutorUtil testRequestExecutorUtil;


	@Test
	public void getBooksIsRetrievingCorrectBook() throws Exception {
		String tokenHeader = testRequestExecutorUtil.authenticate(mvc, "andrea", "password");
		GetBookListResponse getBookListResponse = testRequestExecutorUtil.callGetBookList(mvc, tokenHeader);
		assertEquals("La Compagnia dell'Anello", getBookListResponse.getBooks().get(0).getTitle());
	}

	@Test
	public void getBooksIsRetrievingCorrectBookWithUser2() throws Exception {
		String tokenHeader = testRequestExecutorUtil.authenticate(mvc, "leo", "password");
		GetBookListResponse getBookListResponse = testRequestExecutorUtil.callGetBookList(mvc, tokenHeader);
		assertTrue(getBookListResponse.getBooks() == null || getBookListResponse.getBooks().size() == 0);
	}

}