package io.github.ecc2024team3.oimarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "https://localhost:3000",
            "https://*.oimarket.com",
            "https://*.vercel.app",
            "http://oimarket-backend.ap-northeast-2.elasticbeanstalk.com",
            "https://oimarket-backend.ap-northeast-2.elasticbeanstalk.com",
            // 하나씩 추가해야 하는 건지 실험 중
            "http://oimarket-backend.ap-northeast-2.elasticbeanstalk.com/api/posts"
        ));
        config.setAllowedOrigins(List.of("*")); //get 오류를 해결하기 위해 추가
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization")); // JWT 같은 인증 정보 노출 가능
        config.setAllowCredentials(true); //get 오류를 해결하기 위해 추가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 전체 경로 적용

        return source;
    }
}
