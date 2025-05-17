package org.example.apitests.testutil;

import org.example.apitests.model.request.SignupRequest;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class UserRegistrationFactory {
    public static SignupRequest valid() {
        return Instancio.of(SignupRequest.class)
                .set(field(SignupRequest::getEmail), "user" + System.nanoTime() + "@example.com")
                .set(field(SignupRequest::getUsername), "user" + System.nanoTime())
                .set(field(SignupRequest::getPassword), "SecurePass123!")
                .create();
    }

    public static SignupRequest withoutEmail() {
        SignupRequest req = valid();
        req.setEmail(null);
        return req;
    }

    public static SignupRequest withEmail(String email) {
        SignupRequest req = valid();
        req.setEmail(email);
        return req;
    }

    public static SignupRequest withoutPassword() {
        SignupRequest req = valid();
        req.setPassword(null);
        return req;
    }

    public static SignupRequest withPassword(String password) {
        SignupRequest req = valid();
        req.setUsername(password);
        return req;
    }

    public static SignupRequest withoutUsername() {
        SignupRequest req = valid();
        req.setUsername(null);
        return req;
    }

    public static SignupRequest withUsername(String username) {
        SignupRequest req = valid();
        req.setUsername(username);
        return req;
    }


    public static SignupRequest empty() {
        return new SignupRequest();
    }
}