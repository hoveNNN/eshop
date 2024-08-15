package com.example.ecomerce.shop.business.services;

import com.example.ecomerce.shop.web.dto.OrderedProductsResponseDto;
import com.example.ecomerce.shop.web.dto.ReviewDto;

public interface ReviewService {
    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long  orderId);
    ReviewDto giveReview(ReviewDto reviewDto);
}