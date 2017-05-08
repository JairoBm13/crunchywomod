// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

public class ResponseParser
{
    public static int parse(final int n) {
        if (n < 200 || n > 299) {
            if (n >= 300 && n <= 399) {
                return 1;
            }
            if (n < 400 || n > 499) {
                if (n >= 500) {
                    return 1;
                }
                return 1;
            }
        }
        return 0;
    }
}
