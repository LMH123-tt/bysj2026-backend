package com.ruoyi.content.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CacheMessageListener
{
    private static final Logger log = LoggerFactory.getLogger(CacheMessageListener.class);

    static final String CACHE_INVALIDATE_CHANNEL = "content:cache:invalidate";

    @Autowired
    private MultiLevelCache multiLevelCache;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init()
    {
        if (redisTemplate == null)
        {
            log.info("RedisTemplate not available, cache invalidation listener disabled");
            return;
        }
        try
        {
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(redisTemplate.getConnectionFactory());
            container.addMessageListener(new MessageListener()
            {
                @Override
                public void onMessage(Message message, byte[] pattern)
                {
                    try
                    {
                        byte[] body = message.getBody();
                        if (body != null && body.length > 0)
                        {
                            String key = new String(body);
                            log.debug("Received cache invalidation: {}", key);
                            String localKey = key.replace("content:cache:", "");
                            if (localKey.endsWith(":*"))
                            {
                                multiLevelCache.invalidateLocalByPattern(localKey.substring(0, localKey.length() - 2));
                            }
                            else
                            {
                                multiLevelCache.invalidateLocal(localKey);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        log.warn("Error processing cache invalidation message: {}", e.getMessage());
                    }
                }
            }, new ChannelTopic(CACHE_INVALIDATE_CHANNEL));
            container.afterPropertiesSet();
            container.start();
            log.info("Cache invalidation listener started on channel: {}", CACHE_INVALIDATE_CHANNEL);
        }
        catch (Exception e)
        {
            log.warn("Failed to start cache invalidation listener, falling back to full L1 invalidation: {}", e.getMessage());
        }
    }
}
