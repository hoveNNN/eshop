package com.example.ecomerce.shop.web.controller.customer;


import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.ReviewService;
import com.example.ecomerce.shop.web.dto.OrderedProductsResponseDto;
import com.example.ecomerce.shop.web.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class ReviewController {
    private final  ReviewService reviewService;

    @GetMapping("/ordered-products/{orderId}")
     public  ResponseEntity<OrderedProductsResponseDto> getOrderedProductsDetailsById(@PathVariable Long  orderId){
        return ResponseEntity.ok(reviewService.getOrderedProductsDetailsByOrderId(orderId));
     }

     @PostMapping("/review")
     public ResponseEntity<ReviewDto> giveReview(@ModelAttribute ReviewDto reviewDto) throws IOException {
      ReviewDto savedReview = reviewService.giveReview(reviewDto);
      if (savedReview == null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
  }
}