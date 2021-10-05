package com.rvpnp.users.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordResponse {

    private Long userId;

    private String email;
}
