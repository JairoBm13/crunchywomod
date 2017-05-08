// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public final class zzt
{
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int hashCode(final Object... array) {
        return Arrays.hashCode(array);
    }
    
    public static zza zzt(final Object o) {
        return new zza(o);
    }
    
    public static final class zza
    {
        private final Object zzGE;
        private final List<String> zzabb;
        
        private zza(final Object o) {
            this.zzGE = zzu.zzu(o);
            this.zzabb = new ArrayList<String>();
        }
        
        @Override
        public String toString() {
            final StringBuilder append = new StringBuilder(100).append(this.zzGE.getClass().getSimpleName()).append('{');
            for (int size = this.zzabb.size(), i = 0; i < size; ++i) {
                append.append(this.zzabb.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }
        
        public zza zzg(final String s, final Object o) {
            this.zzabb.add(zzu.zzu(s) + "=" + String.valueOf(o));
            return this;
        }
    }
}
