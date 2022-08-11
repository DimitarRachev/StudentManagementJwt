package com.example.studentmanagmentrest.configuration;

import com.example.studentmanagmentrest.filter.CustomAuthenticationFilter;

import com.example.studentmanagmentrest.filter.CustomAuthorizationFilter;
import com.example.studentmanagmentrest.service.UserService;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenGenerator tokenGenerator;
    private final AuthenticationConfiguration auth;


    public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService, TokenGenerator tokenGenerator, AuthenticationConfiguration auth) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.auth = auth;
    }



    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/refreshToken", "/generate", "/register", "/register/**" )
                .permitAll()
                .antMatchers("/*")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(auth), tokenGenerator));
        http.addFilterBefore(new CustomAuthorizationFilter(tokenGenerator), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setUserDetailsService(userService);
//        return provider;
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration auth) throws Exception {
//        return super.authenticationManagerBean();
        return auth.getAuthenticationManager();
    }
}
