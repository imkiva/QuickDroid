package com.imkiva.quickdroid.database.statement;

import android.support.annotation.NonNull;

import com.imkiva.quickdroid.database.DatabaseMalformedException;
import com.imkiva.quickdroid.database.TableData;
import com.imkiva.quickdroid.database.type.FieldDataMapper;
import com.imkiva.quickdroid.database.type.FieldType;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author kiva
 */

public class StatementBuilder {
    private final StringBuilder statement = new StringBuilder();
    private StatementType statementType = StatementType.INITIAL;
    private TableData table;

    StatementBuilder(TableData table) {
        this.table = table;
    }

    public StatementBuilder createTable() {
        switchState(StatementType.INITIAL, StatementType.CREATE_TABLE);
        statement.append("CREATE TABLE IF NOT EXISTS ")
                .append("'").append(table.tableName).append("'")
                .append(" (");

        if (table.hasPrimaryKey) {
            buildPrimaryKey();
        } else {
            // default primary key
            statement.append("'quick_id' INTEGER PRIMARY KEY AUTOINCREMENT,");
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

    public StatementBuilder insertInto(Object newValue) {
        switchState(StatementType.INITIAL, StatementType.INSERT);
        statement.append("INSERT INTO '")
                .append(table.tableName).append("' ")
                .append("VALUES(");

        if (table.hasPrimaryKey) {
            switch (table.primaryKeyType) {
                case INTEGER:
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
    public StatementBuilder where(@NonNull String sql, @NonNull Object... args) {
        ensure(StatementType.WHERE, StatementType.DELETE, StatementType.SELECT, StatementType.UPDATE);

        if (args.length == 0) {
            statement.append(" WHERE ").append(sql);
            return this;
        }

        String[] argStrings = new String[args.length];
        for (int i = 0; i < args.length; ++i) {
            argStrings[i] = FieldDataMapper.mapToString(args[i]);
        }
        statement.append(" WHERE ")
                .append(MessageFormat.format(sql, (Object[]) argStrings));
        return this;
    }

    /****** finisher *******/
    public Statement end() {
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

        throw new DatabaseMalformedException(newType.name()
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
