package com.crezam.librarymanagement.controllers;


import com.crezam.librarymanagement.entities.Member;
import com.crezam.librarymanagement.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // Get all members - Only ACTIVE members can access this
    @PreAuthorize("hasRole('ACTIVE')")
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // Get member by ID
    @PreAuthorize("hasRole('ACTIVE')")
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    // Register new member (no authorization needed)
    @PostMapping
    public ResponseEntity<Member> addMember(@Valid @RequestBody Member member) {
        Member savedMember = memberService.addMember(member);
        return ResponseEntity.ok(savedMember);
    }

    // Update member details - Only ACTIVE members
    @PreAuthorize("hasRole('ACTIVE')")
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(id, member);
        return ResponseEntity.ok(updatedMember);
    }

    // Delete a member - Only ACTIVE members
    @PreAuthorize("hasRole('ACTIVE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
