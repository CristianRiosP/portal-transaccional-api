package com.portal.transaccional.portal_transaccional_api.domain.port;

public interface LoggerPort {
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Throwable throwable, Object... args);
}