package com.imkiva.quickdroid;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
        return Reflector.of("android.app.ActivityThread")
                .call("currentActivityThread")
                .call("getApplication")
                .get();
    }

    /**
     * Get app's version code defined in AndroidManifest.xml
     *
     * @return version code
     */
    public static int getAppVersionCode() {
        return getPackageInfo().versionCode;
    }

    /**
     * Get app's version name defined in AndroidManifest.xml
     *
     * @return version name
     */
    public static String getAppVersionName() {
        return getPackageInfo().versionName;
    }

    /**
     * Get app's package info
     *
     * @return package info
     * @see QuickApp#getPackageInfo(int)
     */
    public static PackageInfo getPackageInfo() {
        return getPackageInfo(0);
    }

    /**
     * Get app's package info
     *
     * @return package info
     */
    public static PackageInfo getPackageInfo(int flags) {
        Application application = getApplication();
        PackageManager packageManager = application.getPackageManager();
        try {
            return packageManager.getPackageInfo(application.getPackageName(), flags);

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("This should never happen.");
        }
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
