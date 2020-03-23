package com.andrea.config;

import com.andrea.Application;
import com.andrea.testUtils.TestRequestExecutorUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {Application.class, TestRequestExecutorUtil.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class JwtAuthenticationFilterIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	TestRequestExecutorUtil testRequestExecutorUtil;

	@Autowired
	SecurityProperties securityProperties;


	@Test
	public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
		mvc.perform(get("/books")).andExpect(status().isForbidden());
	}

	@Test
	public void authenticateCorrectly() throws Exception {
		String tokenHeader = testRequestExecutorUtil.authenticate(mvc,"andrea", "password");

		Jws<Claims> parsedToken = Jwts.parser()
				.setSigningKey(TextCodec.BASE64.decode(securityProperties.getJwtSecret()))
				.parseClaimsJws(tokenHeader.replace(SecurityProperties.TOKEN_PREFIX, ""));

		String userName = parsedToken.getBody().getSubject();

		assertEquals("andrea", userName);
	}

	@Test
	public void performRequestWithJWTToken() throws Exception {
		String tokenHeader = testRequestExecutorUtil.authenticate(mvc, "andrea", "password");

		MockHttpServletRequestBuilder getBookRequestBuilder = get("/books")
				.header(securityProperties.getTokenHeaderName(), tokenHeader)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		mvc.perform(getBookRequestBuilder).andExpect(status().isOk());
	}


}