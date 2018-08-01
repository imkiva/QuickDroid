package com.imkiva.quickdroid.persistence;

import java.util.HashMap;

/**
 * @author kiva
 */
class MemoryPreferenceHolder implements IPreferenceHolder {
    private HashMap<String, Object> prefs;

    MemoryPreferenceHolder() {
        this.prefs = new HashMap<>(4);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object value = prefs.get(key);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

    @Override
    public void put(String key, Object value) {
        if (value == null) {
            prefs.remove(key);
            return;
        }

        prefs.put(key, value);
    }
}
