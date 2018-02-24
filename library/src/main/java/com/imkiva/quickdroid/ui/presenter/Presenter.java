package com.imkiva.quickdroid.ui.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.ui.view.IPresentedView;

/**
 * @author kiva
 */

@SuppressWarnings("WeakerAccess")
public class Presenter<V extends IPresentedView> {
    private V view;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
    }

    protected void onTake(@NonNull V view) {
    }

    protected void onDrop() {
    }

    protected void onDestroy() {
    }

    protected void onSaveState(Bundle bundle) {
    }

    @Nullable
    public V getView() {
        return this.view;
    }

    public void create(Bundle bundle) {
        onCreate(bundle);
    }

    public void take(@NonNull V view) {
        this.view = view;
        onTake(view);
    }

    public void drop() {
        if (this.view != null) {
            onDrop();
            this.view = null;
        }
    }

    public void destroy() {
        onDestroy();
    }

    public void saveState(Bundle bundle) {
        onSaveState(bundle);
    }
}
