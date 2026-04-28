package com.ruoyi.content.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ruoyi.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MultiLevelCache
{
    private static final Logger log = LoggerFactory.getLogger(MultiLevelCache.class);

    private static final String CACHE_PREFIX = "content:cache:";

    private static final String NULL_CACHE = "NULL";

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private ContentBloomFilter bloomFilter;

    private final Cache<String, Object> localCache = Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .recordStats()
            .build();

    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);

    private final ReentrantLock lock = new ReentrantLock();

    public <T> T get(String key, Class<T> clazz, CacheLoader<T> loader)
    {
        return get(key, clazz, loader, 300L, 30L);
    }

    public <T> T get(String key, Class<T> clazz, CacheLoader<T> loader, long redisTtlSeconds, long localTtlSeconds)
    {
        if (bloomFilter != null && key.startsWith("detail:"))
        {
            String idStr = key.substring("detail:".length());
            try
            {
                Long contentId = Long.parseLong(idStr);
                if (!bloomFilter.mightContain(contentId))
                {
                    missCount.incrementAndGet();
                    return null;
                }
            }
            catch (NumberFormatException ignored) {}
        }

        Object localValue = localCache.getIfPresent(key);
        if (localValue != null)
        {
            hitCount.incrementAndGet();
            if (NULL_CACHE.equals(localValue))
            {
                return null;
            }
            log.debug("L1 cache hit: {}", key);
            @SuppressWarnings("unchecked")
            T result = (T) localValue;
            return result;
        }

        Object redisValue = redisService.getCacheObject(CACHE_PREFIX + key);
        if (redisValue != null)
        {
            hitCount.incrementAndGet();
            if (NULL_CACHE.equals(redisValue))
            {
                localCache.put(key, NULL_CACHE);
                return null;
            }
            log.debug("L2 cache hit: {}", key);
            localCache.put(key, redisValue);
            @SuppressWarnings("unchecked")
            T result = (T) redisValue;
            return result;
        }

        lock.lock();
        try
        {
            redisValue = redisService.getCacheObject(CACHE_PREFIX + key);
            if (redisValue != null)
            {
                hitCount.incrementAndGet();
                if (NULL_CACHE.equals(redisValue))
                {
                    localCache.put(key, NULL_CACHE);
                    return null;
                }
                localCache.put(key, redisValue);
                @SuppressWarnings("unchecked")
                T result = (T) redisValue;
                return result;
            }

            missCount.incrementAndGet();
            T result = loader.load();
            if (result != null)
            {
                long jitter = (long) (Math.random() * 60);
                redisService.setCacheObject(CACHE_PREFIX + key, result, redisTtlSeconds + jitter, TimeUnit.SECONDS);
                localCache.put(key, result);
            }
            else
            {
                redisService.setCacheObject(CACHE_PREFIX + key, NULL_CACHE, 60L, TimeUnit.SECONDS);
                localCache.put(key, NULL_CACHE);
            }
            return result;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void evict(String key)
    {
        if (localCache.estimatedSize() > 0 && localCache.getIfPresent(key) != null)
        {
            evictionCount.incrementAndGet();
        }
        localCache.invalidate(key);
        redisService.deleteObject(CACHE_PREFIX + key);
        publishInvalidate(CACHE_PREFIX + key);
        log.debug("Cache evicted: {}", key);
    }

    public void evictByPattern(String pattern)
    {
        java.util.Collection<String> keys = redisService.keys(CACHE_PREFIX + pattern);
        if (keys != null && !keys.isEmpty())
        {
            redisService.deleteObject(keys);
        }
        localCache.invalidateAll();
        publishInvalidate(CACHE_PREFIX + pattern + ":*");
        log.debug("Cache evicted by pattern: {}", pattern);
    }

    public void invalidateLocal(String key)
    {
        if (localCache.getIfPresent(key) != null)
        {
            localCache.invalidate(key);
            log.debug("Local cache invalidated by pub/sub: {}", key);
        }
    }

    public void invalidateLocalByPattern(String patternPrefix)
    {
        localCache.invalidateAll();
        log.debug("Local cache invalidated by pub/sub pattern: {}", patternPrefix);
    }

    private void publishInvalidate(String key)
    {
        try
        {
            if (redisTemplate != null)
            {
                redisTemplate.convertAndSend(CacheMessageListener.CACHE_INVALIDATE_CHANNEL, key);
            }
        }
        catch (Exception e)
        {
            log.warn("Failed to publish cache invalidation: {}", e.getMessage());
        }
    }

    public void clear()
    {
        localCache.invalidateAll();
        java.util.Collection<String> keys = redisService.keys(CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty())
        {
            redisService.deleteObject(keys);
        }
        hitCount.set(0);
        missCount.set(0);
        evictionCount.set(0);
        log.info("All content cache cleared");
    }

    public LocalCacheStats getStats()
    {
        long hit = hitCount.get();
        long miss = missCount.get();
        long total = hit + miss;
        double rate = total > 0 ? (double) hit / total : 0.0;
        return new LocalCacheStats(
                hit,
                miss,
                rate,
                localCache.estimatedSize(),
                evictionCount.get()
        );
    }

    @FunctionalInterface
    public interface CacheLoader<T>
    {
        T load();
    }

    public static class LocalCacheStats
    {
        private final long hitCount;
        private final long missCount;
        private final double hitRate;
        private final long size;
        private final long evictionCount;

        public LocalCacheStats(long hitCount, long missCount, double hitRate, long size, long evictionCount)
        {
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.hitRate = hitRate;
            this.size = size;
            this.evictionCount = evictionCount;
        }

        public long getHitCount() { return hitCount; }
        public long getMissCount() { return missCount; }
        public double getHitRate() { return hitRate; }
        public long getSize() { return size; }
        public long getEvictionCount() { return evictionCount; }
    }
}
