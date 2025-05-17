package org.example.apitests;

import io.restassured.http.ContentType;
import org.example.apitests.assertions.SignupAssert;
import org.example.apitests.extension.RestAssuredBaseExtension;
import org.example.apitests.extension.UserRegistrationExtension;
import org.example.apitests.model.request.SignupRequest;
import org.example.apitests.testutil.UserRegistrationFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@ExtendWith({RestAssuredBaseExtension.class, UserRegistrationExtension.class})

class AuthSignupTest {

    static final String SIGNUP_URL = "/auth/signup";

    @Test
    void signupSuccess() {
        var req = UserRegistrationFactory.valid();

        new SignupAssert(
                given().contentType(ContentType.JSON).body(req)
                        .when().post(SIGNUP_URL).then()
        ).isSuccess();
    }

    @Test
    void signupDuplicateUsername(SignupRequest existingUser) {
        // Создаем нового юзера с таким же username, но другим email
        SignupRequest dupe = UserRegistrationFactory.withUsername(existingUser.getUsername());

        new SignupAssert(
                given().contentType(ContentType.JSON).body(dupe)
                        .when().post(SIGNUP_URL).then()
        ).isConflict("Username already exists");
    }

    @ParameterizedTest(name = "[{index}] Негативная регистрация: {0}")
    @MethodSource("invalidUsers")
    void signupInvalid(String scenario, SignupRequest req) {
        new SignupAssert(
                given().contentType(ContentType.JSON).body(req)
                        .when().post(SIGNUP_URL).then()
        ).isValidationError();
    }

    static Stream<Arguments> invalidUsers() {
        return Stream.of(
                Arguments.of("Пустой объект", UserRegistrationFactory.empty()),
                Arguments.of("Пустой email", UserRegistrationFactory.withEmail("")),
                Arguments.of("Короткий пароль", UserRegistrationFactory.withPassword("12")),
                Arguments.of("Некорректный email", UserRegistrationFactory.withEmail("bad_email"))
        );
    }
}
