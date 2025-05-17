package org.example.apitests.assertions;

import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;

public class SignupAssert {
    private final ValidatableResponse response;

    public SignupAssert(ValidatableResponse response) {
        this.response = response;
    }

    public SignupAssert isSuccess() {
        response.statusCode(201)
                .body("status", equalTo("success"))
                .body("message", not(emptyString()));
        return this;
    }

    public SignupAssert isValidationError() {
        response.statusCode(400)
                .body("status", equalTo("error"))
                .body("errors", not(anEmptyMap())); // errors — это map c полями
        return this;
    }

    public SignupAssert isConflict(String expectedMsg) {
        response.statusCode(409)
                .body("status", equalTo("error"))
                .body("message", equalTo(expectedMsg));
        return this;
    }

    // Можешь добавить доп. методы если хочешь проверять конкретные поля errors, например hasErrorField(field, message)
}
