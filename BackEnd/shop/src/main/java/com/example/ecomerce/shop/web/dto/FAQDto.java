package com.example.ecomerce.shop.web.dto;

import lombok.Data;

@Data
public class FAQDto {
    private Long Id;
    private String question;
    private String answer;
    private Long productId;
}