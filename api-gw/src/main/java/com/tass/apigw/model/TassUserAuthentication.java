package com.tass.apigw.model;

import com.tass.apigw.security.UserDetailExtend;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class TassUserAuthentication extends UsernamePasswordAuthenticationToken {

    public TassUserAuthentication(UserDetailExtend userDetailExtend  ) {
        super(userDetailExtend, null , new ArrayList<>());
    }
    public TassUserAuthentication(UserDetailExtend userDetailExtend, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(userDetailExtend, credentials, authorities);
    }
}
