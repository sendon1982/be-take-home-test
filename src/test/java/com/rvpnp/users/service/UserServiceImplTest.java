package com.rvpnp.users.service;

import com.rvpnp.users.dao.UserRepository;
import com.rvpnp.users.entity.UserEntity;
import com.rvpnp.users.event.ChangePasswordEventPublisher;
import com.rvpnp.users.exception.NewAndConfirmedPasswordNotMatchException;
import com.rvpnp.users.exception.UserEmailNotFoundException;
import com.rvpnp.users.model.ChangePasswordRequest;
import com.rvpnp.users.model.ChangePasswordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.rvpnp.users.TestData.TEST_EMAIL;
import static com.rvpnp.users.TestData.TEST_ENCRYPTED_PASSWORD;
import static com.rvpnp.users.TestData.TEST_PASSWORD;
import static com.rvpnp.users.TestData.TEST_USER_ID;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.anyString;


class UserServiceImplTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ChangePasswordEventPublisher changePasswordEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserServiceImpl(userRepository, passwordEncoder, changePasswordEventPublisher);
    }

    @Test
    void test_changePassword_NewPasswordConfirmedPasswordNotMatch() {
        Mockito.when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        ChangePasswordRequest request = buildRequest();
        request.setConfirmNewPassword(TEST_PASSWORD + TEST_ENCRYPTED_PASSWORD);

        try {
            userService.changePassword(request);
        } catch (NewAndConfirmedPasswordNotMatchException e) {
            assertThat(e, notNullValue());
            assertThat(e.getErrors(), allOf(
                hasProperty("code", equalTo("0003")),
                hasProperty("message", containsString("User new password and confirmed new password not match"))
            ));
        }

        Mockito.verifyNoInteractions(userRepository);
        Mockito.verifyNoInteractions(changePasswordEventPublisher);
    }

    @Test
    void test_changePassword_UserNotFound() {
        Mockito.when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        try {
            userService.changePassword(buildRequest());
        } catch (UserEmailNotFoundException e) {
            assertThat(e, notNullValue());
            assertThat(e.getErrors(), allOf(
                hasProperty("code", equalTo("0001")),
                hasProperty("message", containsString("User does not exist in the system"))
            ));
        }

        Mockito.verify(userRepository).findByEmail(TEST_EMAIL);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(changePasswordEventPublisher);
    }

    @Test
    void test_changePassword_success() {
        UserEntity userEntity = buildUserEntity();

        Mockito.when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(userEntity);

        Mockito.when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCRYPTED_PASSWORD);

        Mockito.doNothing().when(changePasswordEventPublisher).publishCustomEvent(anyString());

        ChangePasswordResponse response = userService.changePassword(buildRequest());

        assertThat(response.getUserId(), equalTo(TEST_USER_ID));
        assertThat(response.getEmail(), equalTo(TEST_EMAIL));

        Mockito.verify(userRepository).findByEmail(TEST_EMAIL);
        Mockito.verify(userRepository).save(MockitoHamcrest.argThat(
            allOf(
                hasProperty("email", equalTo(TEST_EMAIL)),
                hasProperty("password", equalTo(TEST_ENCRYPTED_PASSWORD)),
                hasProperty("updateDAt", notNullValue())
            )
        ));

        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verify(passwordEncoder).encode(TEST_PASSWORD);
        Mockito.verifyNoMoreInteractions(passwordEncoder);

        Mockito.verify(changePasswordEventPublisher).publishCustomEvent(anyString());
        Mockito.verifyNoMoreInteractions(changePasswordEventPublisher);
    }

    private ChangePasswordRequest buildRequest() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(TEST_EMAIL);
        request.setNewPassword(TEST_PASSWORD);
        request.setConfirmNewPassword(TEST_PASSWORD);

        return request;
    }

    private UserEntity buildUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(TEST_USER_ID);
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setPassword(TEST_PASSWORD);

        return userEntity;
    }
}