package com.danish.taskmanager.config;

import com.danish.taskmanager.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AuthService userDetailsService;

    public SecurityConfig(AuthService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registerUserForm").permitAll()
                        .requestMatchers("/users").hasAnyRole("ADMIN", "MEMBER", "MANAGER")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/auth/loginForm")
                        .loginProcessingUrl("/auth/login") //The url is set here Spring Security treats THIS URL as the login endpoint
                        .usernameParameter("email")          // tell Spring to read 'email' field
                        .passwordParameter("password")
                        .defaultSuccessUrl("/users", true)
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")                // URL to trigger logout
                        .logoutSuccessUrl("/auth/loginForm") // redirect after logout
                        .invalidateHttpSession(true)         // invalidate session
                        .deleteCookies("JSESSIONID")         // remove cookies
                        .permitAll()
                );
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}