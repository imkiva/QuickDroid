package com.imkiva.quickdroid.database;

/**
 * @author kiva
 */

public class DatabaseMalformedException extends RuntimeException {
    public DatabaseMalformedException(String message) {
        super(message);
    }
}
