package com.imkiva.quickdroid.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author kiva
 */

public class FakeInvocationHandler implements InvocationHandler {
    private Object target;
    private boolean isMap;

    FakeInvocationHandler(Object target) {
        this.target = target;
        this.isMap = (target instanceof Map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        try {
            return Reflector.on(target).call(name, args).get();

        } catch (ReflectionException e) {
            if (isMap) {
                Map<String, Object> map = (Map<String, Object>) target;
                int length = (args == null ? 0 : args.length);

                // Pay special attention to those getters and setters
                if (length == 0 && name.startsWith("get")) {
                    return map.get(convertPropertyName(name.substring(3)));
                } else if (length == 0 && name.startsWith("is")) {
                    return map.get(convertPropertyName(name.substring(2)));
                } else if (length == 1 && name.startsWith("set")) {
                    map.put(convertPropertyName(name.substring(3)), args[0]);
                    return null;
                }
            }

            throw new FakePenetratedException(e);
        }
    }

    private String convertPropertyName(String string) {
        int length = string.length();

        if (length == 0) {
            return "";
        } else if (length == 1) {
            return string.toLowerCase();
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }
}
