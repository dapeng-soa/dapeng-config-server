package com.github.dapeng.config;

import com.github.dapeng.web.system.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;
import javax.servlet.Filter;


/**
 * @author with struy.
 * Create by 2018/6/8 00:27
 * email :yq1724555319@gmail.com
 * 启用WebSecurity
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomerFilterSecurityInterceptor customerFilterSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(customerFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http.headers().frameOptions().disable();
        http.csrf().disable().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/me")
                .and()
                .logout()
                .and()
                .authorizeRequests()
                .accessDecisionManager(customerFilterSecurityInterceptor.getAccessDecisionManager())
                .anyRequest()
                .authenticated();

    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new Md5PasswordEncoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/plugins/**", "/css/**", "/js/**", "/images/**","/");
    }

}
