// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public abstract class $FinalizableSoftReference<T> extends SoftReference<T> implements $FinalizableReference
{
    protected $FinalizableSoftReference(final T t, final $FinalizableReferenceQueue $FinalizableReferenceQueue) {
        super(t, $FinalizableReferenceQueue.queue);
        $FinalizableReferenceQueue.cleanUp();
    }
}
