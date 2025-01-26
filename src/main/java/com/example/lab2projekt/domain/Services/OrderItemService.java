package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.OrderItem;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.repositories.OrderItemRepository;
import com.example.lab2projekt.domain.repositories.PizzaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final PizzaRepository pizzaRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, PizzaRepository pizzaRepository) {
        this.orderItemRepository = orderItemRepository;
        this.pizzaRepository = pizzaRepository;
    }

    public void createOrderItem(Integer quantity, BigDecimal orderValue, Integer pizzaId){
        Optional<Pizza> optionalPizza = pizzaRepository.findById(pizzaId);
        // Sprawdź, czy znaleziono pizzę
        if (optionalPizza.isEmpty()) {
            throw new IllegalArgumentException("Pizza with ID " + pizzaId + " not found.");
        }
        Pizza pizza = optionalPizza.get();
        BigDecimal price = orderValue.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);

        OrderItem orderItem = new OrderItem();
        orderItem.setPizza(pizza);
        orderItem.setPrice(price);
        orderItem.setQuantity(quantity);
        orderItemRepository.save(orderItem);
    }

    public List<OrderItem> findAllOrderItems() { return orderItemRepository.findAll(); }
}
