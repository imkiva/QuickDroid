package com.imkiva.quickdroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.statement.Statement;
import com.imkiva.quickdroid.database.statement.StatementBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kiva
 */
public class DatabaseOperator extends SQLiteOpenHelper {
    private boolean clearTablesWhenUpdated;
    private OnDatabaseUpgradedListener onDatabaseUpgradedListener;

    public DatabaseOperator(@NonNull Context context, @NonNull DatabaseConfig databaseConfig) {
        super(context, databaseConfig.getDatabaseName(),
                null, databaseConfig.getDatabaseVersion());
        setConfig(databaseConfig);
    }

    public void setConfig(@NonNull DatabaseConfig config) {
        this.clearTablesWhenUpdated = config.isClearTablesWhenUpdated();
        this.onDatabaseUpgradedListener = config.getOnDatabaseUpgradedListener();
    }

    public void dropAllTables() {
        for (String tableName : getAllTables()) {
            dropTable(tableName);
        }
    }

    public void dropTable(@NonNull Class<?> type) {
        TableData tableData = TableData.parse(type);
        dropTable(tableData.tableName, tableData);
    }

    public void dropTable(@NonNull String tableName) {
        dropTable(tableName, null);
    }

    private void dropTable(@NonNull String tableName, @Nullable TableData tableData) {
        if (tableData == null) {
            tableData = TableData.get(tableName);
        }
        if (tableData != null && tableData.created) {
            return;
        }
        Statement statement = Statement
                .rawStatement("DROP TABLE IF EXISTS {0}", tableName);
        exec(statement);
        if (tableData != null) {
            tableData.created = false;
        }
    }

    public <T> T select(Class<?> type) {
        return null;
    }

    public <T> T select(Class<?> type, String where, Object... args) {
        return null;
    }

    public <T> List<T> selectAll(Class<?> type) {
        return null;
    }

    public void insert(@NonNull Object object) {
        TableData tableData = TableData.parse(object.getClass());
        createTableIfNeed(tableData);
        Statement statement = Statement.begin(tableData)
                .insert(object)
                .end();
        exec(statement);
    }

    public void update(@NonNull Object object) {
        TableData tableData = TableData.parse(object.getClass());
        createTableIfNeed(tableData);
        Statement statement = Statement.begin(tableData)
                .update(object)
                .end();
        exec(statement);
    }

    public void delete(@NonNull Object object) {
        // TODO
    }

    public void delete(@NonNull Class<?> type, @Nullable String where, @Nullable Object... args) {
        TableData tableData = TableData.parse(type);
        StatementBuilder builder = Statement.begin(tableData);
        if (where != null) {
            builder.where(where, args);
        }
        Statement statement = builder.end();
        exec(statement);
    }

    private Cursor query(Statement statement) {
        return getWritableDatabase().rawQuery(statement.getCode(), null);
    }

    private void exec(Statement statement) {
        getWritableDatabase().execSQL(statement.getCode());
    }

    private void createTableIfNeed(TableData tableData) {
        if (isTableCreated(tableData)) {
            return;
        }
        createTable(tableData);
    }

    private boolean isTableCreated(TableData tableData) {
        if (tableData.created) {
            return true;
        }

        Statement statement = Statement.begin(TableData.getMasterTableData())
                .count()
                .where("type = {0}", "table")
                .and("name = {0}", tableData.tableName)
                .end();
        try (Cursor cursor = query(statement)) {
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    tableData.created = true;
                }
            }
        } catch (Throwable ignore) {
        }

        return tableData.created;
    }

    private List<String> getAllTables() {
        Statement statement = Statement.begin(TableData.getMasterTableData())
                .select()
                .where("type = {0}", "table")
                .end();
        try (Cursor cursor = query(statement)) {
            cursor.moveToFirst();
            List<String> tableNames = new ArrayList<>();
            while (cursor.moveToNext()) {
                tableNames.add(cursor.getString(0));
            }
            return tableNames;
        } catch (Throwable ignore) {
        }
        return Collections.emptyList();
    }

    private void createTable(TableData tableData) {
        if (tableData.created) {
            return;
        }
        Statement statement = Statement.begin(tableData)
                .createTable()
                .end();
        exec(statement);
        tableData.created = true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion && clearTablesWhenUpdated) {
            dropAllTables();
        }
        if (onDatabaseUpgradedListener != null) {
            onDatabaseUpgradedListener.onUpgraded(this, oldVersion, newVersion);
        }
    }
}
