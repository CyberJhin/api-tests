package org.example.apitests.testutil;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class GraphQLUtil {

    public static ValidatableResponse graphql(String query, Object variables, String token) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .contentType("application/json")
                .port(8081)
                .header("Authorization", "Bearer " + token)
                .body(new GraphQLRequest(query, variables))
                .when()
                .post("/graphql")
                .then();
    }

    // Вспомогательный внутренний класс для запроса
    public static class GraphQLRequest {
        public String query;
        public Object variables;

        public GraphQLRequest(String query, Object variables) {
            this.query = query;
            this.variables = variables;
        }
    }
}
