package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import com.example.lab2projekt.domain.Objects.Entities.OrderItem;
import com.example.lab2projekt.domain.Objects.Entities.OrderStatus;
import com.example.lab2projekt.domain.repositories.OrderItemRepository;
import com.example.lab2projekt.domain.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public Order getOrderByCookie(String UserCookie){
        return orderRepository.findByUserSessionCookie(UserCookie);
    }

    public Optional<Order> getOrderById(Order order){
        Long id = order.getId();
        return orderRepository.findById(id);
    }


    public void processOrderCreation(Map<String, String> params, String totalOrderValue, String userCookies) {
        // Tworzenie nowego zamówienia
        Order order = new Order();
        order.setTotalPrice(BigDecimal.valueOf(Double.parseDouble(totalOrderValue)));
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> updatedOrderItems = new ArrayList<>();

        params.forEach((key, value) -> {
            if (key.startsWith("items")) {
                // Parsowanie kluczy w formacie "items[1].pizzaId"
                String[] keyParts = key.split("\\.");
                if (keyParts.length < 2) return;

                String fieldName = keyParts[1]; // pizzaId, quantity, price
                Long orderItemId = Long.valueOf(key.substring(6, key.indexOf(']')));

                // Pobieramy istniejący OrderItem z bazy
                OrderItem orderItem = orderItemRepository.findById(Math.toIntExact(orderItemId))
                        .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + orderItemId));

                // Aktualizacja wartości na podstawie przesłanych danych
                switch (fieldName) {
                    case "quantity":
                        orderItem.setQuantity(Integer.valueOf(value));
                        break;
                    case "price":
                        orderItem.setPrice(BigDecimal.valueOf(Double.parseDouble(value)));
                        break;
                }

                // Dodanie zaktualizowanego OrderItem do listy
                if (!updatedOrderItems.contains(orderItem)) {
                    updatedOrderItems.add(orderItem);
                }
            }
        });

        // Zapis zaktualizowanych OrderItems
        orderItemRepository.saveAll(updatedOrderItems);

        // Przypisanie zaktualizowanych OrderItems do zamówienia jako Set
        order.setOrderItems(new HashSet<>(updatedOrderItems));

        order.setOrderStatus(OrderStatus.PENDING);
        order.setUserSessionCookie(userCookies);

        // Zapis zamówienia do bazy
        orderRepository.save(order);
    }

    public void updateOrderEmail(Map<String, String> params, Order order) {
        String email = params.get("email");

        Long orderId = order.getId();
        Optional<Order> searchedOrder = orderRepository.findById(orderId);
        Order findedOrder = searchedOrder.orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia"));

        findedOrder.setClientEmail(email);
        orderRepository.save(findedOrder);


    }
}
