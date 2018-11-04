package com.imkiva.quickdroid.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author kiva
 */
public class MirrorInvocationHandler implements InvocationHandler {
    public <T> MirrorInvocationHandler(Class<T> mirrorClass) {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
