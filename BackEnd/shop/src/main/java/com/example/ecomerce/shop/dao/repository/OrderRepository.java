package com.example.ecomerce.shop.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.Order;
import com.example.ecomerce.shop.dao.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
    List<Order> findAllByOrderStatusIn(List<OrderStatus>orderStatusList);
    List <Order> findByUserIdAndOrderStatusIn(Long userId, List <OrderStatus> orderStatus);

}