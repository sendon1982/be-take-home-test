package com.rvpnp.users.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChangePasswordEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishCustomEvent(final String message) {
        log.info("Publishing ChangePasswordEvent for {}", message);

        ChangePasswordEvent changePasswordEvent = new ChangePasswordEvent(this, message);
        applicationEventPublisher.publishEvent(changePasswordEvent);
    }
}


