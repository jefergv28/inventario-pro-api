package com.inventariopro.crud.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Usa aquí la clave secreta igual que en el frontend (texto plano)
    private static final String SECRET_KEY = "miClaveSuperSecretaMuyLargaYSegura1234567890";

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

  public String getUserNameFromToken(String token) {
    try {
        return extractClaim(token, Claims::getSubject);
    } catch (Exception e) {
        System.out.println("Error al obtener el username del token: " + e.getMessage());
        return null;
    }
}


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @SuppressWarnings("UseSpecificCatch")
   private Claims getAllClaims(String token) {
    try {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    } catch (Exception e) {
        System.out.println("Token inválido o corrupto: " + e.getMessage());
        return null;
    }
}


    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaims(token);
    return claims != null ? claimsResolver.apply(claims) : null;
}


    public Date getExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
    }

}
