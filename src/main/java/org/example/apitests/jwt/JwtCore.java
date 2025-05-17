package org.example.apitests.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.example.apitests.service.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${testing.app.secret}")
    private String accessSecret;

    @Value("${testing.app.refreshSecret}")
    private String refreshSecret;

    @Value("${testing.app.lifetime}")
    private int accessLifetime;

    @Value("${testing.app.refreshLifetime}")
    private int refreshLifetime;

    @PostConstruct
    public void init() {
        System.out.println("Access secret: " + accessSecret);
        System.out.println("Refresh secret: " + refreshSecret);
    }

    public String generateAccessToken(Authentication authentication) {
        // Получаем объект пользователя из Authentication
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String uuid = userDetails.getUuid();  // Извлекаем uuid пользователя из UserDetailsImpl
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessLifetime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, accessSecret)
                .compact();
    }
    public String generateAccessToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessLifetime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, accessSecret)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        // Получаем объект пользователя из Authentication
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String uuid = userDetails.getUuid();  // Извлекаем uuid пользователя из UserDetailsImpl

        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshLifetime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, refreshSecret)
                .compact();
    }

    public String generateRefreshToken(String uuid) {
        System.out.println("Access secret: " + accessSecret);
        System.out.println("Refresh secret: " + refreshSecret);

        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshLifetime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, refreshSecret)
                .compact();
    }


    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }


    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }


    // Общая логика валидации токенов
    private boolean validateToken(String token, String secret) {
        try {
            // Извлекаем данные из токена
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            // Проверяем UUID
            String uuid = claims.getSubject(); // getSubject() возвращает "uuid", если он был установлен
            if (uuid != null) {// Если срок действия истек, будет выброшено исключение ExpiredJwtException
                Date expirationDate = claims.getExpiration();
                return !expirationDate.before(new Date());
            } else {
                return false;
            }

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUuidFromRefreshToken(String token) {
        return Jwts.parser()
                .setSigningKey(refreshSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Subject теперь хранит uuid
    }

    public String getUuidFromAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(accessSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Subject теперь хранит uuid
    }
}
