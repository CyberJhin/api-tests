package org.example.apitests.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.example.apitests.Util.CookieUtils;
import org.example.apitests.jwt.JwtCore;
import org.example.apitests.model.RefreshToken;
import org.example.apitests.model.User;
import org.example.apitests.model.request.SigninRequest;
import org.example.apitests.model.request.SignupRequest;
import org.example.apitests.model.response.AuthResponse;
import org.example.apitests.repository.UserRepository;
import org.example.apitests.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.apitests.service.RefreshTokenService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public void setUserRepository(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest,
                                    HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.getUsername(), signinRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtCore.generateAccessToken(authentication);
            String userUuid = ((UserDetailsImpl) authentication.getPrincipal()).getUuid();

            // Генерация нового refreshToken и его сохранение в базе данных
            RefreshToken refreshToken = refreshTokenService.createToken(userUuid);

            // Устанавливаем refreshToken в HttpOnly cookie
            Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // обязательно для HTTPS
            cookie.setPath("/");    // доступен на всём сервере
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthResponse(accessToken));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.status(400).body("Username already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(400).body("Email already exists");
        }

        User user = new User();
        String hashed = passwordEncoder.encode(signupRequest.getPassword());
        user.setPassword(hashed);
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request,
                                                HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, "refreshToken");

        if (token == null || !refreshTokenService.validate(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        String userUuid = jwtCore.getUuidFromRefreshToken(token);
        if (userUuid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
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

        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }
}
