package com.example.promotionservice.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PromotionPublisher {

    private static final String CHANNEL = "promotion-updates";

    private final StringRedisTemplate redisTemplate;

    public PromotionPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishProductUpdate(Long productId) {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "product_id phải là số dương"
            );
        }

        redisTemplate.convertAndSend(
                CHANNEL,
                String.valueOf(productId)
        );

        System.out.println(
                "Đã gửi product_id = " + productId
                        + " lên channel " + CHANNEL
        );
    }
}