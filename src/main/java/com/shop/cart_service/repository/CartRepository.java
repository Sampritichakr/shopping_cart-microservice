package com.shop.cart_service.repository;

import com.shop.cart_service.model.CartItem;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveCassandraRepository<CartItem, MapId> {
    // Spring Boot automatically implements the reactive save(), find(), and delete() methods!
}