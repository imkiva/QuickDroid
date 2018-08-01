package com.imkiva.quickdroid.persistence;

import com.imkiva.quickdroid.QuickApp;
import com.imkiva.quickdroid.functional.QConsumer;
import com.imkiva.quickdroid.util.LazySingleton;

/**
 * @author kiva
 */
public class QOnce {
    private static final LazySingleton<QPreference> PREFERENCE = new LazySingleton<QPreference>() {
        @Override
        protected QPreference createInstance() {
            return QPreference.memory();
        }
    };

    private final String key;

    private QOnce(String key) {
        this.key = key;
    }

    public void run(QConsumer<QOnce> consumer) {
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

    public static QOnce of(String key) {
        return new QOnce(key);
    }

    public static QOnce of(int stringId) {
        return new QOnce(QuickApp.getApplication().getString(stringId));
    }
}
