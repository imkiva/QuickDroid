package com.imkiva.quickdroid.database;

/**
 * @author kiva
 */

public class SQLMalformedException extends RuntimeException {
    public SQLMalformedException(String message) {
        super(message);
    }
}
