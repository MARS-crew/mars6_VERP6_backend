package mars_6th.VER6.domain.minio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisFileService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 데이터 저장 (파일명 저장 예제)
    public void saveFileName(Long userId, String fileName) {
        redisTemplate.opsForValue().set("pendingFile:" + userId, fileName, 10, TimeUnit.MINUTES);
    }

    // 데이터 조회 (파일명 조회)
    public String getFileName(Long userId) {
        return (String) redisTemplate.opsForValue().get("pendingFile:" + userId);
    }

    // 데이터 삭제 (파일명 삭제)
    public void deleteFileName(Long userId) {
        redisTemplate.delete("pendingFile:" + userId);
    }
}
