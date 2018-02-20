package com.imkiva.quickdroid.util;

import com.imkiva.quickdroid.QuickApp;
import com.imkiva.quickdroid.functional.QSupplier;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * @author kiva
 * @date 2017/12/16
 */
public final class Log {
    private static boolean LOG_ENABLED = QuickApp.isDebug();

    public static void setLogEnabled(boolean enabled) {
        Log.LOG_ENABLED = enabled;
    }

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

    private static void log(PrintStream printStream, String logType, QSupplier<String> message) {
        if (LOG_ENABLED) {
            printStream.print(getCallerHeader(logType));
            printStream.println(message.get());
        }
    }

    public static void e(QSupplier<String> message) {
        log(System.err, "Error", message);
    }

    public static void d(QSupplier<String> message) {
        log(System.out, "Debug", message);
    }

    public static void i(QSupplier<String> message) {
        log(System.out, "Info", message);
    }

    public static void w(QSupplier<String> message) {
        log(System.err, "Warn", message);
    }
}
