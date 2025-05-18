package org.example.apitests.assertions;

import io.qameta.allure.Allure;
import org.springframework.graphql.test.tester.GraphQlTester;

public class StudioAssert {
    private final GraphQlTester.Response response;

    public StudioAssert(GraphQlTester.Response response) {
        this.response = response;
    }

    public StudioAssert isCreated(String expectedName, String expectedCountry) {
        Allure.step("Проверяем что студия создана");
        response.path("createStudio").hasValue();
        response.path("createStudio.name").entity(String.class).isEqualTo(expectedName);
        response.path("createStudio.country").entity(String.class).isEqualTo(expectedCountry);
        return this;
    }

    public StudioAssert isValidationError() {
        Allure.step("Проверяем ошибку валидации");
        response.errors().satisfy(errors -> {
            Allure.addAttachment("Ошибка GraphQL", errors.toString());
        });
        return this;
    }
}
