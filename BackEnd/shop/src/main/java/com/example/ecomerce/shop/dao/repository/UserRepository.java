package com.example.ecomerce.shop.dao.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecomerce.shop.dao.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {
Optional <User> findFirstByEmail(String email);

}