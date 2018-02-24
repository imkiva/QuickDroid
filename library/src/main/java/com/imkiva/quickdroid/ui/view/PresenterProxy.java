package com.imkiva.quickdroid.ui.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.ui.presenter.IPresenterFactory;
import com.imkiva.quickdroid.ui.presenter.Presenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kiva
 */

public class PresenterProxy<P extends Presenter> {
    private static final String PRESENTER_SAVED_STATE = "quick_presenter_saved_state";
    private static final String PRESENTER_ID = "quick_presenter_id";

    private static final class PresenterCache {
        private static Map<String, Presenter> PRESENTER_MAP = new HashMap<>();

        static void store(@NonNull Presenter presenter) {
            PRESENTER_MAP.put(generateId(presenter), presenter);
        }

        @Nullable
        static Presenter get(@Nullable String presenterId) {
            return PRESENTER_MAP.get(presenterId);
        }

        @NonNull
        static String generateId(@NonNull Presenter presenter) {
            return presenter.getClass().getName() + "@" + presenter.hashCode();
        }
    }

    @Nullable
    private P presenter;

    @Nullable
    private IPresenterFactory<P> factory;

    @Nullable
    private Bundle savedState = null;

    public PresenterProxy(@Nullable IPresenterFactory<P> factory) {
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public P getPresenter() {
        if (presenter == null) {
            IPresenterFactory<P> factory = getFactory();

            if (factory != null) {
                if (savedState != null) {
                    String presenterId = savedState.getString(PRESENTER_ID);
                    this.presenter = (P) PresenterCache.get(presenterId);

                } else {
                    presenter = factory.createPresenter();
                    if (presenter != null) {
                        PresenterCache.store(presenter);
                        presenter.create(savedState == null
                                ? null
                                : savedState.getBundle(PRESENTER_SAVED_STATE));
                    }
                }
            }
        }
        return presenter;
    }

    @Nullable
    public IPresenterFactory<P> getFactory() {
        return factory;
    }

    public void setFactory(@Nullable IPresenterFactory<P> factory) {
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    public void onTake(IPresentedView<P> view) {
        P presenter = getPresenter();
        if (presenter != null) {
            presenter.take(view);
        }
    }

    public void onDrop() {
        P presenter = getPresenter();
        if (presenter != null) {
            presenter.drop();
        }
    }

    public void onDestroy() {
        P presenter = getPresenter();
        if (presenter != null) {
            presenter.destroy();
        }
    }

    @NonNull
    public Bundle onSaveState() {
        Bundle savedState = new Bundle();
        P presenter = getPresenter();
        if (presenter != null) {
            // Save presenter state
            Bundle presenterSavedState = new Bundle();
            presenter.saveState(presenterSavedState);
            savedState.putBundle(PRESENTER_SAVED_STATE, presenterSavedState);

            // Save presenter
            savedState.putString(PRESENTER_ID, PresenterCache.generateId(presenter));
        }

        return savedState;
    }

    public void onRestoreState(@NonNull Bundle bundle) {
        this.savedState = bundle;
    }
}
