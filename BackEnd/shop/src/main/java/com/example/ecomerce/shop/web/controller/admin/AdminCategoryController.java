package com.example.ecomerce.shop.web.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.business.services.CategoryService;
import com.example.ecomerce.shop.dao.entity.Category;
import com.example.ecomerce.shop.web.dto.CategoryDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCategoryController {
    private static final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);
    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto){
        logger.info("Received request to create category: {}", categoryDto.getName());
        Category category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        logger.info("Received request to fetch all categories");
        List<Category> categories = categoryService.getAllCategories();
        logger.info("Returning categories: {}", categories);
        return ResponseEntity.ok(categories);
    }
}
