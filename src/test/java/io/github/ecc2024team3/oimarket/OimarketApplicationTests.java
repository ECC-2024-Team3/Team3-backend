package io.github.ecc2024team3.oimarket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // ✅ 테스트 실행 시 H2 사용
class OimarketApplicationTests {

    @Test
    void contextLoads() {
        // ✅ 애플리케이션이 정상적으로 실행되는지 확인하는 기본 테스트
    }
}