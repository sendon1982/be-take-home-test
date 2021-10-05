package com.rvpnp.users.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChangePasswordEventListener {

    @EventListener
    public void handleContextStart(ChangePasswordEvent event) {
        log.info("ChangePasswordEvent happened for {}", event.getMessage());
    }
}