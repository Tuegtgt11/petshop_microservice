package com.tass.userservice.model.request;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String rePassword;
    private String avatar;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private int gender;
}
