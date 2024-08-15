package com.example.ecomerce.shop.web.controller.customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.CartService;
import com.example.ecomerce.shop.exceptions.ValidationException;
import com.example.ecomerce.shop.web.dto.AddProductInCartDto;
import com.example.ecomerce.shop.web.dto.OrderDto;
import com.example.ecomerce.shop.web.dto.PlaceOrderDto;

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
    @GetMapping("/cart/{userId}")
    public ResponseEntity<?>addProductToCart(@PathVariable Long userId){
        OrderDto orderDto= cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @GetMapping("/coupon/{userId}/{code}")
public ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String code) {
    try {
        OrderDto orderDto = cartService.applyCoupon(userId, code);
        return ResponseEntity.ok(orderDto);
    } catch (ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

@PostMapping("/addition")
public ResponseEntity<?>increaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto){
    
    return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductInCartDto));
}
@PostMapping("/decreasing")
public ResponseEntity<?>decreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto){
    
    return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decreaseProductQuantity(addProductInCartDto));
}

@PostMapping("/placeOrder")
public ResponseEntity<?>placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
    
    return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
}

@GetMapping("/myOrders/{userId}")
public ResponseEntity<List<OrderDto>>getMyPlaceOrders(@PathVariable Long userId){
    
    return ResponseEntity.ok(cartService.getMyPlaceOrders(userId));
}


}