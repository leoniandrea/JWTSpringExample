package com.andrea.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class SecurityProperties {

    @Value("${security.authentication.path}")
    private String authenticationPath;

    @Value("${security.authentication.header.username}")
    private String authenticationUsernameHeader;

    @Value("${security.authentication.header.password}")
    private String authenticationPasswordHeader;

    @Value("${security.header.jwt}")
    private String tokenHeaderName;


    // Signing key for HS512 algorithm
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.tokenIssuer}")
    private String jwtIssuer;

    @Value("${security.jwt.tokenAudience}")
    private String jwtAudience;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
}
