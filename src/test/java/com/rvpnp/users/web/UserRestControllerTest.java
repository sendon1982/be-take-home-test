package com.rvpnp.users.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvpnp.users.exception.EmailPasswordNotMatchException;
import com.rvpnp.users.exception.Errors;
import com.rvpnp.users.exception.UserEmailNotFoundException;
import com.rvpnp.users.model.ChangePasswordRequest;
import com.rvpnp.users.model.ChangePasswordResponse;
import com.rvpnp.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.rvpnp.users.TestData.TEST_EMAIL;
import static com.rvpnp.users.TestData.TEST_PASSWORD;
import static com.rvpnp.users.TestData.TEST_USER_ID;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void test_changePassword_EmailIsNull() throws Exception {
        ChangePasswordRequest request = buildRequest();
        request.setEmail(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users/changePassword")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        //language=JSON
        String json = "{\n" +
            "  \"code\": \"0000\",\n" +
            "  \"message\": \"email:email cannot be empty;\"\n" +
            "}";

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void test_changePassword_EmailNotValidForamt() throws Exception {
        ChangePasswordRequest request = buildRequest();
        request.setEmail("test-not-valid-email");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users/changePassword")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        //language=JSON
        String json = "{\n" +
            "  \"code\": \"0000\",\n" +
            "  \"message\": \"email:invalid email format;\"\n" +
            "}";

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void test_changePassword_UserEmailNotFoundException() throws Exception {
        ChangePasswordRequest request = buildRequest();

        Mockito.when(userService.changePassword(any(ChangePasswordRequest.class))).thenThrow(
            new UserEmailNotFoundException(Errors.USER_EMAIL_NOT_FOUND));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users/changePassword")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        //language=JSON
        String json = "{\n" +
            "  \"code\": \"0001\",\n" +
            "  \"message\": \"User does not exist in the system\"\n" +
            "}";

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verify(userService).changePassword(MockitoHamcrest.argThat(
            allOf(
                hasProperty("email", equalTo(TEST_EMAIL)),
                hasProperty("newPassword", equalTo(TEST_PASSWORD)),
                hasProperty("confirmNewPassword", equalTo(TEST_PASSWORD))
            )
        ));

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void test_changePassword_EmailPasswordNotMatchException() throws Exception {
        ChangePasswordRequest request = buildRequest();

        Mockito.when(userService.changePassword(any(ChangePasswordRequest.class))).thenThrow(
            new EmailPasswordNotMatchException(Errors.USER_EMAIL_NOT_MATCH));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users/changePassword")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        //language=JSON
        String json = "{\n" +
            "  \"code\": \"0002\",\n" +
            "  \"message\": \"User login email or password not match\"\n" +
            "}";

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verify(userService).changePassword(MockitoHamcrest.argThat(
            allOf(
                hasProperty("email", equalTo(TEST_EMAIL)),
                hasProperty("newPassword", equalTo(TEST_PASSWORD)),
                hasProperty("confirmNewPassword", equalTo(TEST_PASSWORD))
            )
        ));

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void test_changePassword_ok() throws Exception {
        ChangePasswordRequest request = buildRequest();
        ChangePasswordResponse response = buildResponse();

        Mockito.when(userService.changePassword(any(ChangePasswordRequest.class))).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users/changePassword")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        //language=JSON
        String json = "{\n" +
            "  \"userId\": 999,\n" +
            "  \"email\": \"test-admin@gmail.com\"\n" +
            "}";

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verify(userService).changePassword(MockitoHamcrest.argThat(
            allOf(
                hasProperty("email", equalTo(TEST_EMAIL)),
                hasProperty("newPassword", equalTo(TEST_PASSWORD)),
                hasProperty("confirmNewPassword", equalTo(TEST_PASSWORD))
            )
        ));
        Mockito.verifyNoMoreInteractions(userService);
    }

    private ChangePasswordRequest buildRequest() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(TEST_EMAIL);
        request.setNewPassword(TEST_PASSWORD);
        request.setConfirmNewPassword(TEST_PASSWORD);

        return request;
    }

    private ChangePasswordResponse buildResponse() {
        ChangePasswordResponse response = new ChangePasswordResponse();

        response.setUserId(TEST_USER_ID);
        response.setEmail(TEST_EMAIL);

        return response;
    }
}