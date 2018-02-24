package com.imkiva.quickdroid.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.ui.presenter.ActivityPresenterFactory;
import com.imkiva.quickdroid.ui.presenter.IPresenterFactory;
import com.imkiva.quickdroid.ui.presenter.Presenter;
import com.imkiva.quickdroid.ui.view.IPresentedView;
import com.imkiva.quickdroid.ui.view.PresenterProxy;

/**
 * @author kiva
 */

public class PresentedActivity<P extends Presenter> extends QuickActivity
        implements IPresentedView<P> {

    private static final String PRESENTER_STATE_KEY = "quick_presenter_state";

    @NonNull
    private PresenterProxy<P> presenterProxy = new PresenterProxy<>(
            new ActivityPresenterFactory<P>(this));

    @Override
    @NonNull
    public P getPresenter() {
        P presenter = presenterProxy.getPresenter();
        if (presenter == null) {
            throw new IllegalStateException("presenter for view "
                    + getClass().getSimpleName()
                    + " is null");
        }
        return presenter;
    }

    @Override
    @Nullable
    public IPresenterFactory<P> getPresenterFactory() {
        return presenterProxy.getFactory();
    }

    @Override
    public void setPresenterFactory(@Nullable IPresenterFactory<P> factory) {
        presenterProxy.setFactory(factory);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Bundle presenterState = savedInstanceState.getBundle(PRESENTER_STATE_KEY);
            if (presenterState != null) {
                presenterProxy.onRestoreState(presenterState);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterProxy.onSaveState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterProxy.onTake(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterProxy.onDrop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isChangingConfigurations()) {
            presenterProxy.onDestroy();
        }
    }
}
