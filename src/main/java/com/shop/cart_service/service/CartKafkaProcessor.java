package com.shop.cart_service.service;

import com.shop.cart_service.model.CartItem;
import com.shop.cart_service.model.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.shop.cart_service.repository.CartRepository;
@Service
public class CartKafkaProcessor {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CartRepository cartRepository;

    // We inject BOTH Kafka and our Astra DB Repository here
    public CartKafkaProcessor(KafkaTemplate<String, Object> kafkaTemplate, CartRepository cartRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.cartRepository = cartRepository;
    }

    @KafkaListener(topics = "order-requests", groupId = "cart-group")
    public void processOrderAndManageCart(OrderEvent event) {
        System.out.println("\n[CART SERVICE] 📥 Picked up order request: " + event.getOrderId());

        // This is a perfect Reactive pipeline!
        saveToCartDatabase(event)
                .flatMap(this::publishCartConfirmation)
                .subscribe(
                        success -> System.out.println("[CART SERVICE] 📤 Confirmation sent back to Order Service."),
                        error -> System.err.println("[CART SERVICE] ❌ Error: " + error.getMessage())
                );
    }

    private Mono<OrderEvent> saveToCartDatabase(OrderEvent event) {
        // 1. Create the database entity
        CartItem item = new CartItem(event.getUserId(), event.getProductId(), event.getQuantity());

        System.out.println("-> Saving Product " + event.getProductId() + " to User " + event.getUserId() + "'s cart in Astra DB.");

        // 2. Save it using the reactive repository
        return cartRepository.save(item)
                .map(savedItem -> {
                    // 3. Once saved, update the event status and pass it down the chain
                    event.setStatus("CART_UPDATE_SUCCESS");
                    return event;
                });
    }

    private Mono<Void> publishCartConfirmation(OrderEvent event) {
        // Publish to the 'cart-events' topic so the Order Service hears it
        return Mono.fromFuture(kafkaTemplate.send("cart-events", event.getOrderId(), event)).then();
    }
}