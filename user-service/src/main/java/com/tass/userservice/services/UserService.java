package com.tass.userservice.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.redis.dto.UserLoginDTO;
import com.tass.common.redis.repository.UserLoginRepository;
import com.tass.userservice.entities.Role;
import com.tass.userservice.entities.User;
import com.tass.userservice.entities.myenum.UserStatus;
import com.tass.userservice.model.request.LoginRequest;
import com.tass.userservice.model.request.UserRequest;
import com.tass.userservice.repositories.RoleRepository;
import com.tass.userservice.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserLoginRepository userLoginRepository;

    @Autowired
    RoleRepository roleRepository;

    public BaseResponseV2 register(UserRequest request) throws ApplicationException {
        if (StringUtils.isBlank(request.getUsername())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username is empty");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password is empty");
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password not match");
        }
        if (StringUtils.isBlank(request.getEmail())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Email is empty");
        }
        Optional<User> optionalUser = userRepository.findUserByUsername(request.getUsername());
        if (optionalUser.isPresent()){
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAvatar("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg?fbclid=IwAR0IeeX4fdIKXrmKyVLdn3mGEhAkNFdQv3MH7f4P5okIBtG_Rx_fqonZjss");
        user.setStatus(UserStatus.ACTIVE);
        Role role = roleRepository.findRoleByName("USER");
        user.setRole(role);
        userRepository.save(user);
        return new BaseResponseV2<>(user);
    }

    public BaseResponseV2<UserLoginDTO> login(LoginRequest request) throws ApplicationException {
        Optional<User> optionalUser = userRepository.findUserByUsernameAndStatus(request.getUsername(), UserStatus.ACTIVE);

        if (optionalUser.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username not found");
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password not match");
        }


        UserLoginDTO userLoginDTO = new UserLoginDTO();
        String token = UUID.randomUUID().toString();

        userLoginDTO.setToken(token);
        userLoginDTO.setRole(user.getRole().getName());
        userLoginDTO.setUserId(user.getId());
        userLoginDTO.setTimeToLive(10000);

        userLoginRepository.save(userLoginDTO);

        BaseResponseV2<UserLoginDTO> loginResponse = new BaseResponseV2<>();
        loginResponse.setData(userLoginDTO);

        return loginResponse;
    }


    public BaseResponseV2<UserDTO> updateAccount(UserRequest userRequest, Principal principal, Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<User> optionalPrincipal = userRepository.findUserByUsernameAndStatus(principal.getName(), UserStatus.ACTIVE);
        if (optionalUser.isEmpty() || optionalPrincipal.isEmpty()) {
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }
        if (StringUtils.isBlank(userRequest.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password is empty");
        }

        if (!userRequest.getPassword().equals(userRequest.getRePassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password not match");
        }

        User existUser = optionalUser.get();
        existUser.setPassword(userRequest.getPassword());
        existUser.setAvatar(userRequest.getAvatar());
        existUser.setAddress(userRequest.getAddress());
        existUser.setEmail(userRequest.getEmail());
        existUser.setGender(userRequest.getGender());
        existUser.setFullName(userRequest.getFullName());
        existUser.setCreatedAt(LocalDateTime.now());
        existUser.setUpdatedAt(LocalDateTime.now());
        existUser.setCreatedBy(userRequest.getFullName());
        existUser.setUpdatedBy(userRequest.getFullName());
        userRepository.save(existUser);
        return new BaseResponseV2(ERROR.SUCCESS);
    }


    //                  for admin                    ////////////////////////////////////////////////

    public BaseResponseV2 findAllUser(Specification<User> specification) throws ApplicationException {
        return new BaseResponseV2(userRepository.findAll(specification, Sort.by("updatedAt").descending()));
    }

    public BaseResponseV2 findUserById(Long id) throws ApplicationException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }
        User user = optionalUser.get();

        BaseResponseV2<User> response = new BaseResponseV2<>();

        response.setData(user);
        return response;
    }

    public BaseResponseV2 blockUser( Long id) throws ApplicationException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }
        User existUser = optionalUser.get();
        existUser.setStatus(UserStatus.BLOCKED);
        userRepository.save(existUser);
        return new BaseResponseV2(ERROR.SUCCESS);
    }

    public BaseResponseV2 activeUser(Long id) throws ApplicationException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }
        User existUser = optionalUser.get();
        existUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(existUser);
        return new BaseResponseV2(ERROR.SUCCESS);
    }

    public BaseResponseV2 deleteUser(Long id) throws ApplicationException {
        BaseResponseV2 responseV2 = new BaseResponseV2();

        userRepository.deleteById(id);
        return responseV2;
    }

}
