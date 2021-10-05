package com.rvpnp.users.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Setter
@Getter
public class UserEntity {

    @Id
    private Long id;

    private String email;

    private String password;

    @Column(name = "updated_at")
    private LocalDateTime updateDAt;
}
