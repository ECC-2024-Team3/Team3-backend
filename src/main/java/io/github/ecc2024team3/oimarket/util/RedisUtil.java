package io.github.ecc2024team3.oimarket.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final StringRedisTemplate template;

    public String getData(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Redis key cannot be null or empty");
        }
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) {
        if (key == null || key.isBlank()) {
            return false;  // 존재하지 않는 것으로 처리
        }
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value, long duration) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Redis key cannot be null or empty");
        }
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        if (key == null || key.isBlank()) {
            return; // key가 null이면 삭제할 필요 없음
        }
        template.delete(key);
    }
}

