package io.github.ecc2024team3.oimarket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "spring.profiles.active=test"
)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class OimarketApplicationTests {
    @Test
    void contextLoads() {
    }
}