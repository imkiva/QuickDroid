package com.imkiva.quickdroid.database;

/**
 * @author kiva
 */
@FunctionalInterface
public interface OnDatabaseUpgradedListener {
    void onUpgraded(DatabaseOperator databaseOperator, int oldVersion, int newVersion);
}
