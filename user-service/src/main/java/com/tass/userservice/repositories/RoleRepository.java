package com.tass.userservice.repositories;

import com.tass.userservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
