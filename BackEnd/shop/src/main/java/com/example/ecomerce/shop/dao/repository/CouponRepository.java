package com.example.ecomerce.shop.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.Coupon;

public interface CouponRepository  extends JpaRepository<Coupon,Long>{
    boolean existsByCode(String code);
    Optional <Coupon>findByCode(String code);
}