package com.imkiva.quickdroid;

import com.imkiva.quickdroid.reflection.MirrorInvocationHandler;
import com.imkiva.quickdroid.reflection.Mirrored;

import java.lang.reflect.Proxy;

/**
 * @author kiva
 */
public class QuickReflection {
    @SuppressWarnings("unchecked")
    public static <P> P mirror(Class<P> mirrorClass) {
        if (mirrorClass.getAnnotation(Mirrored.class) == null) {
            throw new IllegalArgumentException("Not a mirror");
        }

        return (P) Proxy.newProxyInstance(mirrorClass.getClassLoader(),
                new Class[]{mirrorClass}, new MirrorInvocationHandler(mirrorClass));
    }
}
