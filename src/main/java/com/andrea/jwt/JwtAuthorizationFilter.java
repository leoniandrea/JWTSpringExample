package com.andrea.jwt;

import com.andrea.config.SecurityProperties;
import com.andrea.session.RequestUserDataContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    final RequestUserDataContext requestUserDataContext;
    final SecurityProperties securityProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, RequestUserDataContext requestUserDataContext, SecurityProperties securityProperties) {
        super(authenticationManager);
        this.requestUserDataContext = requestUserDataContext;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(securityProperties.getTokenHeaderName());
        if (StringUtils.isEmpty(token) || !token.startsWith(SecurityProperties.TOKEN_PREFIX)) {
            return null;
        }


        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(securityProperties.getJwtSecret()))
                    .parseClaimsJws(token.replace(SecurityProperties.TOKEN_PREFIX, ""));

            String userName = parsedToken.getBody().getSubject();
            requestUserDataContext.retrieveUserAndPutInSession(userName);
/*
            List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                    .get("roles")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toList());
*/
            return new UsernamePasswordAuthenticationToken(userName, null, null);
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }

        return null;
    }
}
