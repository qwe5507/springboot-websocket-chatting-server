package com.chat.developchatserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class WebSecurityConfig {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin() // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제한다.
                .and()
                .formLogin() // 권한없이 페이지 접근하면 로그인 페이지로 이동한다.
//                .successForwardUrl("/chat/room")
                .and()
                .authorizeRequests()
                .antMatchers("/chat/**").hasRole("USER") // chat으로 시작하는 리소스에 대한 접근 권한 설정
                .anyRequest().permitAll();


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        BCryptPasswordEncoder bCryptPasswordEncoder = passwordEncoder();

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("test")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        userDetailsManager.createUser(User.withUsername("tes")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        userDetailsManager.createUser(User.withUsername("guest")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("GUEST")
                .build());
        return userDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }

}
