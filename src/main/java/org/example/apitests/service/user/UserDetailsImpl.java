package org.example.apitests.service.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.apitests.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String uuid;
    private String username;
    private String password;
    private String email;


    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getUuid(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return true;
    }
}
