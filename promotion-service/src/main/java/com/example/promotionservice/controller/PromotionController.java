package com.example.promotionservice.controller;

import com.example.promotionservice.service.PromotionPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionPublisher promotionPublisher;

    public PromotionController(PromotionPublisher promotionPublisher) {
        this.promotionPublisher = promotionPublisher;
    }

    @PostMapping("/{productId}/publish-update")
    public Map<String, Object> publishUpdate(
            @PathVariable Long productId) {

        promotionPublisher.publishProductUpdate(productId);

        return Map.of(
                "message", "Đã gửi thông báo cập nhật khuyến mãi",
                "product_id", productId
        );
    }
}