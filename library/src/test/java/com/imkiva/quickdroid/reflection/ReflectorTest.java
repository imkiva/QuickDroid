package com.imkiva.quickdroid.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kiva
 */

public class ReflectorTest {
    @Test
    public void forName() {
        Assert.assertEquals(SomeClass.class, Reflector.on(SomeClass.class.getName()).getTargetClass());
        Assert.assertEquals(SomeClass.class, Reflector.on(new SomeClass()).getTargetClass());
        Assert.assertEquals(SomeClass.class, Reflector.on(SomeClass.class).getTargetClass());
    }

    @Test
    public void instance() {
        SomeClass someClass = Reflector.on(SomeClass.class).instance().get();
        Assert.assertNotNull(someClass);
        Assert.assertEquals("default", someClass.getString());

        SomeClass someClass1 = Reflector.on(SomeClass.class).instance("ABC").get();
        Assert.assertNotNull(someClass1);
        Assert.assertEquals("ABC", someClass1.getString());
    }

    @Test
    public void call() {
        SomeClass someClass = Reflector.on(SomeClass.class).instance().get();
        Assert.assertNotNull(someClass);
        String got = Reflector.on(someClass).call("getString").get();
        Assert.assertEquals("default", got);

        SomeClass someClass1 = Reflector.on(SomeClass.class).instance().get();
        Assert.assertNotNull(someClass1);
        Reflector.on(someClass1).call("setString", "ABCDEF");
        Assert.assertEquals("ABCDEF", someClass1.getString());
    }

    @Test(expected = ReflectionException.class)
    public void callDoesNotExist() {
        SomeClass someClass = Reflector.on(SomeClass.class).instance().get();
        Reflector.on(someClass).call("doesNotExist");
    }

    @Test
    public void field() {
        SomeClass someClass = Reflector.on(SomeClass.class).instance().get();
        Assert.assertNotNull(someClass);
        String got = Reflector.on(someClass).get("string");
        Assert.assertEquals("default", got);

        SomeClass someClass1 = Reflector.on(SomeClass.class).instance().get();
        Assert.assertNotNull(someClass1);
        Reflector.on(someClass1).set("string", "ABCDEF");
        Assert.assertEquals("ABCDEF", someClass1.getString());
    }

    @Test
    public void fake() {
        SomeClass someClass = new SomeClass();
        SomeInterface someInterface = Reflector.on(someClass).fake(SomeInterface.class);
        someInterface.setName("hello");
        Assert.assertEquals("hello", someInterface.getName());
    }

    @Test(expected = FakePenetratedException.class)
    public void fakeDoesNotExist() {
        SomeClass someClass = new SomeClass();
        SomeInterface someInterface = Reflector.on(someClass).fake(SomeInterface.class);
        someInterface.setAge(10);
    }

    @Test
    public void fakeMap() {
        Map<String, Object> values = new HashMap<>();
        SomeClass someClass = new SomeClass();
        values.put("name", "Tom");
        values.put("age", 10);
        SomeInterface someInterface = Reflector.on(values).fake(SomeInterface.class);
        Assert.assertEquals("Tom", someInterface.getName());
        Assert.assertEquals(10, someInterface.getAge());

        someInterface.setSomeClass(someClass);
        someInterface.setAge(100);
        someInterface.setName("Bob");
        Assert.assertEquals("Bob", values.get("name"));
        Assert.assertEquals(100, values.get("age"));
        Assert.assertEquals(someClass, values.get("someClass"));
    }
}
