package io.github.ecc2024team3.oimarket.config;

import io.github.ecc2024team3.oimarket.token.JwtAuthenticationFilter;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.context.annotation.Profile;


@Configuration
@EnableWebSecurity
//@Profile("!test")
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfigurationSource corsConfigurationSource; //  CORS 적용을 위해 추가

    // 없으면 cors 적용이 안 됨.
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CorsConfigurationSource corsConfigurationSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // 없으면 cors 적용이 안 됨.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 세션 사용 안 함 (JWT 기반)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/singup", "/api/users/login", "/h2-console/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/403")) // 403 예외 핸들링
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)));

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