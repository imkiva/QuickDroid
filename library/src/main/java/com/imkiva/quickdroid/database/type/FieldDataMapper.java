package com.imkiva.quickdroid.database.type;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;

/**
 * @author kiva
 */
public class FieldDataMapper {
    @NonNull
    public static String mapToString(@NonNull Object object) {
        FieldType fieldType = FieldTypeConverter.convert(object.getClass());
        return mapToString(fieldType, object);
    }

    @NonNull
    public static String mapToString(@NonNull FieldType fieldType,
                                     @NonNull Field field,
                                     @NonNull Object receiver) throws IllegalAccessException {
        Object value = field.get(receiver);
        return mapToString(fieldType, value);
    }

    @NonNull
    private static String mapToString(@NonNull FieldType fieldType, @NonNull Object o) {
        switch (fieldType) {
            case INTEGER:
                if (o instanceof Boolean) {
                    return ((boolean) o) ? "1" : "0";
                } else {
                    return String.valueOf((int) o);
                }
            case TEXT:
                return "'" + o + "'";
            case DOUBLE:
                return String.valueOf((double) o);
            case FLOAT:
                return String.valueOf((float) o);
            case BIGINT:
                return String.valueOf((long) o);
        }
        throw new IllegalStateException();
    }
}
