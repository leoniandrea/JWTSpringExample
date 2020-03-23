package com.andrea.session;


import com.andrea.services.IUserRetrievalService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestUserDataContext {

	final IUserRetrievalService userRetrievalService;

	public RequestUserDataContext(IUserRetrievalService userRetrievalService){
		this.userRetrievalService = userRetrievalService;
	}

	private String authenticatedUserName;


	public void retrieveUserAndPutInSession(String userName){
		UserDetails user = userRetrievalService.loadUserByUsername(userName);
		authenticatedUserName = userName;
	}
}
