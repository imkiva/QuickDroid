package com.imkiva.quickdroid;

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

        getPresenter().requestDelay(1000);
    }

    public void onDelayFinish() {
        button.setText("delay finished");
    }
}
