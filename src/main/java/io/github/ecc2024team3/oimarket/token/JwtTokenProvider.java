package io.github.ecc2024team3.oimarket.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    //  JWT 서명 키 (반드시 256비트 이상)
    private final String secretKey = "your-very-secret-key-your-very-secret-key";

    //  Key 타입의 secretKey 초기화
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    private final UserDetailsService userDetailsService;

    private final Map<String, Boolean> tokenBlacklist = Collections.synchronizedMap(new HashMap<>());

    //  JWT 생성
    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        // 1시간
        long validityInMilliseconds = 3600000;
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)  // Key 타입 사용
                .compact();
    }

    //  JWT에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) //  Key 타입 사용
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //  resolveToken() 수정 (static 제거)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값 반환
        }
        return null;
    }

    // 사용자 인증(Authentication) 객체 생성
    public Authentication getAuthentication(String token) {
        String email = getEmail(token); //  토큰에서 사용자 이메일(아이디) 추출
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //  JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        if (tokenBlacklist.containsKey(token)) {
            return false; // 로그아웃된 토큰은 무효화
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

