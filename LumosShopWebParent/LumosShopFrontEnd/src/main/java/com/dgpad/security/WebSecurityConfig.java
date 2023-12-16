package com.dgpad.security;

import com.dgpad.security.oauth.ClientOAuth2Service;
import com.dgpad.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private ClientOAuth2Service clientOAuth2Service;
    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler();
    }

    @Bean
    public SystemLoginSuccessHandler systemLoginSuccessHandler() {
        return new SystemLoginSuccessHandler();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerDetailsService();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        http.authorizeHttpRequests(request ->
                request
                        .requestMatchers("/customers","/bag","/my-account","/addresses","/orders/**","/review/**",
                                "/share-review/**").authenticated()
                        .anyRequest().permitAll()
        );
        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("email")
                        .successHandler(systemLoginSuccessHandler())
                        .permitAll()
        );

        http.oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                httpSecurityOAuth2LoginConfigurer.loginPage("/login")
                .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
                        .userService(clientOAuth2Service))
                .successHandler(oAuth2LoginSuccessHandler())
        );

        http.rememberMe((rememberMe) -> rememberMe
                .key("YaHussain_2023")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .userDetailsService(userDetailsService())
        );
        return (SecurityFilterChain) http.build();

    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/images/**", "/css/**", "/webjars/**","/js/**");
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();


        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }
}

