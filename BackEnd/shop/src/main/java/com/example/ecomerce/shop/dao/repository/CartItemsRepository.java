package com.example.ecomerce.shop.dao.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
    Optional<CartItems> findByProductIdAndOrderIdAndUserId (Long prouctId, Long orderId,Long userId );
}