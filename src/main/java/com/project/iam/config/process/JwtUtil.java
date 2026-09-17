package com.project.iam.config.process;

import com.project.iam.models.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public String generateToken(String username, Map<String, Object> claims){
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer("IAM")
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getToken().getSecret().getBytes(StandardCharsets.UTF_8)))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getToken().getExpiration()))
                .compact();
    }

    public Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getToken().getSecret().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        Date currentDate = new Date();

        long skewedExpirationTime = expirationDate.getTime() + jwtProperties.getToken().getExpiration();

        return skewedExpirationTime < currentDate.getTime();
    }
}
