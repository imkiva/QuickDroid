package com.imkiva.quickdroid.database.type;

/**
 * @author kiva
 */
public enum FieldType {
    INTEGER,

    TEXT,

    FLOAT,

    BIGINT,

    DOUBLE;

    private boolean isNotNull = false;

    public FieldType setNotNull(boolean notNull) {
        this.isNotNull = notNull;
        return this;
    }

    public boolean isNotNull() {
        return isNotNull;
    }
}
