package com.example.productservice.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class PromotionUpdateSubscriber implements MessageListener {

    private final CacheManager cacheManager;

    public PromotionUpdateSubscriber(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String productIdText = new String(message.getBody()).trim();

        try {
            Long productId = Long.parseLong(productIdText);

            Cache cache = cacheManager.getCache("products");

            if (cache != null) {
                cache.evict(productId);

                System.out.println(
                        "Đã xóa cache sản phẩm ID = " + productId
                                + " do cập nhật khuyến mãi"
                );
            }

        } catch (NumberFormatException e) {
            System.err.println(
                    "product_id không hợp lệ: " + productIdText
            );
        }
    }
}