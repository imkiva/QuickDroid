package com.imkiva.quickdroid.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imkiva.quickdroid.QuickApp;
import com.imkiva.quickdroid.database.statement.Statement;

/**
 * @author kiva
 */
public class DatabaseOperator extends SQLiteOpenHelper {
    private boolean clearTablesWhenUpdated;
    private OnDatabaseUpgradedListener onDatabaseUpgradedListener;

    public DatabaseOperator(DatabaseConfig databaseConfig) {
        super(QuickApp.getApplication().getApplicationContext(),
                databaseConfig.getDatabaseName(),
                null,
                databaseConfig.getDatabaseVersion());
        this.clearTablesWhenUpdated = databaseConfig.isClearTablesWhenUpdated();
        this.onDatabaseUpgradedListener = databaseConfig.getOnDatabaseUpgradedListener();
    }

    public void dropAllTables() {}

    public void exec(Statement statement) {}

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
