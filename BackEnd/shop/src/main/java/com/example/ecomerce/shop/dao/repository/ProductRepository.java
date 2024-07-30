package com.example.ecomerce.shop.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByName(String Name);
}