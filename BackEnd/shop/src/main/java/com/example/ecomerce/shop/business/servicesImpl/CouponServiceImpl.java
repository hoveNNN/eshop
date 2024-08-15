package com.example.ecomerce.shop.business.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CouponService;
import com.example.ecomerce.shop.dao.entity.Coupon;
import com.example.ecomerce.shop.dao.repository.CouponRepository;
import com.example.ecomerce.shop.exceptions.ValidationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final  CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon){
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new ValidationException("Coupon code already exist");
        }
        return couponRepository.save(coupon);
    }

    public List<Coupon>getAllCoupons(){
        return couponRepository.findAll();
    }
}