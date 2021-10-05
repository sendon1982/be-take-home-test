package com.rvpnp.users.validator;

import com.rvpnp.users.model.ChangePasswordRequest;
import org.junit.jupiter.api.Test;

import static com.rvpnp.users.TestData.TEST_EMAIL;
import static com.rvpnp.users.TestData.TEST_ENCRYPTED_PASSWORD;
import static com.rvpnp.users.TestData.TEST_PASSWORD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ChangePasswordValidatorTest {

    @Test
    void test_isPasswordAndConfirmPasswordMatch_WithMatchedPassword() {
        boolean result = ChangePasswordValidator.isPasswordAndConfirmPasswordMatch(buildRequestWithMatchedPassword());
        assertThat(result, is(true));
    }

    @Test
    void test_isPasswordAndConfirmPasswordMatch_WithNotMatchedPassword() {
        boolean result = ChangePasswordValidator.isPasswordAndConfirmPasswordMatch(buildRequestWithNotMatchedPassword());
        assertThat(result, is(false));
    }

    private ChangePasswordRequest buildRequestWithMatchedPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(TEST_EMAIL);
        request.setNewPassword(TEST_PASSWORD);
        request.setConfirmNewPassword(TEST_PASSWORD);

        return request;
    }

    private ChangePasswordRequest buildRequestWithNotMatchedPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(TEST_EMAIL);
        request.setNewPassword(TEST_PASSWORD);
        request.setConfirmNewPassword(TEST_PASSWORD + TEST_ENCRYPTED_PASSWORD);

        return request;
    }
}