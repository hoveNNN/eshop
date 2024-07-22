package com.example.ecomerce.shop.web.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecomerce.shop.configs.JwtUtil;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.AuthenticationRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getWriter().write(new JSONObject().put("error", "Incorrect username or password").toString());
            } catch (JSONException jsonException) {
                response.getWriter().write("{\"error\":\"Incorrect username or password\"}");
            }
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        Optional<User> user = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (user.isPresent()) {
            try {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("userId", user.get().getId());
                jsonResponse.put("role", user.get().getRole());

                response.getWriter().write(jsonResponse.toString());
                response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
            } catch (JSONException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"JSON processing error\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getWriter().write(new JSONObject().put("error", "User not found").toString());
            } catch (JSONException e) {
                response.getWriter().write("{\"error\":\"User not found\"}");
            }
        }
    }
}
