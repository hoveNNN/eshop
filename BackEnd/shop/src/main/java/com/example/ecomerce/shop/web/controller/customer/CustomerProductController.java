package com.example.ecomerce.shop.web.controller.customer;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.CustomerProductService;
import com.example.ecomerce.shop.web.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerProductController {
    private final  CustomerProductService customerProductService;

     @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = customerProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/products/search")
public ResponseEntity<List<ProductDto>> getAllProductByName(@RequestParam String name) {
    List<ProductDto> productDtos = customerProductService.getProductByName(name);
    return ResponseEntity.ok(productDtos);
}
}