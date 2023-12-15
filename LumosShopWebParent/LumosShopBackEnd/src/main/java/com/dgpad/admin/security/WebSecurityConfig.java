package com.dgpad.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class  WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new LumosUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/users/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**").hasAnyAuthority("Admin", "Content Manager")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin", "product Manager", "Content Manager", "Customer Service")
                        .requestMatchers("/customers/**","/Analyses/**").hasAnyAuthority("Admin", "product Manager")
                        .requestMatchers("/shipping/**").hasAnyAuthority("Admin", "product Manager")
                        .requestMatchers("/orders/**").hasAnyAuthority("Admin", "product Manager", "Customer Service")
                        .requestMatchers("/menus/**").hasAnyAuthority("Admin", "Content Manager")
                        .requestMatchers("/review/**").hasAnyAuthority("Admin", "Content Manager","Customer Service")
                        .requestMatchers("/controlCenter/**").hasAuthority("Admin"));

        http.authorizeHttpRequests((requests) -> {
            requests.anyRequest().authenticated();

        });

        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("email").permitAll()
        );

        http.logout(LogoutConfigurer::permitAll);

        http.rememberMe((rememberMe) -> rememberMe
                .key("YaAbaaasii_2023")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .userDetailsService(userDetailsService()));

        http.headers(httpSecurityHeadersConfigurer ->
                httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        return (SecurityFilterChain) http.build();

    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/images/**", "/css/**", "/webjars/**", "/js/**");
    }


}

