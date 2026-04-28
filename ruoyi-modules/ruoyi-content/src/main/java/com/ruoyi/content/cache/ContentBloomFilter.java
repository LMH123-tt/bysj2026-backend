package com.ruoyi.content.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ContentBloomFilter
{
    private static final Logger log = LoggerFactory.getLogger(ContentBloomFilter.class);

    private static final String BLOOM_KEY = "content:bloom:ids";
    private static final int BITMAP_SIZE = 1000000;

    private static final int[] SEEDS = {3, 7, 11, 13, 31, 37, 61};

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public void put(Long contentId)
    {
        if (contentId == null || redisTemplate == null) return;
        for (int seed : SEEDS)
        {
            long hash = hash(contentId, seed);
            redisTemplate.opsForValue().setBit(BLOOM_KEY, hash, true);
        }
    }

    public boolean mightContain(Long contentId)
    {
        if (contentId == null || redisTemplate == null) return true;
        for (int seed : SEEDS)
        {
            long hash = hash(contentId, seed);
            Boolean bit = redisTemplate.opsForValue().getBit(BLOOM_KEY, hash);
            if (bit == null || !bit)
            {
                return false;
            }
        }
        return true;
    }

    private long hash(Long contentId, int seed)
    {
        long h = contentId * seed;
        h = h ^ (h >>> 16);
        return Math.abs(h) % BITMAP_SIZE;
    }
}
