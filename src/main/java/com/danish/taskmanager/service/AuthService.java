package com.danish.taskmanager.service;

import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {


    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Can use repository OR service
            User user = userRepository.findByEmail(email).orElseThrow(() ->
                            new UsernameNotFoundException("User not found"));
        System.out.println(user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())) // can also set ROLE_ADMIN in enum
        );
    }
}