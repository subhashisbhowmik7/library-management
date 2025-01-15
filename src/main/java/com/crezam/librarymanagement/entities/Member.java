package com.crezam.librarymanagement.entities;


import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Getter
@Setter

public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Contact number is mandatory")
    private String contactNumber;

    @NotBlank(message = "password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role membershipStatus=Role.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       var status =  new SimpleGrantedAuthority("ROLE_" + this.membershipStatus.name());
        return List.of(status);
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    
//     @Override
//     public boolean isAccountNonLocked() {
//     return this.membershipStatus == MembershipStatus.USER;
// }

@Override
public boolean isAccountNonExpired() {
    return true; // All accounts are non-expired for now.
}

@Override
public boolean isAccountNonLocked() {
    return true; // Adjust logic for locked accounts if needed.
}

@Override
public boolean isCredentialsNonExpired() {
    return true; // All credentials are non-expired for now.
}

@Override
public boolean isEnabled() {
    return true; // Adjust logic for account activation if required.
}
//     @Override
//     public boolean isEnabled() {
//     return this.membershipStatus == MembershipStatus.ADMIN;
// }

}

