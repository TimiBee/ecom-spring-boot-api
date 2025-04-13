package com.ecommerce.controller;

import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null && loginRequest.getPassword().equals(user.getPassword())) {
            return jwtTokenUtil.generateToken(user.getUsername());
        } else {
            return "Invalid username or password";
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken";
        }

        user.setRoles(Set.of("ROLE_USER")); // default role for new users
        userRepository.save(user);

        return "User registered successfully";
    }
}
