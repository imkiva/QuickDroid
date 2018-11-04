package com.imkiva.quickdroid.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.annotation.Table;
import com.imkiva.quickdroid.database.type.FieldType;
import com.imkiva.quickdroid.reflection.core.ReflectionHelper;
import com.imkiva.quickdroid.util.LazySingleton;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kiva
 */

public class TableMetaInfo {
    private static final HashMap<Class<?>, TableMetaInfo> TABLE_DATA_CACHE = new HashMap<>(4);

    private static final String MASTER_TABLE = "sqlite_master";
    private static final LazySingleton<TableMetaInfo> MASTER_TABLE_DATA = new LazySingleton<TableMetaInfo>() {
        @Override
        protected TableMetaInfo createInstance() {
            TableMetaInfo tableMetaInfo = new TableMetaInfo();
            tableMetaInfo.tableName = MASTER_TABLE;
            tableMetaInfo.hasDeclaredPrimaryKey = false;
            tableMetaInfo.created = true;
            return tableMetaInfo;
        }
    };

    public boolean hasDeclaredPrimaryKey;
    public Field primaryKeyField;
    public FieldType primaryKeyType;
    public String tableName;
    public Map<Field, FieldType> databaseItems;
    public boolean created = false;

    private TableMetaInfo() {
    }

    static TableMetaInfo getMasterTableData() {
        return MASTER_TABLE_DATA.get();
    }

    @Nullable
    static TableMetaInfo searchByName(@NonNull String tableName) {
        for (TableMetaInfo tableMetaInfo : TABLE_DATA_CACHE.values()) {
            if (tableMetaInfo.tableName.equals(tableName)) {
                return tableMetaInfo;
            }
        }
        return null;
    }

    @NonNull
    static TableMetaInfo get(@NonNull Class<?> clazz) {
        TableMetaInfo tableMetaInfo = TABLE_DATA_CACHE.get(clazz);
        if (tableMetaInfo != null) {
            return tableMetaInfo;
        }

        tableMetaInfo = new TableMetaInfo();
        tableMetaInfo.hasDeclaredPrimaryKey = false;

        Table table = clazz.getAnnotation(Table.class);
        if (table != null && table.name().trim().length() != 0) {
            tableMetaInfo.tableName = table.name();
        } else {
            tableMetaInfo.tableName = clazz.getName().replaceAll("\\.", "_");
        }

        Map<Field, FieldType> databaseItems = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            ReflectionHelper.makeAccessible(field);
            if (FieldType.shouldSkip(field)) {
                continue;
            }
            if (FieldType.isPrimaryKey(field)) {
                if (tableMetaInfo.primaryKeyField != null) {
                    throw new SQLMalformedException("Multiple primary key is not allowed.");
                }
                tableMetaInfo.primaryKeyField = field;
                tableMetaInfo.primaryKeyType = parseFieldType(field);
                tableMetaInfo.hasDeclaredPrimaryKey = true;
                continue;
            }

            FieldType fieldType = parseFieldType(field);
            databaseItems.put(field, fieldType);
        }

        tableMetaInfo.databaseItems = databaseItems;
        putCache(clazz, tableMetaInfo);
        return tableMetaInfo;
    }

    private static FieldType parseFieldType(Field field) {
        FieldType fieldType = FieldType.convert(field);
        if (fieldType == null) {
            throw new IllegalArgumentException("Field not supported due to type mismatch: "
                    + field.getName()
                    + ", whose type is: " + field.getType().getName());
        }
        return fieldType;
    }

    private static void putCache(Class<?> clazz, TableMetaInfo tableMetaInfo) {
        synchronized (TableMetaInfo.class) {
            TABLE_DATA_CACHE.put(clazz, tableMetaInfo);
        }
    }
}
