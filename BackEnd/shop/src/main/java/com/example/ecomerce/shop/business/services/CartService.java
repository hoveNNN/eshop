package com.example.ecomerce.shop.business.services;

import org.springframework.http.ResponseEntity;

import com.example.ecomerce.shop.web.dto.AddProductInCartDto;

public interface CartService {
     public ResponseEntity<?> addProductInCart(AddProductInCartDto addProductInCartDto);
}