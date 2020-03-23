package com.andrea.testUtils;

import com.andrea.config.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestConfiguration {

	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	public TestRequestExecutorUtil authenticationTestUtil(){
		return new TestRequestExecutorUtil(securityProperties);
	}
}
