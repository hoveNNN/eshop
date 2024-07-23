package com.example.ecomerce.shop.dao.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.UserRole;


public interface UserRepository extends JpaRepository<User,Long> {
Optional <User> findFirstByEmail(String email);
User findByRole(UserRole userRole);

}