# API Tests

## О проекте

**API Tests** — это учебный Java-проект, созданный для демонстрации навыков программирования на Java и автоматизации тестирования современного backend-приложения. Проект реализован с использованием Spring Boot, поддерживает тестирование REST и GraphQL API, а также включает примеры работы с безопасностью, валидацией и взаимодействием с базой данных.

## Назначение

Данный репозиторий предназначен для:
- Показа навыков разработки на Java (Spring Boot, JPA, JWT, GraphQL);
- Демонстрации умений автоматизировать тестирование (JUnit 5, Rest-assured);
- Примера организации кода, тестов и сопутствующей инфраструктуры;
- Быстрого старта для написания собственных API-тестов.

## Технологии и зависимости

- Java 17
- Spring Boot (web, security, data-jpa, validation, graphql)
- PostgreSQL (через Spring Data JPA)
- JWT (io.jsonwebtoken)
- Webjars (graphiql)
- Lombok
- JUnit 5
- Rest-assured
- Instancio
- Gradle

Все зависимости настроены в файле `build.gradle`.

## Структура проекта

```
.
├── build.gradle
├── src/
│   ├── main/
│   │   ├── java/           # Основной код приложения (контроллеры, сервисы и т.д.)
│   │   └── resources/
│   └── test/
│       └── java/
│           └── org/
│               └── example/
│                   └── apitests/
│                       ├── ApiTestsApplicationTests.java
│                       ├── AuthSignupTest.java
│                       ├── assertions/    # Пользовательские ассерты
│                       ├── extension/     # Расширения для тестов
│                       └── testutil/      # Вспомогательные классы для тестов
```

## Примеры тестов

- `AuthSignupTest.java` — пример теста регистрации пользователя.
- `ApiTestsApplicationTests.java` — базовые интеграционные тесты приложения.
- Каталоги `assertions`, `extension`, `testutil` содержат вспомогательные классы, расширяющие возможности тестирования.

## Как запустить

1. Клонируйте репозиторий:
    ```sh
    git clone https://github.com/CyberJhin/api-tests.git
    cd api-tests
    ```
2. Соберите проект и запустите тесты:
    ```sh
    ./gradlew test
    ```
3. Для запуска приложения (если требуется):
    ```sh
    ./gradlew bootRun
    ```

> Для работы тестов с Testcontainers требуется докер.


## Контакты

Автор: [CyberJhin](https://github.com/CyberJhin)