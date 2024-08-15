package com.example.ecomerce.shop.business.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.AuthService;
import com.example.ecomerce.shop.dao.entity.Order;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.OrderStatus;
import com.example.ecomerce.shop.dao.enums.UserRole;
import com.example.ecomerce.shop.dao.repository.OrderRepository;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.SignupRequest;
import com.example.ecomerce.shop.web.dto.UserDto;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    public UserDto createUser(SignupRequest signupRequest) {
        if (hasUserWithEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUserName(signupRequest.getUserName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.Customer);

        User createdUser = userRepository.save(user);

        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);

        return new UserDto(createdUser);
    }

    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
     @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.Admin);
        if (adminAccount == null) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setUserName("admin");
            user.setRole(UserRole.Admin);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }
}
