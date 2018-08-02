package com.imkiva.quickdroid.example;

import android.os.Bundle;
import android.widget.Button;

import com.imkiva.quickdroid.ui.PresentedActivity;
import com.imkiva.quickdroid.ui.annotation.BindPresenter;
import com.imkiva.quickdroid.ui.annotation.BindView;

/**
 * @author kiva
 */
@BindPresenter(MainPresenter.class)
public class MainActivity extends PresentedActivity<MainPresenter> {
    @BindView(R.id.main_button)
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_presenter);

        getPresenter().requestDelay(1000);
        getPresenter().loadWelcome();
    }

    public void onConfigureLoaded() {
    }

    public void onDelayFinish() {
        button.setText("delay finished");
    }
}
