package com.example.ecomerce.shop.business.services;

import com.example.ecomerce.shop.web.dto.SignupRequest;
import com.example.ecomerce.shop.web.dto.UserDto;

public interface AuthService {

    public UserDto  createUser(SignupRequest signupRequest);
    public  Boolean hasUserWithEmail(String email );
}