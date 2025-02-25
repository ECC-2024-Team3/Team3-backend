package io.github.ecc2024team3.oimarket.token;

import io.github.ecc2024team3.oimarket.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;//  의존성 주입
    private final RedisUtil redisUtil;

    //  생성자 주입을 사용해서 RedisUtil 가져오기
    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/users/signup") || path.startsWith("/api/users/login") || path.startsWith("/api/users/force-verified?emali=test11@example.com") // 회원가입 & 로그인은 필터 제외
        ||path.startsWith("/api/comments/post/"); //댓글 조회는 필터 제외
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 헤더에서 토큰 받아오기
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        //  토큰이 유효한 경우, 인증 객체 생성 후 SecurityContext에 저장
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //  블랙리스트 체크 추가
            if (redisUtil.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted");
                return;
            }

            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }


}

