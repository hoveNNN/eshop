package com.example.ecomerce.shop.business.services;

import java.util.List;

import com.example.ecomerce.shop.dao.entity.Coupon;

public interface CouponService {
    public Coupon createCoupon(Coupon coupon);
     public List<Coupon>getAllCoupons();
}