package com.example.ecomerce.shop.business.services;

import java.util.List;

import com.example.ecomerce.shop.web.dto.ProductDetailDto;
import com.example.ecomerce.shop.web.dto.ProductDto;

public interface CustomerProductService {
          public List<ProductDto> getAllProducts() ;
          public List<ProductDto> getProductByName(String name);
          ProductDetailDto getProductDetailId(Long productId);
}