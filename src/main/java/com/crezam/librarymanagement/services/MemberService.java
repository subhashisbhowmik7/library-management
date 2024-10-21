package com.crezam.librarymanagement.services;


import com.crezam.librarymanagement.entities.Member;
import com.crezam.librarymanagement.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
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
