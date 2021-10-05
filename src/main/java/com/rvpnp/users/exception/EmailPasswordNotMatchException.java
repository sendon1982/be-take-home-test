package com.rvpnp.users.exception;

import lombok.Getter;

@Getter
public class EmailPasswordNotMatchException extends RuntimeException {

    private Errors errors;

    public EmailPasswordNotMatchException(Errors errors) {
        this.errors = errors;
    }

}
