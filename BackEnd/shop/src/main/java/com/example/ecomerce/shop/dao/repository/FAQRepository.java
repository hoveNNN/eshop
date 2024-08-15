package com.example.ecomerce.shop.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.FAQ;

public interface FAQRepository  extends JpaRepository<FAQ,Long>{
    List<FAQ> findAllByProductId(Long productId);
}