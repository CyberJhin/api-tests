package org.example.apitests.service.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apitests.customException.InvalidRefreshTokenException;
import org.example.apitests.jwt.JwtCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenService {

    private final RefreshTokenService refreshTokenService;
    private final JwtCore jwtCore;

    @Autowired
    public TokenService(RefreshTokenService refreshTokenService, JwtCore jwtCore) {
        this.refreshTokenService = refreshTokenService;
        this.jwtCore = jwtCore;
    }

    public Map<String, String> refreshTokens(String token, HttpServletResponse response) {
        if (token == null || !refreshTokenService.validate(token)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }

        String userUuid = jwtCore.getUuidFromRefreshToken(token);
        if (userUuid == null) {
            throw new InvalidRefreshTokenException("Invalid token");
        }

        // Удалить использованный токен (одноразовость)
        refreshTokenService.delete(token);

        // Создать новый refresh и access токены
        Authentication authentication = new UsernamePasswordAuthenticationToken(userUuid, null, null);
        String accessToken = jwtCore.generateAccessToken(authentication);
        String newRefreshToken = refreshTokenService.createToken(userUuid).getToken();

        // Установить новый refreshToken в куку
        Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return Map.of("accessToken", accessToken);
    }
}
