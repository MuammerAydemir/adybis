package com.muammer.adybis.base.security;

import org.springframework.stereotype.Service;

import com.muammer.adybis.base.configs.AppProperties;
import com.muammer.adybis.user.services.Impls.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    private final String secret;
    private final long jwtExpTime;

    JwtService(AppProperties appProperties) {
        this.secret = appProperties.getJwtSecretKey();
        this.jwtExpTime = appProperties.getJwtExpirationTime().toMillis();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private Claims extractAllClaims(String token)
            throws JwtException, IllegalArgumentException, UnsupportedJwtException {

        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();

    }

    public String generateToken(UserDetailsImpl userDetails) {
        long now = System.currentTimeMillis();

        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpTime))
                .claim("nonce", UUID.randomUUID().toString())
                .signWith(getSecretKey()).compact();

    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetailsImpl userDetailsImpl) {
        return extractUserName(token).equals(userDetailsImpl.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
