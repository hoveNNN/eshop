package com.example.ecomerce.shop.web.dto;

import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;

    // Constructor to initialize UserDto from User entity
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getUserName();
        this.userRole = user.getRole(); // Assuming User entity has a getRole() method
    }
}
