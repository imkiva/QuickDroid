package com.imkiva.quickdroid.persistence;

/**
 * @author kiva
 */
interface IPreferenceHolder {
    @SuppressWarnings("unchecked")
    <T> T get(String key);

    void put(String key, Object value);
}
