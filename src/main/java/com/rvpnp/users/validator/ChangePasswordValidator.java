package com.rvpnp.users.validator;

import com.rvpnp.users.model.ChangePasswordRequest;

public class ChangePasswordValidator {

    public static boolean isPasswordAndConfirmPasswordMatch(ChangePasswordRequest request) {
        return request.getNewPassword().equals(request.getConfirmNewPassword());
    }
}
