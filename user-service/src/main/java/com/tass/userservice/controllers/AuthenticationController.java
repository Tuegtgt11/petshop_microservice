package com.tass.userservice.controllers;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.redis.dto.UserLoginDTO;
import com.tass.userservice.model.request.LoginRequest;
import com.tass.userservice.model.request.UserRequest;
import com.tass.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public BaseResponseV2<UserLoginDTO> login(@RequestBody LoginRequest loginRequest) throws
        ApplicationException{
        return userService.login(loginRequest);
    }
    @PostMapping("/user/register")
    public BaseResponseV2 register(@RequestBody UserRequest request) throws ApplicationException {
        return userService.register(request);
    }

     @PutMapping("/user/update/{id}")
    public BaseResponseV2<UserDTO> updateAccount(@RequestBody UserRequest userRequest, Principal principal, @PathVariable Long id) throws ApplicationException {
        return userService.updateAccount(userRequest, principal, id);
     }
}
