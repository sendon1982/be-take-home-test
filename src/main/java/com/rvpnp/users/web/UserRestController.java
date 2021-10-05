package com.rvpnp.users.web;

import com.rvpnp.users.model.ChangePasswordRequest;
import com.rvpnp.users.model.ChangePasswordResponse;
import com.rvpnp.users.model.UserLoginRequest;
import com.rvpnp.users.model.UserLoginResponse;
import com.rvpnp.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @PostMapping("/changePassword")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        ChangePasswordResponse response = userService.changePassword(request);

        return ResponseEntity.ok(response);
    }

    // TODO: test purpose only
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }
}
