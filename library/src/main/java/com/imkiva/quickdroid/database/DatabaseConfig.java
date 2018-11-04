package com.imkiva.quickdroid.database;

/**
 * @author kiva
 */

public class DatabaseConfig {
    private String databaseName;
    private int databaseVersion;

    private OnDatabaseUpgradeListener onDatabaseUpgradeListener;
    private boolean clearTablesWhenUpdated = false;

    public DatabaseConfig(String databaseName, int databaseVersion) {
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion >= 1 ? databaseVersion : 1;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public boolean isClearTablesWhenUpdated() {
        return clearTablesWhenUpdated;
    }

    public void setClearTablesWhenUpdated(boolean clearTablesWhenUpdated) {
        this.clearTablesWhenUpdated = clearTablesWhenUpdated;
    }

    public void setOnDatabaseUpgradeListener(OnDatabaseUpgradeListener onDatabaseUpgradeListener) {
        this.onDatabaseUpgradeListener = onDatabaseUpgradeListener;
    }

    public OnDatabaseUpgradeListener getOnDatabaseUpgradeListener() {
        return onDatabaseUpgradeListener;
    }
}
