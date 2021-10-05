package com.rvpnp.users.exception;

import lombok.Getter;

@Getter
public enum Errors {

    VALIDATION_ERROR(
        "0000",
        "Validation error"
    ),


    USER_EMAIL_NOT_FOUND(
        "0001",
        "User does not exist in the system"
    ),

    USER_EMAIL_NOT_MATCH(
        "0002",
        "User login email or password not match"
    ),

    USER_NEW_CONFIRMED_PASSWORD_NOT_MATCH(
        "0003",
        "User new password and confirmed new password not match"
    ),

    GENERAL_ERROR(
        "1111",
        "System general error, please retry later"
    );


    private final String code;

    private final String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
