package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CHANNELS_BY_USER = "channelsByUser";
    public static final String NOTIFICATIONS_BY_USER = "notificationsByUser";
    public static final String USERS_ALL = "usersAll";

    @Bean
    @Primary
    public CacheManager compositeCacheManager() {

        log.debug("복합 캐시 매니저 초기화");

        CompositeCacheManager composite = new CompositeCacheManager();

        composite.setCacheManagers(
            List.of(
                channelCacheManager(),
                notificationCacheManager(),
                userCacheManager()
            )
        );

        composite.setFallbackToNoOpCache(false);

        log.debug("복합 캐시 매니저 설정 완료");

        return composite;
    }

    @Bean
    public CacheManager channelCacheManager() {

        log.debug("채널 캐시 매니저 설정 시작");

        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(Duration.ofMinutes(1))
                .recordStats()
                .removalListener((key, value, cause) -> {
                    switch (cause) {
                        case SIZE:
                            log.debug("Category 캐시 크기 초과로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPIRED:
                            log.debug("Category 캐시 만료로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPLICIT:
                            log.info("Category 캐시 수동 삭제로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case REPLACED:
                            log.debug("Category 캐시 새 값으로 교체로 인한 엔트리 제거 - key: {}", key);
                            break;
                        default:
                            log.debug("Category 캐시 엔트리 제거 - key: {}, cause: {}", key, cause);
                    }
                })
        );

        manager.setCacheNames(List.of(CHANNELS_BY_USER));

        log.debug("채널 캐시 매니저 설정 완료");

        return manager;
    }

    @Bean
    public CacheManager notificationCacheManager() {

        log.debug("알림 캐시 매니저 설정 시작");

        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(Duration.ofSeconds(10))
                .recordStats()
                .removalListener((key, value, cause) -> {
                    switch (cause) {
                        case SIZE:
                            log.debug("Category 캐시 크기 초과로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPIRED:
                            log.debug("Category 캐시 만료로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPLICIT:
                            log.info("Category 캐시 수동 삭제로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case REPLACED:
                            log.debug("Category 캐시 새 값으로 교체로 인한 엔트리 제거 - key: {}", key);
                            break;
                        default:
                            log.debug("Category 캐시 엔트리 제거 - key: {}, cause: {}", key, cause);
                    }
                })

        );

        manager.setCacheNames(List.of(NOTIFICATIONS_BY_USER));

        log.debug("알림 캐시 매니저 설정 완료");

        return manager;
    }

    @Bean
    public CacheManager userCacheManager() {

        log.debug("사용자 캐시 매니저 설정 시작");

        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(Duration.ofSeconds(10))
                .recordStats()
                .removalListener((key, value, cause) -> {
                    switch (cause) {
                        case SIZE:
                            log.debug("Category 캐시 크기 초과로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPIRED:
                            log.debug("Category 캐시 만료로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case EXPLICIT:
                            log.info("Category 캐시 수동 삭제로 인한 엔트리 제거 - key: {}", key);
                            break;
                        case REPLACED:
                            log.debug("Category 캐시 새 값으로 교체로 인한 엔트리 제거 - key: {}", key);
                            break;
                        default:
                            log.debug("Category 캐시 엔트리 제거 - key: {}, cause: {}", key, cause);
                    }
                })
        );

        manager.setCacheNames(List.of(USERS_ALL));

        log.debug("사용자 캐시 매니저 설정 완료");

        return manager;
    }
}
