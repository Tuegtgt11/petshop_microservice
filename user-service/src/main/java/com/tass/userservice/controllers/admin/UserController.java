package com.tass.userservice.controllers.admin;

import com.tass.common.customanotation.RequireUserLogin;
import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.userservice.entities.Role;
import com.tass.userservice.entities.User;
import com.tass.userservice.entities.myenum.UserStatus;
import com.tass.userservice.model.request.UserRequest;
import com.tass.userservice.services.UserService;
import com.tass.userservice.spec.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequireUserLogin
    @GetMapping()
    public BaseResponseV2 findAllUser(@RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "fullName", required = false) String fullName,
                                      @RequestParam(value = "phone", required = false) String phone,
                                      @RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "gender", required = false) Integer gender,
                                      @RequestParam(value = "address", required = false) String address,
                                      @RequestParam(value = "status", required = false) UserStatus status,
                                      @RequestParam(value = "role", required = false) Role role) throws ApplicationException{
        Specification<User> specification = UserSpec.userSpec(username, fullName, phone, email, gender, address, role, status);
        return userService.findAllUser(specification);
    }


    @RequireUserLogin
    @GetMapping("/{id}")
    public BaseResponseV2<UserDTO> findUserById(@PathVariable Long id) throws ApplicationException {
        return userService.findUserById(id);
    }

    @RequireUserLogin
    @PutMapping ("/block/{id}")
    public BaseResponseV2 blockUser(@PathVariable Long id) throws ApplicationException {
        return userService.blockUser(id);
    }

    @RequireUserLogin
    @GetMapping("/active/{id}")
    public BaseResponseV2 activeUser(@PathVariable Long id) throws ApplicationException {
        return userService.activeUser(id);
    }

    @RequireUserLogin
    @DeleteMapping("/id")
    public BaseResponseV2 deteteUser(@PathVariable Long id) throws ApplicationException {
        return  userService.deleteUser(id);
    }
}
