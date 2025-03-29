package com.inventariopro.JwtUtil;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String SECRET_KEY = "inventarioProSecretinventarioProSecret"; // Debe ser de al menos 256 bits
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    private static final Key KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes()));

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
