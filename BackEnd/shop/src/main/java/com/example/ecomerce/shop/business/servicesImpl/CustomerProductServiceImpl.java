package com.example.ecomerce.shop.business.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CustomerProductService;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.web.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl  implements CustomerProductService  {

    private final ProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByName(String name) {
        List<Product> products = productRepository.findByName(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
}