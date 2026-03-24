package cires.bemodule.models;

import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// TODO : implement boolean methods
public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {this.user = user;}

    // TODO : return list.of() or collection of roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        // TODO : add permissions
//                for (Role role : user.getRoles()) {
//            for (Permission permission : role.getPermissions()) {
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            }
//            authorities.add(new SimpleGrantedAuthority("Role" + role.getRoleName()));
//        }
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getAccountStatus() == AccountStatus.ACTIVE;
        //return UserDetails.super.isEnabled();
    }
}
