package com.github.dapeng.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author with struy.
 * Create by 2018/6/8 00:27
 * email :yq1724555319@gmail.com
 * 启用WebSecurity
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("1234")
                .roles("USER", "ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/webapp/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/security_login")
                .successForwardUrl("/")
                .permitAll()
                .failureForwardUrl("/security_login")
                .and()
                .authorizeRequests()
                .antMatchers( "/config/**").hasRole("USER")
                .antMatchers("/monitor", "/monitor/**").hasRole("USER")
                .antMatchers("/deploy", "/deploy/**").hasRole("USER")
                .and()
                .logout()
                .logoutSuccessUrl("/login");
    }

}
