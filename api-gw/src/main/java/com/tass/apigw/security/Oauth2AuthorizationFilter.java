package com.tass.apigw.security;

import com.tass.apigw.model.TassUserAuthentication;
import com.tass.apigw.utils.HttpUtil;
import com.tass.common.model.constans.AUTHENTICATION;
import com.tass.common.redis.dto.UserLoginDTO;
import com.tass.common.redis.repository.UserLoginRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Oauth2AuthorizationFilter extends BasicAuthenticationFilter {

    UserLoginRepository userLoginRepository;
    public Oauth2AuthorizationFilter(
        AuthenticationManager authenticationManager , UserLoginRepository userLoginRepository) {
        super(authenticationManager);

        this.userLoginRepository = userLoginRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // lay ra token


        String token = HttpUtil.getValueFromHeader(request, AUTHENTICATION.HEADER.TOKEN);

        if (StringUtils.isBlank(token)){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Optional<UserLoginDTO> userLoginDTO = userLoginRepository.findById(token);

        if (userLoginDTO.isEmpty()){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        try {
            UserLoginDTO userLoginDTOObject = userLoginDTO.get();

            UserDetailExtend userDetailExtend = new UserDetailExtend(userLoginDTOObject);


            TassUserAuthentication tassUserAuthentication = new TassUserAuthentication(userDetailExtend, null, getRoles(userDetailExtend.getRole()));

            SecurityContextHolder.getContext().setAuthentication(tassUserAuthentication);
            chain.doFilter(request, response);
        } catch (AccessDeniedException ex){
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);

            JSONObject rs = new JSONObject();
            rs.put("code" , 99);
            rs.put("msg" , "Không có quyền truy cập");
            response.getWriter().write(rs.toJSONString());
            chain.doFilter(request, response);
            return;
        }

    }

    private List<GrantedAuthority> getRoles(String role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.isBlank(role))
            return authorities;

        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}
