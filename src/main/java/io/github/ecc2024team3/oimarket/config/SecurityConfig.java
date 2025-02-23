package io.github.ecc2024team3.oimarket.config;

import io.github.ecc2024team3.oimarket.token.JwtAuthenticationFilter;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import io.github.ecc2024team3.oimarket.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
//import org.springframework.web.cors.CorsConfiguration;
//스프링 시큐리티는 웹 사이트의 콘텐츠가 다른 사이트에 포함되지 않도록 X-Frame-Options헤더의 기본값을 deny함. 이를 수정할 예정

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    //  생성자 주입 추가
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.cors(AbstractHttpConfigurer::disable) // CORS 설정 해제 -> 필요시 주석 풀고 쓰세요
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //  세션 사용 안 함 (JWT 기반)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/force-verified").permitAll()
                        .requestMatchers("/api/users/signup", "/api/users/login", "/h2-console/**").permitAll()
                        //.requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }))
                // JwtAuthenticationFilter 생성 시 2개의 매개변수를 넣어줌
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisUtil), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}