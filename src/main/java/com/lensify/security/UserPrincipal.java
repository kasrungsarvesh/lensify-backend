package com.lensify.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lensify.entity.User;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long userId,
                         String username,
                         String password,
                         boolean active,
                         Collection<? extends GrantedAuthority> authorities) {

        this.userId = userId;
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());

        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                Collections.singletonList(authority)
        );
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}