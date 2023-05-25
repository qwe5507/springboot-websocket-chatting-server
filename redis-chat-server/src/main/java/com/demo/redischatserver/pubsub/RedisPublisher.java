package com.demo.redischatserver.pubsub;

import com.demo.redischatserver.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/*
    Pub/Sub 메커니즘에서는 구독하든 발행하든 둘 중 어느 쪽을 사용하더라도 채널이 생성되고 Redis에서 관리됩니다
 */
@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}