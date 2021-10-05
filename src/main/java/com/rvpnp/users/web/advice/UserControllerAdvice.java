package com.rvpnp.users.web.advice;

import com.rvpnp.users.exception.EmailPasswordNotMatchException;
import com.rvpnp.users.exception.Errors;
import com.rvpnp.users.exception.NewAndConfirmedPasswordNotMatchException;
import com.rvpnp.users.exception.UserEmailNotFoundException;
import com.rvpnp.users.model.ErrorResponse;
import com.rvpnp.users.web.UserRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice(assignableTypes = UserRestController.class)
public class UserControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(Errors.VALIDATION_ERROR.getCode());

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            String defaultMessage = fieldError.getDefaultMessage();
            sb.append(fieldError.getField());
            sb.append(":");
            sb.append(fieldError.getDefaultMessage());
            sb.append(";");
        }
        errorResponse.setMessage(sb.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NewAndConfirmedPasswordNotMatchException.class)
    public ResponseEntity<ErrorResponse> handle(NewAndConfirmedPasswordNotMatchException e) {
        log.error("UserEmailNotFoundException", e);

        ErrorResponse errorResponse = buildErrorResponse(e.getErrors());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UserEmailNotFoundException e) {
        log.error("UserEmailNotFoundException", e);

        ErrorResponse errorResponse = buildErrorResponse(e.getErrors());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailPasswordNotMatchException.class)
    public ResponseEntity<ErrorResponse> handle(EmailPasswordNotMatchException e) {
        log.error("EmailPasswordNotMatchException", e);

        ErrorResponse errorResponse = buildErrorResponse(e.getErrors());

        return ResponseEntity.badRequest().body(errorResponse);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        log.error("Exception", e);

        ErrorResponse errorResponse = buildErrorResponse(Errors.GENERAL_ERROR);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    private ErrorResponse buildErrorResponse(Errors e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(e.getCode());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
}

