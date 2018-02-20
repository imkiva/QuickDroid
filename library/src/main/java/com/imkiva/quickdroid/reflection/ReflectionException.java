package com.imkiva.quickdroid.reflection;

/**
 * @author kiva
 */

public class ReflectionException extends RuntimeException {
    ReflectionException(String message) {
        super(message);
    }

    ReflectionException(Throwable cause) {
        super(cause);
    }
}
