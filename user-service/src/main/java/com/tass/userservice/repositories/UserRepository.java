package com.tass.userservice.repositories;

import com.tass.userservice.entities.User;
import com.tass.userservice.entities.myenum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUsernameAndStatus(String username, UserStatus status);
}
