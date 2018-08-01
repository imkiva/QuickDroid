package com.imkiva.quickdroid.persistence;

import android.content.SharedPreferences;

/**
 * @author kiva
 */
class SharedPreferenceHolder implements IPreferenceHolder {
    private SharedPreferences prefs;

    SharedPreferenceHolder(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object value = prefs.getAll().get(key);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

    @Override
    public void put(String key, Object value) {
        if (value == null) {
            prefs.edit().remove(key).apply();
            return;
        }

        Class<?> type = value.getClass();
        if (type == int.class || type == Integer.class) {
            prefs.edit().putInt(key, (int) value).apply();

        } else if (type == float.class || type == Float.class) {
            prefs.edit().putFloat(key, (float) value).apply();

        } else if (type == boolean.class || type == Boolean.class) {
            prefs.edit().putBoolean(key, (boolean) value).apply();

        } else if (type == long.class || type == Long.class) {
            prefs.edit().putLong(key, (long) value).apply();

        } else if (type == String.class) {
            prefs.edit().putString(key, (String) value).apply();

        } else {
            prefs.edit().putString(key, value.toString()).apply();
        }
    }
}
