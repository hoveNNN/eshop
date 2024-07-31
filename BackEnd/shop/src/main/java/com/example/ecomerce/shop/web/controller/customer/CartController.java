package com.example.ecomerce.shop.web.controller.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.CartService;
import com.example.ecomerce.shop.web.dto.AddProductInCartDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("/cart")
    public ResponseEntity<?>addProductInCart(@RequestBody AddProductInCartDto addProductInCartDto){
        return cartService.addProductInCart(addProductInCartDto);
    }
}