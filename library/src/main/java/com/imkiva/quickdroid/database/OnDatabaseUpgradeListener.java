package com.imkiva.quickdroid.database;

/**
 * @author kiva
 */
@FunctionalInterface
public interface OnDatabaseUpgradeListener {
    void onUpgraded(DatabaseOperator databaseOperator, int oldVersion, int newVersion);
}
