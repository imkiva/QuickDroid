package com.imkiva.quickdroid.database.statement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.TableData;

/**
 * @author kiva
 */

public class Statement {
    @NonNull
    public static StatementBuilder begin(@NonNull TableData table) {
        return new StatementBuilder(table);
    }

    @NonNull
    public static Statement rawStatement(@NonNull String statement, @Nullable Object... args) {
        return new Statement(StatementArgumentHelper.formatArgs(statement, args));
    }

    private final String sql;

    Statement(String sql) {
        this.sql = sql;
    }

    public String getCode() {
        return sql;
    }
}
