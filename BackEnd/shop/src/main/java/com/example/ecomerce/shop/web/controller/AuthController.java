package com.example.ecomerce.shop.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.configs.JwtUtil;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.UserRole;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.AuthenticationRequest;
import com.example.ecomerce.shop.web.dto.SignupRequest;
import com.example.ecomerce.shop.web.dto.UserDto;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"error\":\"Incorrect username or password\"}");
        }
    
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> user = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
    
        if (user.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            headers.add("Access-Control-Expose-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
    
            UserDto userDto = new UserDto(user.get());
            return ResponseEntity.ok()
                .headers(headers)
                .body(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"error\":\"User not found\"}");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
    
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Ensure password is encoded
        user.setUserName(signupRequest.getName());
        user.setRole(UserRole.Customer); // Assuming default role is USER
        userRepository.save(user);
    
        UserDto userDto = new UserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
