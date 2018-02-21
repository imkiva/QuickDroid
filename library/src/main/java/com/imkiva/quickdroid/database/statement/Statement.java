package com.imkiva.quickdroid.database.statement;

import android.support.annotation.NonNull;

import com.imkiva.quickdroid.database.TableData;

/**
 * @author kiva
 */

public class Statement {
    @NonNull
    public static StatementBuilder begin(@NonNull TableData table) {
        return new StatementBuilder(table);
    }

    private final String sql;

    Statement(String sql) {
        this.sql = sql;
    }

    public String getCode() {
        return sql;
    }
}
