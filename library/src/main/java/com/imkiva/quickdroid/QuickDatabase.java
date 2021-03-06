package com.imkiva.quickdroid;

import com.imkiva.quickdroid.database.DatabaseConfig;
import com.imkiva.quickdroid.database.DatabaseOperator;
import com.imkiva.quickdroid.database.OnDatabaseUpgradeListener;

import java.util.HashMap;
import java.util.Map;

/**
 * The database helper.
 *
 * @author kiva
 * @see DatabaseOperator
 */
public final class QuickDatabase {
    private static final Map<String, DatabaseOperator> CACHES = new HashMap<>(4);

    /**
     * Open a database with default name {@code DatabaseOperator.DEFAULT_DATABASE_NAME}.
     *
     * @return Database operator
     */
    public static DatabaseOperator open() {
        return open(DatabaseOperator.DEFAULT_DATABASE_NAME);
    }

    /**
     * Open a database with given database name.
     *
     * @param databaseName Database name
     * @return Database operator
     */
    public static DatabaseOperator open(String databaseName) {
        return open(new DatabaseConfig(databaseName, QuickApp.getAppVersionCode()));
    }

    /**
     * Open a database with given name and version.
     *
     * @param databaseName    Database name
     * @param databaseVersion Database version
     * @return Database operator
     */
    public static DatabaseOperator open(String databaseName, int databaseVersion) {
        return open(new DatabaseConfig(databaseName, databaseVersion));
    }

    /**
     * Open a database with callbacks.
     *
     * @param upgradedListener {@link OnDatabaseUpgradeListener}
     * @return Database operator
     */
    public static DatabaseOperator open(OnDatabaseUpgradeListener upgradedListener) {
        return open(DatabaseOperator.DEFAULT_DATABASE_NAME, upgradedListener);
    }

    /**
     * Open a database with given name and callbacks.
     *
     * @param databaseName           Database name
     * @param upgradedListener{@link OnDatabaseUpgradedListener}
     * @return Database operator
     */
    public static DatabaseOperator open(String databaseName, OnDatabaseUpgradeListener upgradedListener) {
        DatabaseConfig config = new DatabaseConfig(databaseName, QuickApp.getAppVersionCode());
        config.setOnDatabaseUpgradeListener(upgradedListener);
        return open(config);
    }

    /**
     * Open a database with given config.
     *
     * @param config {@link DatabaseConfig}
     * @return Database operator
     */
    public static DatabaseOperator open(DatabaseConfig config) {
        DatabaseOperator operator = CACHES.get(config.getDatabaseName());
        if (operator != null) {
            return operator;
        }
        operator = new DatabaseOperator(QuickApp.getApplication().getApplicationContext(),
                config);
        synchronized (CACHES) {
            CACHES.put(config.getDatabaseName(), operator);
        }
        return operator;
    }
}
