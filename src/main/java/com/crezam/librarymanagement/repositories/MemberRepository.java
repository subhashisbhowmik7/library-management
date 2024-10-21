package com.crezam.librarymanagement.repositories;


import com.crezam.librarymanagement.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    public Member findByEmail(String email);

}
