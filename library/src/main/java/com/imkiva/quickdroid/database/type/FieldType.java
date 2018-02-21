package com.imkiva.quickdroid.database.type;

import com.imkiva.quickdroid.database.annotation.NotNull;
import com.imkiva.quickdroid.database.annotation.PrimaryKey;
import com.imkiva.quickdroid.database.annotation.Skip;
import com.imkiva.quickdroid.functional.When;
import com.imkiva.quickdroid.reflection.ReflectionHelper;

import java.lang.reflect.Field;

/**
 * @author kiva
 */
public enum FieldType {
    INTEGER,

    TEXT,

    FLOAT,

    BIGINT,

    DOUBLE;

    private boolean isNotNull = false;

    public FieldType setNotNull(boolean notNull) {
        this.isNotNull = notNull;
        return this;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public static FieldType convert(Field field) {
        FieldType fieldType = convert(field.getType());
        if (fieldType != null) {
            fieldType.setNotNull(field.getAnnotation(NotNull.class) != null);
        }
        return fieldType;
    }

    public static FieldType convert(Class<?> clazz) {
        return When.<FieldType>when(ReflectionHelper.wrapType(clazz))
                .meet(String.class, () -> FieldType.TEXT)
                .meet(Integer.class, () -> FieldType.INTEGER)
                .meet(Float.class, () -> FieldType.FLOAT)
                .meet(Long.class, () -> FieldType.BIGINT)
                .meet(Double.class, () -> FieldType.DOUBLE)
                .meet(Boolean.class, () -> FieldType.INTEGER)
                .get();
    }

    public static boolean shouldSkip(Field field) {
        return field.getAnnotation(Skip.class) != null;
    }

    public static boolean isPrimaryKey(Field field) {
        return field.getAnnotation(PrimaryKey.class) != null;
    }
}
