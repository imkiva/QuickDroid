package com.imkiva.quickdroid.database.statement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.DatabaseOperator;
import com.imkiva.quickdroid.database.SQLMalformedException;
import com.imkiva.quickdroid.database.TableMetaInfo;
import com.imkiva.quickdroid.database.type.FieldDataMapper;
import com.imkiva.quickdroid.database.type.FieldType;
import com.imkiva.quickdroid.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author kiva
 */

public class StatementBuilder {
    private final StringBuilder statement = new StringBuilder();
    private StatementType statementType = StatementType.INITIAL;
    private TableMetaInfo table;

    StatementBuilder(TableMetaInfo table) {
        this.table = table;
    }

    public StatementBuilder createTable() {
        switchState(StatementType.INITIAL, StatementType.CREATE_TABLE);
        statement.append("CREATE TABLE IF NOT EXISTS ")
                .append("'").append(table.tableName).append("'")
                .append(" (");

        if (table.hasDeclaredPrimaryKey) {
            buildPrimaryKey();
        } else {
            // default primary key
            statement.append("'").append(DatabaseOperator.DEFAULT_PRIMARY_KEY)
                    .append("' INTEGER PRIMARY KEY AUTOINCREMENT,");
        }

        for (Field field : table.databaseItems.keySet()) {
            FieldType dataType = table.databaseItems.get(field);
            statement.append("'").append(field.getName()).append("'")
                    .append(" ").append(dataType.name());
            if (dataType.isNotNull()) {
                statement.append(" NOT NULL");
            }

            statement.append(",");
        }
        deleteLastComma();
        statement.append(")");
        return this;
    }

    private void buildPrimaryKey() {
        statement.append("'").append(table.primaryKeyField.getName()).append("'");
        switch (table.primaryKeyType) {
            case INTEGER:
                statement.append(" INTEGER PRIMARY KEY AUTOINCREMENT");
                break;
            default:
                statement
                        .append("  ")
                        .append(table.primaryKeyType.name())
                        .append(" PRIMARY KEY");
        }
        statement.append(",");
    }

    public StatementBuilder insert(Object newValue) {
        switchState(StatementType.INITIAL, StatementType.INSERT);
        statement.append("INSERT INTO '")
                .append(table.tableName).append("' ")
                .append("VALUES(");

        if (table.hasDeclaredPrimaryKey) {
            switch (table.primaryKeyType) {
                case INTEGER:
                    // auto increment
                    statement.append("NULL,");
                    break;
                default:
                    try {
                        statement.append(FieldDataMapper.mapToString(table.primaryKeyType,
                                table.primaryKeyField, newValue)).append(",");
                    } catch (IllegalAccessException ignore) {
                    }
                    break;
            }

        } else {
            // default primary key
            statement.append("NULL,");
        }

        for (Field field : table.databaseItems.keySet()) {
            FieldType fieldType = table.databaseItems.get(field);
            try {
                statement.append(FieldDataMapper.mapToString(fieldType, field, newValue)).append(",");
            } catch (IllegalAccessException ignore) {
            }
        }
        deleteLastComma();
        statement.append(")");
        return this;
    }

    public StatementBuilder delete() {
        switchState(StatementType.INITIAL, StatementType.DELETE);
        statement.append("DELETE FROM '")
                .append(table.tableName)
                .append("' ");
        return this;
    }

    public StatementBuilder select() {
        switchState(StatementType.INITIAL, StatementType.SELECT);
        statement.append("SELECT * FROM '")
                .append(table.tableName)
                .append("' ");
        return this;
    }

    public StatementBuilder count() {
        switchState(StatementType.INITIAL, StatementType.SELECT);
        statement.append("SELECT COUNT(*) FROM '")
                .append(table.tableName)
                .append("' ");
        return this;
    }

    public StatementBuilder update(Object newValue) {
        switchState(StatementType.INITIAL, StatementType.UPDATE);
        statement.append("UPDATE '")
                .append(table.tableName)
                .append("' SET ");

        for (Field field : table.databaseItems.keySet()) {
            FieldType fieldType = table.databaseItems.get(field);
            try {
                statement.append(field.getName())
                        .append(" = ")
                        .append(FieldDataMapper.mapToString(fieldType, field, newValue))
                        .append(",");
            } catch (IllegalAccessException ignore) {
            }
        }

        deleteLastComma();
        return this;
    }


    /****** condition *******/
    public StatementBuilder where(@NonNull String sql, @Nullable Object... args) {
        return condition("WHERE", sql, args);
    }

    public StatementBuilder and(@NonNull String sql, @Nullable Object... args) {
        return condition("AND", sql, args);
    }

    public StatementBuilder or(@NonNull String sql, @Nullable Object... args) {
        return condition("OR", sql, args);
    }

    private StatementBuilder condition(@NonNull String conditionName,
                                       @NonNull String sql, @Nullable Object... args) {
        ensure(StatementType.WHERE,
                StatementType.DELETE, StatementType.SELECT, StatementType.UPDATE);
        statement.append(" ").append(conditionName).append(" ");
        return formatArgs(sql, args);
    }

    private StatementBuilder formatArgs(@NonNull String sql, @Nullable Object... args) {
        statement.append(StatementArgumentHelper.formatArgs(sql, args));
        return this;
    }

    /****** finisher *******/
    public Statement end() {
        Log.d(() -> "SQL: " + statement.toString());
        return new Statement(statement.toString());
    }

    /****** helpers *******/
    private void ensure(StatementType newType, StatementType... expectedCurrentTypes) {
        if (expectedCurrentTypes.length == 0) {
            return;
        }
        for (StatementType expectedType : expectedCurrentTypes) {
            if (statementType == expectedType) {
                return;
            }
        }

        throw new SQLMalformedException(newType.name()
                + " cannot be used in "
                + statementType.name()
                + ", expected "
                + Arrays.toString(expectedCurrentTypes));
    }

    private void switchState(StatementType expectedCurrentType, StatementType newType) {
        ensure(newType, expectedCurrentType);
        if (newType != null) {
            this.statementType = newType;
        }
    }

    private void deleteLastComma() {
        int lastCharIndex = statement.length() - 1;
        if (lastCharIndex >= 0 && statement.charAt(lastCharIndex) == ',') {
            statement.deleteCharAt(lastCharIndex);
        }
    }
}
