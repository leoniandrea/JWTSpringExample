package com.andrea.jwt;

import com.andrea.config.SecurityProperties;
import com.andrea.vo.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenBuilder {

    final private SecurityProperties securityProperties;

    public JwtTokenBuilder(SecurityProperties securityProperties){
        this.securityProperties = securityProperties;
    }


    public String build(UserDetailsImpl user){
/*
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
 */

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(securityProperties.getJwtSecret()))
                .setHeaderParam("typ", SecurityProperties.TOKEN_TYPE)
                .setIssuer(securityProperties.getJwtIssuer())
                .setAudience(securityProperties.getJwtAudience())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                //.claim("roles", roles)
                .compact();

        return token;
    }
}
