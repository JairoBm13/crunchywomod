// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.util.Log;

final class SwrveRunnables
{
    public static Runnable withoutExceptions(final Runnable runnable) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                }
                catch (Exception ex) {
                    Log.e("SwrveSDK", "Error executing runnable: ", (Throwable)ex);
                }
            }
        };
    }
}
