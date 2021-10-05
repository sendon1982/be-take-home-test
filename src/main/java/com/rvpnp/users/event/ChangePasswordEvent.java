package com.rvpnp.users.event;

import org.springframework.context.ApplicationEvent;

public class ChangePasswordEvent extends ApplicationEvent {

    private String message;

    public ChangePasswordEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
