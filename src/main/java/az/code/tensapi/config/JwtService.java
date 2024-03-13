package az.code.tensapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET_KEY = "8f09669ab33ae0c90f375f07bca6b22f5ac2a496e505c5ffa3cf15460b69a882";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME_MS = 1000 * 60 * 60; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION_TIME_MS = 1000L * 60 * 60 * 24; // 1 day

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("tokenType", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTResolver) {
        final Claims claims = extractClaims(token);
        return claimsTResolver.apply(claims);
    }

    public Claims extractClaims(String token) throws RuntimeException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateJwt(userDetails, ACCESS_TOKEN_EXPIRATION_TIME_MS, "access");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwt(userDetails, REFRESH_TOKEN_EXPIRATION_TIME_MS, "refresh");
    }

    public String generateJwt(UserDetails userDetails, long expirationTime, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isExpired(token);
    }

    public boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
