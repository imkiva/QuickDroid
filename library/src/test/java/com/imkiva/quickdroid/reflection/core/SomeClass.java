package com.imkiva.quickdroid.reflection.core;

/**
 * @author kiva
 */

public class SomeClass {
    private String string;
    private String name;

    SomeClass() {
        this("default");
    }

    private SomeClass(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    private void setString(String string) {
        this.string = string;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
