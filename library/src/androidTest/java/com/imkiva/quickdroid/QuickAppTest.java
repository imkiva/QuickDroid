package com.imkiva.quickdroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kiva
 */

public class QuickAppTest {
    @Test
    public void testGetApplication() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(appContext.getApplicationContext(), QuickApp.getApplication().getApplicationContext());
    }

    @Test
    @UiThreadTest
    public void testToast() {
        QuickApp.toast("hello world");
        QuickApp.longToast("hello world long");
        QuickApp.debugToast(() -> "debug hello world");
    }
}
