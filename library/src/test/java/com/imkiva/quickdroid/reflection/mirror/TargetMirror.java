package com.imkiva.quickdroid.reflection.mirror;

import com.imkiva.quickdroid.reflection.Constructor;
import com.imkiva.quickdroid.reflection.Mirrored;

/**
 * @author kiva
 */
@Mirrored("com.imkiva.quickdroid.reflection.ReflectTargetClass")
public interface TargetMirror {
    @Constructor
    void newInstance();

    @Constructor
    void newInstance(String something);

    String getSomething();
}
