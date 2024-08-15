package com.example.ecomerce.shop.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.Review;

public interface ReviewRepository extends JpaRepository <Review,Long>{
    List<Review> findAllByProductId(Long productId);
}