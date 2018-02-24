package com.imkiva.quickdroid.ui.presenter;

import android.support.annotation.NonNull;

import com.imkiva.quickdroid.reflection.Reflector;
import com.imkiva.quickdroid.ui.PresentedActivity;
import com.imkiva.quickdroid.ui.annotation.BindPresenter;

/**
 * @author kiva
 */

public class ActivityPresenterFactory<P extends Presenter> implements IPresenterFactory<P> {
    private Class<P> presenterClass;

    @SuppressWarnings("unchecked")
    public ActivityPresenterFactory(@NonNull PresentedActivity<P> presentedActivity) {
        BindPresenter bindPresenter = presentedActivity.getClass().getAnnotation(BindPresenter.class);
        if (bindPresenter != null) {
            this.presenterClass = (Class<P>) bindPresenter.value();
        }
    }

    @Override
    public P createPresenter() {
        if (presenterClass != null) {
            return Reflector.of(presenterClass).instance().get();
        }
        return null;
    }
}
