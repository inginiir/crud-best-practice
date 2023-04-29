package com.kalita.drone.services.impl;

import com.kalita.drone.services.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditServiceImpl implements AuditService {

    @Override
    public void logAuditEvent(String event) {
        log.info(event); // dummy service for audit
    }
}
