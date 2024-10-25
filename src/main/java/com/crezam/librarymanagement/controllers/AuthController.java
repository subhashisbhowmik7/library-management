package com.crezam.librarymanagement.controllers;


import com.crezam.librarymanagement.dtos.LoginRequest;
import com.crezam.librarymanagement.services.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  
    @Autowired
    private MemberService memberService;

    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String response = memberService.loginUser(loginRequest);
        return ResponseEntity.ok(response);

    }
}
