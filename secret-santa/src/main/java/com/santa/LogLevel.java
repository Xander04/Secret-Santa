package com.santa;

public enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    CRITICAL;

    public static LogLevel fromString(String level) {
            try {
                return LogLevel.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                return WARN; // Return a default enum constant
            }
    }
}
