package com.imkiva.quickdroid.database;

import android.support.annotation.NonNull;

import com.imkiva.quickdroid.database.annotation.Table;
import com.imkiva.quickdroid.database.type.FieldType;
import com.imkiva.quickdroid.reflection.ReflectionHelper;
import com.imkiva.quickdroid.util.LazySingleton;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kiva
 */

public class TableData {
    private static final HashMap<Class<?>, TableData> TABLE_DATA_CACHE = new HashMap<>(4);

    private static final String MASTER_TABLE = "sqlite_master";
    private static final LazySingleton<TableData> MASTER_TABLE_DATA = new LazySingleton<TableData>() {
        @Override
        protected TableData createInstance() {
            TableData tableData = new TableData();
            tableData.tableName = MASTER_TABLE;
            tableData.hasPrimaryKey = false;
            tableData.created = true;
            return tableData;
        }
    };

    public boolean hasPrimaryKey;
    public Field primaryKeyField;
    public FieldType primaryKeyType;
    public String tableName;
    public Map<Field, FieldType> databaseItems;
    public boolean created = false;

    private TableData() {
    }

    static TableData getMasterTableData() {
        return MASTER_TABLE_DATA.get();
    }

    @NonNull
    static TableData parse(@NonNull Class<?> clazz) {
        TableData tableData = TABLE_DATA_CACHE.get(clazz);
        if (tableData != null) {
            return tableData;
        }

        tableData = new TableData();
        tableData.hasPrimaryKey = false;

        Table table = clazz.getAnnotation(Table.class);
        if (table != null && table.name().trim().length() != 0) {
            tableData.tableName = table.name();
        } else {
            tableData.tableName = clazz.getName().replaceAll("\\.", "_");
        }

        Map<Field, FieldType> databaseItems = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            ReflectionHelper.makeAccessible(field);
            if (FieldType.shouldSkip(field)) {
                continue;
            }
            if (FieldType.isPrimaryKey(field)) {
                if (tableData.primaryKeyField != null) {
                    throw new DatabaseMalformedException("Multiple primary key is not allowed.");
                }
                tableData.primaryKeyField = field;
                tableData.primaryKeyType = parseFieldType(field);
                tableData.hasPrimaryKey = true;
                continue;
            }

            FieldType fieldType = parseFieldType(field);
            databaseItems.put(field, fieldType);
        }

        tableData.databaseItems = databaseItems;
        putCache(clazz, tableData);
        return tableData;
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

    private static void putCache(Class<?> clazz, TableData tableData) {
        synchronized (TableData.class) {
            TABLE_DATA_CACHE.put(clazz, tableData);
        }
    }
}
