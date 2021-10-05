package com.rvpnp.users.exception;

import lombok.Getter;

@Getter
public class UserEmailNotFoundException extends RuntimeException {

    private Errors errors;

    public UserEmailNotFoundException(Errors errors) {
        this.errors = errors;
    }

}
