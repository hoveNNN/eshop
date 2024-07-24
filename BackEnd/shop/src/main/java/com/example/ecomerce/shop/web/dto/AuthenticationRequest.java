package com.example.ecomerce.shop.web.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}