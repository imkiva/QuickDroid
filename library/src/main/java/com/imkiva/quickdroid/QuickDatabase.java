package com.imkiva.quickdroid;

import com.imkiva.quickdroid.database.DatabaseConfig;
import com.imkiva.quickdroid.database.DatabaseOperator;
import com.imkiva.quickdroid.database.OnDatabaseUpgradedListener;

/**
 * @author kiva
 */

public final class QuickDatabase {
    public static DatabaseOperator open() {
        return open(DatabaseOperator.DEFAULT_DATABASE_NAME);
    }

    public static DatabaseOperator open(String databaseName) {
        return open(new DatabaseConfig(databaseName, QuickApp.getAppVersionCode()));
    }

    public static DatabaseOperator open(String databaseName, int databaseVersion) {
        return open(new DatabaseConfig(databaseName, databaseVersion));
    }

    public static DatabaseOperator open(OnDatabaseUpgradedListener upgradedListener) {
        return open(DatabaseOperator.DEFAULT_DATABASE_NAME, upgradedListener);
    }

    public static DatabaseOperator open(String databaseName, OnDatabaseUpgradedListener upgradedListener) {
        DatabaseConfig config = new DatabaseConfig(databaseName, QuickApp.getAppVersionCode());
        config.setOnDatabaseUpgradedListener(upgradedListener);
        return open(config);
    }

    public static DatabaseOperator open(DatabaseConfig config) {
        return new DatabaseOperator(QuickApp.getApplication().getApplicationContext(),
                config);
    }
}
