// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;

public class $Finalizer extends Thread
{
    private static final Logger logger;
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;
    
    static {
        logger = Logger.getLogger($Finalizer.class.getName());
    }
    
    private void cleanUp(Reference<?> poll) throws ShutDown {
        final Method finalizeReferentMethod = this.getFinalizeReferentMethod();
    Label_0036_Outer:
        while (true) {
            poll.clear();
            if (poll == this.frqReference) {
                break;
            }
            while (true) {
                try {
                    finalizeReferentMethod.invoke(poll, new Object[0]);
                    if ((poll = this.queue.poll()) == null) {
                        return;
                    }
                    continue Label_0036_Outer;
                }
                catch (Throwable t) {
                    $Finalizer.logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
                    continue;
                }
                break;
            }
        }
        throw new ShutDown();
    }
    
    private Method getFinalizeReferentMethod() throws ShutDown {
        final Class clazz = this.finalizableReferenceClassReference.get();
        if (clazz == null) {
            throw new ShutDown();
        }
        try {
            return clazz.getMethod("finalizeReferent", (Class[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    this.cleanUp(this.queue.remove());
                }
            }
            catch (InterruptedException ex) {
                continue;
            }
            catch (ShutDown shutDown) {}
            break;
        }
    }
    
    private static class ShutDown extends Exception
    {
    }
}
