// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.concurrent.LinkedBlockingQueue;

public final class Queues
{
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }
}
