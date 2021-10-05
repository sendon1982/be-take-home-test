package com.rvpnp.users.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotNull(message = "email cannot be empty")
    @Email(message = "invalid email format")
    private String email;

    @NotNull(message = "newPassword cannot be empty")
    private String newPassword;

    @NotNull(message = "confirmNewPassword cannot be empty")
    private String confirmNewPassword;
}
