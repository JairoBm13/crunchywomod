// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public abstract class $FinalizableWeakReference<T> extends WeakReference<T> implements $FinalizableReference
{
    protected $FinalizableWeakReference(final T t, final $FinalizableReferenceQueue $FinalizableReferenceQueue) {
        super(t, $FinalizableReferenceQueue.queue);
        $FinalizableReferenceQueue.cleanUp();
    }
}
