package org.application.openschooljwt.services;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Duration duration;

    private JwtParser jwtParser;

    @PostConstruct
    public void setJwtParser(){
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String generateToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+duration.toMillis()))
                .claim("role",role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token){
        Claims body = jwtParser.parseClaimsJws(token).getBody();

        return body.getSubject();
    }

    public boolean isExpired(String token){
        Claims body = jwtParser.parseClaimsJws(token).getBody();

        return body.getExpiration().before(new Date());
    }
}
