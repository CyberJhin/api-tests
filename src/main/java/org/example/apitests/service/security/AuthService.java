package org.example.apitests.service.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apitests.jwt.JwtCore;
import org.example.apitests.model.RefreshToken;
import org.example.apitests.model.request.SigninRequest;
import org.example.apitests.model.response.AuthResponse;
import org.example.apitests.service.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;



@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       JwtCore jwtCore,
                       RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(SigninRequest signinRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getUsername(), signinRequest.getPassword())
        );

        String accessToken = jwtCore.generateAccessToken(authentication);
        String userUuid = ((UserDetailsImpl) authentication.getPrincipal()).getUuid();

        // Генерация и установка refreshToken
        RefreshToken refreshToken = refreshTokenService.createToken(userUuid);
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return new AuthResponse(accessToken);
    }
}
