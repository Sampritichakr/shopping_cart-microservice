package com.shop.cart_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("cart_items")
@Data // Generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor // Required by Spring Data Cassandra
@AllArgsConstructor // Required to easily create new items in our KafkaProcessor
public class CartItem {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String userId;

    @PrimaryKeyColumn(name = "product_id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private String productId;

    @Column("quantity")
    private int quantity;

}