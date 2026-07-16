package dev.utsav.api.security;


import dev.utsav.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(JwtProperties props){
        byte[] keyBytes = Base64.getDecoder().decode(props.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = props.expriationMs();
    }

    public String generateToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserId(String token){
        return parseToken(token).getSubject();
    }

    public String extractRole(String token){
        return parseToken(token).get("role", String.class);
    }
}
