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

@Service
public class MemberService {

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

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, Member member) {
        Member existingMember = getMemberById(id);
        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setContactNumber(member.getContactNumber());
        existingMember.setMembershipStatus(member.getMembershipStatus());
        return memberRepository.save(existingMember);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
