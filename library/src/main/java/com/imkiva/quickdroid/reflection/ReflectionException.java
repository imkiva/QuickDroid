package com.imkiva.quickdroid.reflection;

/**
 * {@inheritDoc}
 *
 * @author kiva
 * @see NoSuchMethodException
 * @see NoSuchFieldException
 * @see IllegalAccessException
 * @see java.lang.reflect.InvocationTargetException
 */

public class ReflectionException extends RuntimeException {
    ReflectionException(String message) {
        super(message);
    }

    ReflectionException(Throwable cause) {
        super(cause);
    }
}
