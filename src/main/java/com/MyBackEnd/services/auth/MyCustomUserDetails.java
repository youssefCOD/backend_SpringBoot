package com.MyBackEnd.services.auth;

import org.springframework.security.core.GrantedAuthority;
import com.MyBackEnd.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyCustomUserDetails implements UserDetails {

    private User user;

    public MyCustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public int getUserId() {
        return this.user.getUserId();
    }

    public String getFirstName() {
        return this.user.getFirstName();
    }

    public String getLastName() {
        return this.user.getLastName();
    }

    public int getColor() {
        return this.user.getColor();
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
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
