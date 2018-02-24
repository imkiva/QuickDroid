package com.imkiva.quickdroid;

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
            MainActivity activity = getView();
            if (activity != null) {
                activity.onDelayFinish();
            }
        }
    }
}
