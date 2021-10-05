package com.rvpnp.users.service;

import com.rvpnp.users.model.ChangePasswordRequest;
import com.rvpnp.users.model.ChangePasswordResponse;
import com.rvpnp.users.model.UserLoginRequest;
import com.rvpnp.users.model.UserLoginResponse;

public interface UserService {

    public ChangePasswordResponse changePassword(ChangePasswordRequest request);

    public UserLoginResponse login(UserLoginRequest request);
}
