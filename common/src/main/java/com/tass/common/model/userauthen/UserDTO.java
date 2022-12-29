package com.tass.common.model.userauthen;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO {
    private String token;
    private long userId;
}
