package com.crezam.librarymanagement.services;

import com.crezam.librarymanagement.dtos.LoginRequest;
import com.crezam.librarymanagement.entities.*;
import com.crezam.librarymanagement.repositories.*;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {

    @Autowired
    private final MemberRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public UserServiceImpl(MemberRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder= new BCryptPasswordEncoder();
    }

    public String registerUser(@Valid Member user) {
        String password = user.getPassword();
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(@Valid LoginRequest loginRequest) {
        Member existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser == null) {
            throw new ValidationException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new ValidationException("Invalid password");
        }

        MembershipStatus mem= existingUser.getMembershipStatus();
        return jwtService.generateToken(existingUser.getEmail(), mem);
    }

    public List<Member> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Member> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public String updateUser(Long id, @Valid Member user) {
        Optional<Member> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new ValidationException("User not found");
        }
        Member existingUser = existingUserOpt.get();
        existingUser.setName(user.getName());
        existingUser.setContactNumber(user.getContactNumber());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && user.getPassword().length() >= 8) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
        return "User updated successfully";
    }

    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("User not found");
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}