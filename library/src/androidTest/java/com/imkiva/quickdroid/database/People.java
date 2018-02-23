package com.imkiva.quickdroid.database;

import com.imkiva.quickdroid.database.annotation.PrimaryKey;

/**
 * @author kiva
 */

public class People {
    @PrimaryKey
    String name;

    int age;

    boolean female;

    public People() {}

    public People(String name, int age, boolean female) {
        this.name = name;
        this.age = age;
        this.female = female;
    }
}
