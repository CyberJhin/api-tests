package org.example.apitests;

import org.example.apitests.extension.GraphQLAuthExtension;
import org.example.apitests.controller.StudioController.StudioInput;
import org.example.apitests.testutil.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.*;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Map;

@Epic("GraphQL")
@Feature("Studio")
@ExtendWith(GraphQLAuthExtension.class)
class StudioGraphQLTest {

    @Test
    @DisplayName("Успешное создание студии (GraphQL)")
    @Description("Тест на создание студии через GraphQL с использованием Bearer токена")
    void createStudioSuccess(@Name("bearerToken") String bearerToken) {
        StudioInput studioInput = StudioFactory.valid();

        ValidatableResponse response = GraphQLUtil.graphql(
                GraphQLQueries.CREATE_STUDIO,
                Map.of("input", studioInput),
                bearerToken
        );

        response
                .statusCode(200)
                .body("data.createStudio.id", notNullValue())
                .body("data.createStudio.name", equalTo(studioInput.getName()))
                .body("data.createStudio.country", equalTo(studioInput.getCountry()));

        Allure.step("Проверили успешное создание студии");
    }
}