package com.andrea.config;

import com.andrea.jwt.JwtAuthenticationFilter;
import com.andrea.jwt.JwtAuthorizationFilter;
import com.andrea.jwt.JwtTokenBuilder;
import com.andrea.services.IUserRetrievalService;
import com.andrea.session.RequestUserDataContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    final IUserRetrievalService userRetrievalService;

    final SecurityProperties securityProperties;

    final RequestUserDataContext requestUserDataContext;

    final JwtTokenBuilder jwtTokenBuilder;

    public SecurityConfiguration(IUserRetrievalService userRetrievalService, SecurityProperties securityProperties, RequestUserDataContext requestUserDataContext, JwtTokenBuilder jwtTokenBuilder) {
        this.userRetrievalService = userRetrievalService;
        this.securityProperties = securityProperties;
        this.requestUserDataContext = requestUserDataContext;
        this.jwtTokenBuilder = jwtTokenBuilder;
    }


    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), requestUserDataContext, securityProperties);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), securityProperties.getAuthenticationPath(), jwtTokenBuilder, securityProperties.getTokenHeaderName());

        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(jwtAuthorizationFilter())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userRetrievalService);
        /*auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .authorities("ROLE_USER");*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }
}