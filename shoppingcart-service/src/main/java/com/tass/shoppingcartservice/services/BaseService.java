package com.tass.shoppingcartservice.services;

import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.utils.ThreadLocalCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

public class BaseService {

    public UserDTO getUserDTO(){
        return ThreadLocalCollection.getUserActionLog();
    }
}
