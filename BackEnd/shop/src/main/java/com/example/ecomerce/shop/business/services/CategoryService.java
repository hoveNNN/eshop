package com.example.ecomerce.shop.business.services;

import java.util.List;

import com.example.ecomerce.shop.dao.entity.Category;
import com.example.ecomerce.shop.web.dto.CategoryDto;

public interface CategoryService {
    public Category createCategory( CategoryDto categoryDto);
     public List <Category> getAllCategories();
}