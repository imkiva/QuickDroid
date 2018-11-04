package com.imkiva.quickdroid.reflection;

/**
 * @author kiva
 */
public class ReflectTargetClass {
    private ReflectTargetClass() {
        System.out.println("Default Constructor");
    }

    private ReflectTargetClass(String something) {
        System.out.println("I have " + something);
    }

    private String getSomething() {
        return "You got me!";
    }
}
