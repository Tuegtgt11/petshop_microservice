package com.tass.apigw.security;


import com.tass.common.redis.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Override
    public void configure(
            org.springframework.security.config.annotation.web.builders.WebSecurity web)
            throws Exception {
        web.ignoring().antMatchers( "/user/register" , "/login",
                "/admin/product", "/admin/product/{id}","/admin/brand", "/admin/brand/{id}",
                "/admin/size", "/admin/size/{id}","/admin/category", "/admin/category/{id}");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        HttpSecurity httpSecurity = http.headers().disable()
                .cors()
                .and()
                .requestCache().disable()
                .csrf().disable().authorizeRequests().and();

        BasicAuthenticationFilter filter = new Oauth2AuthorizationFilter(authenticationManager() , userLoginRepository);
        httpSecurity.addFilterBefore(filter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling();

        http.authorizeRequests().anyRequest().authenticated();
    }

}
