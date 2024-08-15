package com.example.ecomerce.shop.business.services;

import java.util.List;

import com.example.ecomerce.shop.web.dto.OrderDto;

public interface AdminOrderService {
    public List<OrderDto> getAllplacedOrders();
    public OrderDto changeOrderStatus(Long orderId,String status);
}