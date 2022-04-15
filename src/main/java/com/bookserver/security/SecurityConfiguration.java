package com.bookserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration {

    @Configuration
    //@Order(1)
    public static class ApiConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .antMatcher("/oauth/authorize")
                .httpBasic()
                .and()
                .csrf().disable();
        }
    }

}
