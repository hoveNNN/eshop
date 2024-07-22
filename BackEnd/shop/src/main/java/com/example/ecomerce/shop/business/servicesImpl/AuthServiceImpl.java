package com.example.ecomerce.shop.business.servicesImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.AuthService;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.UserRole;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.SignupRequest;
import com.example.ecomerce.shop.web.dto.UserDto;

@Service
public class AuthServiceImpl implements AuthService {
        @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDto  createUser(SignupRequest signupRequest){
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUserName(signupRequest.getName());
        user.setPassword( new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.Customer);
        User createdUser = userRepository.save(user);
        UserDto userDto= new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }
    public  Boolean hasUserWithEmail(String email ){
        return userRepository.findFirstByEmail(email).isPresent();
    }
}