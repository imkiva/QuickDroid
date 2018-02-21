package com.imkiva.quickdroid.database;

import com.imkiva.quickdroid.database.annotation.PrimaryKey;
import com.imkiva.quickdroid.database.annotation.Table;

/**
 * @author kiva
 */
@Table
public class PersonWithPrimaryKey {
    @PrimaryKey
    private int id;

    private String name;
    private int age;
    private int sex;
    private long time;
}
