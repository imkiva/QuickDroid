package com.imkiva.quickdroid.example;

import com.imkiva.quickdroid.QuickDatabase;
import com.imkiva.quickdroid.persistence.QPersistOnce;
import com.imkiva.quickdroid.persistence.QPreference;
import com.imkiva.quickdroid.ui.presenter.Presenter;

/**
 * @author kiva
 */

public class MainPresenter extends Presenter<MainActivity> {
    void requestDelay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        } finally {
            postIfAvailable(MainActivity::onDelayFinish);
        }
    }

    void loadWelcome() {
        QPersistOnce.of("first_used")
                .run(() -> {
                    QPreference.of("main").setPreference("first", false);
                    QuickDatabase.open("main").dropAllTables();
                });

        postIfAvailable(MainActivity::onConfigureLoaded);
    }
}
