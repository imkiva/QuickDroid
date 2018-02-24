package com.imkiva.quickdroid.util;

import com.imkiva.quickdroid.QuickApp;
import com.imkiva.quickdroid.functional.QSupplier;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Friendly logger for Android.
 * Printing caller's name and source file info with log message.
 *
 * @author kiva
 */
public final class Log {
    private static String getCallerHeader(String logType) {
        StackTraceElement targetElement = new Throwable().getStackTrace()[3];
        String className = targetElement.getClassName();

        String[] array = className.split("\\.");
        List<String> classNameInfoList = new ArrayList<>(array.length);
        for (String item : array) {
            if (!item.isEmpty()) {
                classNameInfoList.add(item);
            }
        }
        String[] classNameInfo = classNameInfoList.toArray(new String[classNameInfoList.size()]);

        if (classNameInfo.length != 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }

        if (className.contains("$")) {
            className = "<unknown>";
            for (String item : className.split("\\$")) {
                if (!item.isEmpty()) {
                    className = item;
                    break;
                }
            }
        }

        return new Formatter()
                .format("[Thread:%s @ %s] %s.%s (%s:%d): ",
                        Thread.currentThread().getName(),
                        logType,
                        className,
                        targetElement.getMethodName(),
                        targetElement.getFileName(),
                        targetElement.getLineNumber())
                .toString();
    }

    public static void e(String message) {
        android.util.Log.e(getCallerHeader("E"), message);
    }

    public static void w(String message) {
        android.util.Log.e(getCallerHeader("W"), message);
    }

    public static void i(String message) {
        android.util.Log.e(getCallerHeader("I"), message);
    }

    public static void d(QSupplier<String> message) {
        if (QuickApp.isDebug()) {
            android.util.Log.e(getCallerHeader("E"), message.get());
        }
    }
}
