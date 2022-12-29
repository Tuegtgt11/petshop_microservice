package com.tass.userservice.entities;

import com.tass.common.model.base.BaseEntity;
import com.tass.userservice.entities.myenum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private int gender;
    private String address;
    private String avatar;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

}
