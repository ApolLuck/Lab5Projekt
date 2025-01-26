package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Promotion;
import com.example.lab2projekt.domain.repositories.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> findAllPromotions() {
        return promotionRepository.findAll();
    }
}
