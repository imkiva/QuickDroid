package com.imkiva.quickdroid.ui.view;

import com.imkiva.quickdroid.ui.presenter.IPresenterFactory;

/**
 * @author kiva
 */

public interface IPresentedView<P> {
    P getPresenter();

    IPresenterFactory<P> getPresenterFactory();

    void setPresenterFactory(IPresenterFactory<P> factory);
}
