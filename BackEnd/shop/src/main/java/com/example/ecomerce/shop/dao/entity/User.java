package com.example.ecomerce.shop.dao.entity;

import com.example.ecomerce.shop.dao.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String userName;
    private String email;
    private String password;
    private UserRole role;

    @Lob
    @Column(columnDefinition="Longblob")
    private byte[]img;
    
}