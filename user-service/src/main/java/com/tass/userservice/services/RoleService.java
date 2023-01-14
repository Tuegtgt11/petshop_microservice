package com.tass.userservice.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.dto.user.RoleDTO;
import com.tass.userservice.entities.Role;
import com.tass.userservice.model.request.RoleRequest;
import com.tass.userservice.repositories.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    public BaseResponseV2 finAllRole() throws ApplicationException {

        return new BaseResponseV2(roleRepository.findAll());
    }


    public BaseResponseV2<RoleDTO> findById(Long id) throws ApplicationException {
        log.info("get role by id");
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty()) {
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }

        RoleDTO roleDTO = new RoleDTO();
        Role role = optionalRole.get();
        BeanUtils.copyProperties(role, roleDTO);
        BaseResponseV2<RoleDTO> response = new BaseResponseV2<>();
        response.setData(roleDTO);
        return response;

    }

}
