package com.ncc.manage_emp.service;

import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println(usernameOrEmail + "________________");
        Optional<Users> user = Optional.ofNullable(userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email")));
        System.out.println(user.get().getGoogleId() + "111____________");
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.get().getRoleName()));

        if (user.get().getGoogleId() != null) {
            return new org.springframework.security.core.userdetails.User(
                    usernameOrEmail,
                    "",
                    authorities
            );
        }
        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.get().getPassword(),
                authorities
        );
    }
}
