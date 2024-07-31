package com.example.ecomerce.shop.web.dto;


import lombok.Data;

@Data
public class AddProductInCartDto {
    private Long userId;
    private Long productId;
}