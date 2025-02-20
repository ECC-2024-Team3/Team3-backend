package io.github.ecc2024team3.oimarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    // 없으면 cors 적용이 안 됨.
    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            // 없으면 cors 적용이 안 됨.
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}

// 아래는 기존 코드. 실행이 됐고 400 오류와 500 오류 모두 뜨지 않으나, 403 오류가 뜸.
// package io.github.ecc2024team3.oimarket.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.List;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
        
//         config.setAllowCredentials(true);
//         config.setAllowedOriginPatterns(List.of(
//             "http://localhost:[0-9]+",
//             "https://*.oimarket.com",
//             "https://*.vercel.app",
//             "http://oimarket-backend.ap-northeast-2.elasticbeanstalk.com",
//             "https://oimarket-backend.ap-northeast-2.elasticbeanstalk.com"
//         ));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/api/**", config);

//         return source;
//     }
// }