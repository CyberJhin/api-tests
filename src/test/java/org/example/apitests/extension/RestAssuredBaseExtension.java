package org.example.apitests.extension;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class RestAssuredBaseExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
    }
}
