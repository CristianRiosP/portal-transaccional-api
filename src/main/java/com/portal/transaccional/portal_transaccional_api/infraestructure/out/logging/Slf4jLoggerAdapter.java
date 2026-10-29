package com.portal.transaccional.portal_transaccional_api.infraestructure.out.logging;

import com.portal.transaccional.portal_transaccional_api.domain.port.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLoggerAdapter implements LoggerPort {

    private static final Logger log = LoggerFactory.getLogger(Slf4jLoggerAdapter.class);

    @Override
    public void info(String message, Object... args) {
        log.info(maskSensitiveData(message), args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(maskSensitiveData(message), args);
    }

    @Override
    public void error(String message, Throwable throwable, Object... args) {
        log.error(maskSensitiveData(message), throwable);
    }

    /**
     * Enmascara datos sensibles por si se presenta un incidente de seguridad la confidencialidad de los datos esta segura
     **/
    private String maskSensitiveData(String message) {
        if (message == null) return "";
        return message
                .replaceAll("(?i)(token[:=]\\s*)[^\\s]+", "$1[PROTECTED]")
                .replaceAll("(?i)(documento[:=]\\s*)\\d+", "$1***-MASKED");
    }
}