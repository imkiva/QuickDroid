package com.imkiva.quickdroid.reflection.mirror;

import com.imkiva.quickdroid.QuickReflection;

import org.junit.Test;

/**
 * @author kiva
 */
public class MirroredClassTest {
    @Test
    public void testMirror() {
        TargetMirror mirror = QuickReflection.mirror(TargetMirror.class);
        mirror.constructor();
        mirror.constructor("hello");
    }
}
