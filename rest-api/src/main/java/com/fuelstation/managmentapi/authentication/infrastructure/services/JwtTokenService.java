package com.fuelstation.managmentapi.authentication.infrastructure.services;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

    @Value("${security.jwt.secret-key}")
    private String secretKey; //Encoded Base64 format and at least 256 bits is recommended
    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpire;
    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    public String generateAccessToken(Credentials user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRole().name().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpire))
                .signWith(getSigninKey())
                .compact();
    }

    public String generateRefreshToken(Credentials user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpire))
                .signWith(getSigninKey())
                .compact();
    }

    public boolean isValidAccessToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    public boolean isValidRefreshToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername)) && !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UserRole getUserRoleFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String roleName = claims.get("roles", String.class);
        return UserRole.valueOf(roleName);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}