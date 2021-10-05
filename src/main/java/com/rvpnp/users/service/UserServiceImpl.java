package com.rvpnp.users.service;

import com.rvpnp.users.dao.UserRepository;
import com.rvpnp.users.entity.UserEntity;
import com.rvpnp.users.event.ChangePasswordEventPublisher;
import com.rvpnp.users.exception.EmailPasswordNotMatchException;
import com.rvpnp.users.exception.Errors;
import com.rvpnp.users.exception.NewAndConfirmedPasswordNotMatchException;
import com.rvpnp.users.exception.UserEmailNotFoundException;
import com.rvpnp.users.model.ChangePasswordRequest;
import com.rvpnp.users.model.ChangePasswordResponse;
import com.rvpnp.users.model.UserLoginRequest;
import com.rvpnp.users.model.UserLoginResponse;
import com.rvpnp.users.validator.ChangePasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ChangePasswordEventPublisher changePasswordEventPublisher;

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        log.info("Change password for user email {}", request.getEmail());

        boolean match = ChangePasswordValidator.isPasswordAndConfirmPasswordMatch(request);
        if (!match) {
            throw new NewAndConfirmedPasswordNotMatchException(Errors.USER_NEW_CONFIRMED_PASSWORD_NOT_MATCH);
        }

        UserEntity userEntity = findByEmail(request.getEmail());

        if (userEntity == null) {
            throw new UserEmailNotFoundException(Errors.USER_EMAIL_NOT_FOUND);
        }

        log.info("Found user by email {}", userEntity.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setUpdateDAt(LocalDateTime.now());

        userRepository.save(userEntity);

        publishEvent(request);

        return buildChangePasswordResponse(request, userEntity);
    }

    private ChangePasswordResponse buildChangePasswordResponse(ChangePasswordRequest request, UserEntity userEntity) {
        ChangePasswordResponse response = new ChangePasswordResponse();
        response.setUserId(userEntity.getId());
        response.setEmail(request.getEmail());
        return response;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        UserEntity userEntity = findByEmail(request.getEmail());

        if (userEntity == null) {
            throw new UserEmailNotFoundException(Errors.USER_EMAIL_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            log.warn("User login email or password not match");
            throw new EmailPasswordNotMatchException(Errors.USER_EMAIL_NOT_MATCH);
        }

        UserLoginResponse response = buildUserLoginResponse(request, userEntity);

        log.info("User email {} login successfully", request.getEmail());

        return response;
    }

    private UserLoginResponse buildUserLoginResponse(UserLoginRequest request, UserEntity userEntity) {
        UserLoginResponse response = new UserLoginResponse();
        response.setUserId(userEntity.getId());
        response.setEmail(request.getEmail());
        return response;
    }

    // Publish an event
    private void publishEvent(ChangePasswordRequest request) {
        changePasswordEventPublisher.publishCustomEvent("Update password for user email " + request.getEmail());
    }

    private UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email) ;
    }
}
