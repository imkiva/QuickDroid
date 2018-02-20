package com.imkiva.quickdroid.util;

import com.imkiva.quickdroid.functional.QSupplier;

/**
 * @author kiva
 * @date 2018/2/19
 */
public class Job<T> {
    private Runnable before;
    private Runnable after;
    private QSupplier<T> job;

    public static Job<Void> newVoidJob() {
        return new Job<Void>();
    }

    public static <T> Job<T> newJob() {
        return new Job<>();
    }

    private Job() {
    }

    public Job<T> before(Runnable before) {
        this.before = before;
        return this;
    }

    public Job<T> after(Runnable after) {
        this.after = after;
        return this;
    }

    public Job<T> then(QSupplier<T> job) {
        this.job = job;
        return this;
    }

    public T run() {
        callIf(before);
        T r = callIf(job);
        callIf(after);
        return r;
    }

    private void callIf(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private T callIf(QSupplier<T> runnable) {
        if (runnable != null) {
            return runnable.get();
        }
        return null;
    }
}
