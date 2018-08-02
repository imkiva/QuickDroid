package com.imkiva.quickdroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.imkiva.quickdroid.ui.annotation.BindView;
import com.imkiva.quickdroid.util.QCollections;

/**
 * @author kiva
 */

public class QuickActivity extends Activity {
    private boolean viewAttached = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bindViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        bindViews();
    }

    private void bindViews() {
        if (viewAttached) {
            return;
        }

        QCollections.forEach(getClass().getFields(), field -> {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) {
                return;
            }

            int id = bindView.value();
            try {
                View view = findViewById(id);
                field.set(this, view);
            } catch (Throwable ignore) {
            }
        });
        viewAttached = true;
    }
}
