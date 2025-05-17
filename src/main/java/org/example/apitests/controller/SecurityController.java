package org.example.apitests.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.apitests.customException.InvalidRefreshTokenException;
import org.example.apitests.customException.UserAlreadyExistsException;
import org.example.apitests.model.request.SigninRequest;
import org.example.apitests.model.request.SignupRequest;
import org.example.apitests.model.response.AuthResponse;
import org.example.apitests.service.security.AuthService;
import org.example.apitests.service.security.RegistrationService;
import org.example.apitests.service.security.TokenService;
import org.example.apitests.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {


    private final RegistrationService registrationService;
    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public SecurityController(RegistrationService registrationService, AuthService authService, TokenService tokenService) {
        this.registrationService = registrationService;
        this.authService = authService;
        this.tokenService = tokenService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest,
                                    BindingResult bindingResult,
                                    HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errors
            ));
        }
        try {
            AuthResponse authResponse = authService.login(signinRequest, response);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "accessToken", authResponse.getAccessToken()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Invalid credentials"
            ));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errors
            ));
        }
        try {
            registrationService.registerUser(signupRequest);
            return ResponseEntity.status(201).body(Map.of(
                    "status", "success",
                    "message", "User registered successfully"
            ));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, "refreshToken");

        try {
            var tokens = tokenService.refreshTokens(token, response);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "accessToken", tokens.get("accessToken")
            ));
        } catch (InvalidRefreshTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
