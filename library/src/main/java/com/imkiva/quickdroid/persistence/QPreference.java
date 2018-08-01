package com.imkiva.quickdroid.persistence;

import android.content.Context;

import com.imkiva.quickdroid.QuickApp;

/**
 * @author kiva
 */
public class QPreference {
    private IPreferenceHolder preferenceHolder;

    public static QPreference of(String name) {
        return of(name, Context.MODE_PRIVATE);
    }

    public static QPreference of(String name, int mode) {
        return new QPreference(name, mode);
    }

    public static QPreference memory() {
        return new QPreference(null, 0);
    }

    private QPreference(String name, int mode) {
        if (name == null) {
            preferenceHolder = new MemoryPreferenceHolder();
        } else {
            preferenceHolder = new SharedPreferenceHolder(QuickApp.getApplication()
                    .getSharedPreferences(name, mode));
        }
    }

    public void setPreference(String key, Object value) {
        preferenceHolder.put(key, value);
    }

    public <T> T getPreference(String key) {
        return preferenceHolder.get(key);
    }
}
