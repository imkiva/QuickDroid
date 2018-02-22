package com.imkiva.quickdroid;

import android.database.sqlite.SQLiteDatabase;

import com.imkiva.quickdroid.database.DatabaseConfig;
import com.imkiva.quickdroid.database.DatabaseOperator;
import com.imkiva.quickdroid.database.OnDatabaseUpgradedListener;

/**
 * @author kiva
 */

public final class QuickDatabase {
    public static final String DEFAULT_DATABASE_NAME = "quick_database";

    private DatabaseConfig databaseConfig;
    private DatabaseOperator databaseOperator;

    private QuickDatabase(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        this.databaseOperator = new DatabaseOperator(QuickApp.getApplication().getApplicationContext(),
                databaseConfig);
    }

    public SQLiteDatabase getDatabase() {
        return databaseOperator.getWritableDatabase();
    }

    public static QuickDatabase open() {
        return open(new DatabaseConfig(DEFAULT_DATABASE_NAME));
    }

    public static QuickDatabase open(String databaseName) {
        return open(new DatabaseConfig(databaseName));
    }

    public static QuickDatabase open(String databaseName, int databaseVersion) {
        return open(new DatabaseConfig(databaseName, databaseVersion));
    }

    public static QuickDatabase open(OnDatabaseUpgradedListener upgradedListener) {
        return open(DEFAULT_DATABASE_NAME, upgradedListener);
    }

    public static QuickDatabase open(String databaseName, OnDatabaseUpgradedListener upgradedListener) {
        DatabaseConfig config = new DatabaseConfig(databaseName);
        config.setOnDatabaseUpgradedListener(upgradedListener);
        return open(config);
    }

    public static QuickDatabase open(DatabaseConfig config) {
        return new QuickDatabase(config);
    }
}
