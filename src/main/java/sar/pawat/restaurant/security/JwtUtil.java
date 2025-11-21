package sar.pawat.restaurant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey key;

    // Key: token -> value: username
    // we usually store these tokens in an in-memory database such as Redis
    private final Map<String, String> blacklistedTokens = new ConcurrentHashMap<>();

    // Initializes the key after the class is instantiated and
    // the jwtSecret is injected, preventing the repeated creation
    // of the key and enhancing performance
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    // Get username from JWT token
    public String getUsernameFromToken(String token) {
        return this.validateJwtToken(token)
                .getPayload().getSubject();
    }
    // Validate JWT token
    public Jws<io.jsonwebtoken.Claims> validateJwtToken(String token) {
        final var claims = Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token);
        if(blacklistedTokens.containsKey(HexFormat.of().formatHex(claims.getDigest())))
            throw new SecurityException("Revoked JWT Token");
        return claims;
    }

    public void invalidateToken(String token) {
        final var claims = validateJwtToken(token);
        blacklistedTokens.put(HexFormat.of().formatHex(claims.getDigest()), claims.getPayload().getSubject());
    }
}
