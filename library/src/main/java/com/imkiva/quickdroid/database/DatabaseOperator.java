package com.imkiva.quickdroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.statement.Statement;
import com.imkiva.quickdroid.database.statement.StatementBuilder;
import com.imkiva.quickdroid.database.type.FieldDataMapper;
import com.imkiva.quickdroid.database.type.FieldType;
import com.imkiva.quickdroid.functional.QSupplier;
import com.imkiva.quickdroid.reflection.Reflector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The database operator.
 * This class will do all the operations related to SQLiteDatabase.
 *
 * @author kiva
 */
public class DatabaseOperator extends SQLiteOpenHelper {
    /**
     * If no database name specified, use this instead.
     */
    public static final String DEFAULT_DATABASE_NAME = "quick_database";

    /**
     * If no primary key specified, use this instead.
     */
    public static final String DEFAULT_PRIMARY_KEY = "quick_id";

    /**
     * Whether we should drop all tables stored in database when it get updated.
     */
    private boolean clearTablesWhenUpdated;

    /**
     * Called when database get updated.
     */
    private OnDatabaseUpgradedListener onDatabaseUpgradedListener;

    public DatabaseOperator(@NonNull Context context, @NonNull DatabaseConfig databaseConfig) {
        super(context, databaseConfig.getDatabaseName(),
                null, databaseConfig.getDatabaseVersion());
        setConfig(databaseConfig);
    }

    /**
     * Apply new config without changing database name.
     *
     * @param config New config
     */
    public void setConfig(@NonNull DatabaseConfig config) {
        this.clearTablesWhenUpdated = config.isClearTablesWhenUpdated();
        this.onDatabaseUpgradedListener = config.getOnDatabaseUpgradedListener();
    }

    /**
     * Drop all tables!
     */
    public void dropAllTables() {
        for (String tableName : getAllTables()) {
            dropTable(tableName);
        }
    }

    /**
     * Drop table that stores given models.
     *
     * @param type Model
     */
    public void dropTable(@NonNull Class<?> type) {
        TableData tableData = TableData.get(type);
        dropTable(tableData.tableName, tableData);
    }

    /**
     * Drop table by given name.
     *
     * @param tableName Table name
     */
    public void dropTable(@NonNull String tableName) {
        dropTable(tableName, null);
    }

    /**
     * Get all models
     *
     * @param type Model
     * @param <T>  Model type
     * @return Models
     */
    @NonNull
    public <T> List<T> selectAll(@NonNull Class<T> type) {
        return selectWhere(type, null);
    }

    /**
     * Get model by primary key.
     *
     * @param type       Model
     * @param primaryKey Primary value
     * @param <T>        Model type
     * @return Model if found, otherwise {@code null}
     */
    @Nullable
    public <T> T selectPrimary(@NonNull Class<T> type, @NonNull Object primaryKey) {
        return selectPrimaryOrGet(type, primaryKey, () -> null);
    }

    /**
     * Get model by primary key.
     *
     * @param type         Model
     * @param primaryKey   Primary value
     * @param defaultValue The value returned when not found
     * @param <T>          Model type
     * @return Model if found, otherwise defaultValue
     */
    @Nullable
    public <T> T selectPrimaryOr(@NonNull Class<T> type, @NonNull Object primaryKey,
                                 @Nullable T defaultValue) {
        return selectPrimaryOrGet(type, primaryKey, () -> defaultValue);
    }

    /**
     * Get model by primary key.
     *
     * @param type         Model
     * @param primaryKey   Primary value
     * @param defaultValue The value returned when not found
     * @param <T>          Model type
     * @return Model if found, otherwise {@code defaultValue.get()}
     */
    @Nullable
    public <T> T selectPrimaryOrGet(@NonNull Class<T> type, @NonNull Object primaryKey,
                                    @NonNull QSupplier<T> defaultValue) {
        TableData tableData = TableData.get(type);
        validatePrimaryKey(tableData, primaryKey);
        String primaryKeyName = tableData.hasDeclaredPrimaryKey
                ? tableData.primaryKeyField.getName()
                : DEFAULT_PRIMARY_KEY;
        List<T> got = selectWhere(type, primaryKeyName + " = {0}",
                primaryKey);
        return got.isEmpty() ? defaultValue.get() : got.get(0);
    }

    /**
     * Get models that match the conditions.
     *
     * @param type Model
     * @param <T>  Model type
     * @return List containing matched models
     */
    @NonNull
    public <T> List<T> selectWhere(@NonNull Class<T> type, @Nullable String where, @Nullable Object... args) {
        TableData tableData = TableData.get(type);
        StatementBuilder builder = Statement.begin(tableData).select();
        if (where != null) {
            builder.where(where, args);
        }
        Statement statement = builder.end();

        try (Cursor cursor = query(statement)) {
            if (cursor != null) {
                List<T> found = new ArrayList<>();
                while (cursor.moveToNext()) {
                    T one = Reflector.of(type).instance().get();

                    if (tableData.hasDeclaredPrimaryKey) {
                        Field field = tableData.primaryKeyField;
                        FieldType primaryKeyType = tableData.primaryKeyType;
                        setValue(cursor, one, field, primaryKeyType, cursor.getColumnIndex(field.getName()));
                    }
                    for (Field field : tableData.databaseItems.keySet()) {
                        FieldType fieldType = tableData.databaseItems.get(field);
                        setValue(cursor, one, field, fieldType, cursor.getColumnIndex(field.getName()));
                    }
                    found.add(one);
                }
                return found;
            }
        } catch (Throwable ignore) {
        }

        return Collections.emptyList();
    }

    /**
     * Delete a model by primary key.
     *
     * @param type       Model
     * @param primaryKey Primary value
     */
    public void deletePrimary(@NonNull Class<?> type, @NonNull Object primaryKey) {
        TableData tableData = TableData.get(type);
        validatePrimaryKey(tableData, primaryKey);
        String primaryKeyName = tableData.hasDeclaredPrimaryKey
                ? tableData.primaryKeyField.getName()
                : DEFAULT_PRIMARY_KEY;
        Statement statement = Statement.begin(tableData)
                .delete()
                .where("{0} = {1}", primaryKeyName,
                        FieldDataMapper.mapToString(primaryKey))
                .end();
        exec(statement);
    }

    /**
     * Delete models that match the conditions.
     *
     * @param type  Model
     * @param where Conditions
     * @param args  Selection arguments
     */
    public void deleteWhere(@NonNull Class<?> type, @Nullable String where, @Nullable Object... args) {
        TableData tableData = TableData.get(type);
        StatementBuilder builder = Statement.begin(tableData).delete();
        if (where != null) {
            builder.where(where, args);
        }
        Statement statement = builder.end();
        exec(statement);
    }

    /**
     * Insert a model.
     *
     * @param object Model object
     */
    public void insert(@NonNull Object object) {
        TableData tableData = TableData.get(object.getClass());
        createTableIfNeed(tableData);
        Statement statement = Statement.begin(tableData)
                .insert(object)
                .end();
        exec(statement);
    }

    /**
     * Update a model.
     *
     * @param object Model object
     */
    public void update(@NonNull Object object) {
        TableData tableData = TableData.get(object.getClass());
        createTableIfNeed(tableData);
        Statement statement = Statement.begin(tableData)
                .update(object)
                .end();
        exec(statement);
    }

    private Cursor query(Statement statement) {
        return getWritableDatabase().rawQuery(statement.getCode(), null);
    }

    private void exec(Statement statement) {
        getWritableDatabase().execSQL(statement.getCode());
    }

    private void validatePrimaryKey(@NonNull TableData tableData, @NonNull Object primaryKey) {
        if (!tableData.hasDeclaredPrimaryKey) {
            FieldType primaryKeyType = FieldType.convert(primaryKey.getClass());
            if (primaryKeyType != FieldType.INTEGER) {
                throw new DatabaseMalformedException("The primary key type of able "
                        + tableData.tableName
                        + " is integer, but got "
                        + primaryKey.getClass().getName());
            }
            return;
        }

        Class<?> actualType = primaryKey.getClass();
        Class<?> expectedType = tableData.primaryKeyField.getType();
        if (!actualType.equals(expectedType)) {
            throw new DatabaseMalformedException("The primary key type of table "
                    + tableData.tableName
                    + " is " + expectedType.getName()
                    + ", but got " + actualType.getName());
        }
    }

    private void setValue(Cursor cursor, Object receiver, Field field, FieldType fieldType, int index) {
        try {
            switch (fieldType) {
                case INTEGER:
                    int intValue = cursor.getInt(index);
                    try {
                        field.setInt(receiver, intValue);
                    } catch (IllegalArgumentException e) {
                        field.setBoolean(receiver, intValue == 1);
                    }
                    break;
                case BIGINT:
                    field.setLong(receiver, cursor.getLong(index));
                    break;
                case FLOAT:
                    field.setFloat(receiver, cursor.getFloat(index));
                    break;
                case DOUBLE:
                    field.setDouble(receiver, cursor.getDouble(index));
                    break;
                case TEXT:
                    field.set(receiver, cursor.getString(index));
                    break;
            }
        } catch (IllegalAccessException ignore) {
        }
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

    private List<String> getAllTables() {
        Statement statement = Statement.begin(TableData.getMasterTableData())
                .select()
                .where("type = {0}", "table")
                .end();
        try (Cursor cursor = query(statement)) {
            if (cursor != null) {
                List<String> tableNames = new ArrayList<>();
                while (cursor.moveToNext()) {
                    tableNames.add(cursor.getString(0));
                }
                return tableNames;
            }
        } catch (Throwable ignore) {
        }
        return Collections.emptyList();
    }

    private void dropTable(@NonNull String tableName, @Nullable TableData tableData) {
        if (tableData == null) {
            tableData = TableData.searchByName(tableName);
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
