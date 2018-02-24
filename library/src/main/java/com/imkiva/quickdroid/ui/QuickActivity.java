package com.imkiva.quickdroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.imkiva.quickdroid.ui.annotation.BindView;
import com.imkiva.quickdroid.util.QCollections;

/**
 * @author kiva
 */

public class QuickActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
    }

    private void bindViews() {
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
    }
}
