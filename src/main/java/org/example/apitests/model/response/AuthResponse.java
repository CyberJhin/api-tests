package org.example.apitests.model.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // Геттеры и сеттеры

}

