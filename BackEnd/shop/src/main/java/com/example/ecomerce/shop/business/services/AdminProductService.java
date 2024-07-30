package com.example.ecomerce.shop.business.services;

import java.io.IOException;
import java.util.List;

import com.example.ecomerce.shop.web.dto.ProductDto;

public interface AdminProductService {
     public  ProductDto addProduct (ProductDto productDto) throws IOException;
     public List<ProductDto> getAllProducts();
     public List<ProductDto> getProductByName(String name) ;
     public void deleteProduct(Long id) ;
}