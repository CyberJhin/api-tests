package org.example.apitests.model.request;

import lombok.Data;

@Data
public class SigninRequest {
    private String username;
    private String password;
}
