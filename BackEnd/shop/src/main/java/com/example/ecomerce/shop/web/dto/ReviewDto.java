package com.example.ecomerce.shop.web.dto;

import lombok.Data;

@Data
public class ReviewDto {

    private Long id;

    private Long rating;

    private String description;

    private Long userId;

    private Long productId;

    private String userName;

    private byte[] returnedImg; // Add this field if you need to return image data

}
