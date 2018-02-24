package com.imkiva.quickdroid.ui.annotation;

import com.imkiva.quickdroid.ui.presenter.Presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kiva
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindPresenter {
    Class<? extends Presenter> value();
}
