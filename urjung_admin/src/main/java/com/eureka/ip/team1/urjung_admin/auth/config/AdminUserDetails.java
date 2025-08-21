package com.eureka.ip.team1.urjung_admin.auth.config;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class AdminUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final Admin admin;

    private final Collection<? extends GrantedAuthority> authorities;

    @Override public String getUsername() { return admin.getEmail(); }
    @Override public String getPassword() {
        System.out.println("로그 UserDetail 비밀번호 반환 : " +admin.getPassword());
        return admin.getPassword();
    }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
