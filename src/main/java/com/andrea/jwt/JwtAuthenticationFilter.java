package com.andrea.jwt;

import com.andrea.config.SecurityProperties;
import com.andrea.vo.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenBuilder jwtTokenBuilder;
    private final String tokenHeaderName;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String authenticationPath, JwtTokenBuilder jwtTokenBuilder, String tokenHeaderName) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenBuilder = jwtTokenBuilder;
        this.tokenHeaderName = tokenHeaderName;
        setFilterProcessesUrl(authenticationPath);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
		UserDetailsImpl user = ((UserDetailsImpl) authentication.getPrincipal());

        String token = jwtTokenBuilder.build(user);

        response.addHeader(tokenHeaderName, SecurityProperties.TOKEN_PREFIX + token);
    }
}