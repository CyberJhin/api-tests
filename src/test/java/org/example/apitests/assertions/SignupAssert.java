package org.example.apitests.assertions;

import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Allure;

public class SignupAssert {
    private final ValidatableResponse response;

    public SignupAssert(ValidatableResponse response) {
        this.response = response;
    }

    public SignupAssert isSuccess() {
        Allure.step("Проверяем успешный статус регистрации");
        String resp = response.extract().asString();
        Allure.addAttachment("Ответ регистрации", "application/json", resp, ".json");
        response.statusCode(201)
                .body("status", equalTo("success"))
                .body("message", not(emptyString()));
        return this;
    }

    public SignupAssert isConflict(String expectedMsg) {
        Allure.step("Проверяем конфликт (409) и сообщение");
        String resp = response.extract().asString();
        Allure.addAttachment("Ответ с ошибкой (409)", "application/json", resp, ".json");
        response.statusCode(409)
                .body("status", equalTo("error"))
                .body("message", equalTo(expectedMsg));
        return this;
    }

    public SignupAssert isValidationError() {
        Allure.step("Проверяем некорректный запрос (400) и сообщение");
        String resp = response.extract().asString();
        Allure.addAttachment("Ответ с ошибкой (400)", "application/json", resp, ".json");
        response.statusCode(400)
                .body("status", equalTo("error"))
                .body("errors", not(anEmptyMap())); // errors — это map c полями
        return this;
    }

}
