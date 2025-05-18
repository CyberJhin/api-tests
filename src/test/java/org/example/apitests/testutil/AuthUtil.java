package org.example.apitests.testutil;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthUtil {
    public static String getAccessToken(String username, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .port(8081)
                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                .post("/auth/signin")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("accessToken");
    }
}
