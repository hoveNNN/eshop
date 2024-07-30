package com.example.ecomerce.shop.business.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CategoryService;
import com.example.ecomerce.shop.dao.entity.Category;
import com.example.ecomerce.shop.dao.repository.CategoryRepository;
import com.example.ecomerce.shop.web.dto.CategoryDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final  CategoryRepository categoryRepository;
    public Category createCategory( CategoryDto categoryDto){
        Category category= new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return  categoryRepository.save(category);
    }
    public List <Category> getAllCategories(){
        return  categoryRepository.findAll();
    }
}