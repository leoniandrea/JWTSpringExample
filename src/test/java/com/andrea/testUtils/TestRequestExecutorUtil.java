package com.andrea.testUtils;

import com.andrea.config.SecurityProperties;
import com.andrea.dto.rest.GetBookListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestRequestExecutorUtil {

	private SecurityProperties securityProperties;

	TestRequestExecutorUtil(SecurityProperties securityProperties){
		this.securityProperties = securityProperties;
	}


	public String authenticate(MockMvc mvc, String username, String password) throws Exception {
		MockHttpServletRequestBuilder requestBuilder =
				post("/authenticate")
						.param("username", username)
						.param("password", password);

		MvcResult mvcResult =
				mvc.perform(requestBuilder)
						.andExpect(status().isOk())
						.andReturn();

		return mvcResult.getResponse().getHeader(securityProperties.getTokenHeaderName());
	}

	public GetBookListResponse callGetBookList(MockMvc mvc, String tokenHeader) throws Exception {
		MockHttpServletRequestBuilder getBookRequestBuilder = get("/books")
				.header(securityProperties.getTokenHeaderName(), tokenHeader)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult getBookMvcResult = mvc.perform(getBookRequestBuilder).andExpect(status().isOk()).andReturn();
		return new ObjectMapper().readValue(getBookMvcResult.getResponse().getContentAsString(), GetBookListResponse.class);
	}
}
