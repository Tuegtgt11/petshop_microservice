package com.tass.apigw.security;


import com.tass.common.redis.repository.UserLoginRepository;
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

    private UserLoginRepository userLoginRepository;

    public WebSecurity(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/noauth/**", "/user/register", "/login", "/admin/product", "/admin/product/{id}");
    }

    @Override


    protected void configure(HttpSecurity http) throws Exception {

        HttpSecurity httpSercurity = http.headers().disable()
                .cors()
                .and()
                .requestCache().disable()
                .csrf().disable().authorizeRequests()
                .and();

        BasicAuthenticationFilter filter = new Oauth2AuthorizationFilter(authenticationManager(), userLoginRepository);
        httpSercurity.addFilterBefore(filter, BasicAuthenticationFilter.class).sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

        http.authorizeRequests()
                .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
    public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(401);
            res.getWriter().write("AuthorFailResponse.toJson()");
        }
    }

}
