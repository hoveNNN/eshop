package com.example.ecomerce.shop.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.Category;

public interface CategoryRepository  extends JpaRepository<Category,Long>{
    
}