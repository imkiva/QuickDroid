package com.imkiva.quickdroid.persistence;

import com.imkiva.quickdroid.QuickApp;
import com.imkiva.quickdroid.functional.QConsumer;
import com.imkiva.quickdroid.util.LazySingleton;

/**
 * @author kiva
 */
public class QPersistOnce {
    private static final String QONCE_PREFERENCE_NAME = "__quick_once_pref_";
    private static final LazySingleton<QPreference> PREFERENCE = new LazySingleton<QPreference>() {
        @Override
        protected QPreference createInstance() {
            return QPreference.of(QONCE_PREFERENCE_NAME);
        }
    };

    private final String key;

    private QPersistOnce(String key) {
        this.key = key;
    }

    public void run(QConsumer<QPersistOnce> consumer) {
        if (!hasDone()) {
            consumer.accept(this);
        }
    }

    public void run(Runnable runnable) {
        run((once) -> {
            runnable.run();
            once.done();
        });
    }

    public void done() {
        PREFERENCE.get().setPreference(key, true);
    }

    public void todo() {
        PREFERENCE.get().setPreference(key, false);
    }

    private boolean hasDone() {
        return PREFERENCE.get().getPreference(key);
    }

    public static QPersistOnce of(String key) {
        return new QPersistOnce(key);
    }

    public static QPersistOnce of(int stringId) {
        return new QPersistOnce(QuickApp.getApplication().getString(stringId));
    }
}
