package org.example.apitests.testutil;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class CleanDatabaseExtension implements BeforeEachCallback {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void beforeEach(ExtensionContext context) {
        // Например, очищаем все таблицы между тестами
        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    }
}
