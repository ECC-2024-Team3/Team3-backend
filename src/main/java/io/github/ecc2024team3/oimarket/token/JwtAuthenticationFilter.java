package io.github.ecc2024team3.oimarket.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;


public class JwtAuthenticationFilter extends GenericFilterBean {
    private final io.github.ecc2024team3.oimarket.token.JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(io.github.ecc2024team3.oimarket.token.JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 기본 생성자 추가
    public JwtAuthenticationFilter() {
        this.jwtTokenProvider = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 헤더에서 토큰 받아오기
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // ✅ 토큰이 유효한 경우, 인증 객체 생성 후 SecurityContext에 저장
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 Filter 실행
        chain.doFilter(request, response);
    }
}



