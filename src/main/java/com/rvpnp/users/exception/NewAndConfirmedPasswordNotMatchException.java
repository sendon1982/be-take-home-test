package com.rvpnp.users.exception;

import lombok.Getter;

@Getter
public class NewAndConfirmedPasswordNotMatchException extends RuntimeException {

    private Errors errors;

    public NewAndConfirmedPasswordNotMatchException(Errors errors) {
        this.errors = errors;
    }

}
