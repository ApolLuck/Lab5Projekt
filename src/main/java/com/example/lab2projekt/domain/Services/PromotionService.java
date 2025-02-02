package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Promotion;
import com.example.lab2projekt.domain.repositories.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> findAllPromotions() {
        return promotionRepository.findAll();
    }


    public Optional<Promotion> findPromotionById(Integer id) {
        return promotionRepository.findById(Long.valueOf(id));
    }

    public Promotion savePromotion(Promotion promotion) {
        if (promotion.getPromotionCode() != null) {
            promotion.setPromotionCode(promotion.getPromotionCode().toUpperCase());
        }
        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Integer id) {
        promotionRepository.deleteById(Long.valueOf(id));
    }

}
