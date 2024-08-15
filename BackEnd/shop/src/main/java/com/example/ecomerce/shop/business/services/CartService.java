package com.example.ecomerce.shop.business.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.ecomerce.shop.web.dto.AddProductInCartDto;
import com.example.ecomerce.shop.web.dto.OrderDto;
import com.example.ecomerce.shop.web.dto.PlaceOrderDto;

public interface CartService {
      ResponseEntity<?> addProductInCart(AddProductInCartDto addProductInCartDto);
      OrderDto getCartByUserId(Long userId);

     OrderDto applyCoupon (long userId,String code);
      OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
      OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);
      OrderDto placeOrder(PlaceOrderDto placeOrderDto);
       List<OrderDto>getMyPlaceOrders(Long userId);
}