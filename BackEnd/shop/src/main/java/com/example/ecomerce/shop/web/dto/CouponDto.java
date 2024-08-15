package com.example.ecomerce.shop.web.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CouponDto {
      private Long id;
     private  String name;
     private String code;
     private Long  discount;
     private Date  expirationDate;

     
}