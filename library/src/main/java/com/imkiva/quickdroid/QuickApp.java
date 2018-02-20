package com.imkiva.quickdroid;

import android.app.Application;
import android.widget.Toast;

import com.imkiva.quickdroid.functional.QSupplier;
import com.imkiva.quickdroid.reflection.Reflector;

/**
 * @author kiva
 */

public final class QuickApp {
    /**
     * Get whether app is debug build.
     *
     * @return Debug build
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * Get current {@link android.app.Application}
     *
     * @return Current Application
     */
    public static Application getApplication() {
        return Reflector.on("android.app.ActivityThread")
                .call("currentActivityThread")
                .call("getApplication")
                .get();
    }

    /**
     * Show a {@link android.widget.Toast}
     *
     * @param text Text to display
     */
    public static void toast(String text) {
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a {@link android.widget.Toast}, but duration is {@code Toast.LENGTH_LONG}
     *
     * @param text Text to display
     */
    public static void longToast(String text) {
        Toast.makeText(getApplication(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * Show a {@link android.widget.Toast} if in debug builds.
     *
     * @param text Text to display
     */
    public static void debugToast(QSupplier<String> text) {
        if (isDebug()) {
            longToast(text.get());
        }
    }
}
