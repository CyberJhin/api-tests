package org.example.apitests.extension;


import org.example.apitests.model.request.SignupRequest;
import org.example.apitests.testutil.UserRegistrationFactory;
import org.junit.jupiter.api.extension.*;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserRegistrationExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return pc.getParameter().getType().equals(SignupRequest.class);
    }

    @Override
    public Object resolveParameter(ParameterContext pc, ExtensionContext ec) {
        SignupRequest req = UserRegistrationFactory.valid();
        given().contentType(ContentType.JSON).body(req)
                .when().post("/auth/signup").then().statusCode(201);
        return req;
    }
}
