package io.github.ecc2024team3.oimarket.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Service
public class RedisUtil {
    private final StringRedisTemplate template;

    public RedisUtil(StringRedisTemplate template) {
        this.template =template;
    }

    //  데이터 저장 메서드 추가
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        valueOperations.set(key, value);
    }

    //  TTL(시간 만료) 적용한 데이터 저장
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    //  데이터 가져오기
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    //  데이터 존재 여부 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    //  데이터 삭제
    public void deleteData(String key) {
        template.delete(key);
    }

    public void addToBlacklist(String token, long expiration) {
        // 만료 시간 설정 후 Redis에 저장
        template.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return template.hasKey(token);
    }

}

