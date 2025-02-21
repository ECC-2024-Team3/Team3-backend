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
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:3000",
            "https://localhost:3000",
            "https://*.oimarket.com",
            "https://*.vercel.app",
            "http://oimarket-backend.ap-northeast-2.elasticbeanstalk.com",
            "https://oimarket-backend.ap-northeast-2.elasticbeanstalk.com",
            // 하나씩 추가해야 하는 건지 실험 중
            "http://oimarket-backend.ap-northeast-2.elasticbeanstalk.com/api/posts"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}
