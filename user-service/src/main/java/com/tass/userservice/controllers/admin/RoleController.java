package com.tass.userservice.controllers.admin;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.dto.user.RoleDTO;
import com.tass.userservice.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public BaseResponseV2 findAllRole() throws ApplicationException {
        return roleService.finAllRole();
    }

    @GetMapping("/{id}")
    public BaseResponseV2<RoleDTO> findRoleById(@PathVariable Long id) throws ApplicationException {
        return roleService.findById(id);
    }
}
