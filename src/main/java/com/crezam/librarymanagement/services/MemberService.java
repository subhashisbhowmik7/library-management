package com.crezam.librarymanagement.services;


import com.crezam.librarymanagement.dtos.LoginRequest;
import com.crezam.librarymanagement.entities.Member;
import com.crezam.librarymanagement.repositories.MemberRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService  {

    private MemberRepository memberRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository userRepository, JwtService jwtService) {
        this.memberRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public Member registerMember(@Valid Member user) {
        String password = user.getPassword();
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (memberRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(password));
        Member savedUser=memberRepository.save(user);
        return savedUser;
    }

        public String loginUser(@Valid LoginRequest loginRequest) {
        Member existingUser = memberRepository.findByEmail(loginRequest.getEmail());
        if (existingUser == null) {
            throw new ValidationException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new ValidationException("Invalid password");
        }
        return jwtService.generateToken(existingUser.getUsername(), existingUser.getMembershipStatus());
    }

    

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    // Updated to Register Member
    // public Member addMember(Member member) {
    //     return memberRepository.save(member);
    // }

    public Member updateMember(Long id, Member member) {
        Optional<Member> existingUserOpt = memberRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new ValidationException("User not found");
        }
        Member existingUser = existingUserOpt.get();
        existingUser.setName(member.getName());
        existingUser.setContactNumber(member.getContactNumber());
        existingUser.setEmail(member.getEmail());
        if (member.getPassword() != null && member.getPassword().length() >= 8) {
            existingUser.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        Member saved= memberRepository.save(existingUser);
        return saved;
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
