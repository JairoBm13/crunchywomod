// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import android.text.TextUtils;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import android.net.Uri$Builder;
import android.util.LogPrinter;
import android.net.Uri;

public final class zznn implements zznu
{
    private static final Uri zzaDR;
    private final LogPrinter zzaDS;
    
    static {
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("uri");
        uri$Builder.authority("local");
        zzaDR = uri$Builder.build();
    }
    
    public zznn() {
        this.zzaDS = new LogPrinter(4, "GA/LogCatTransport");
    }
    
    @Override
    public void zzb(final zzno zzno) {
        final ArrayList<Object> list = new ArrayList<Object>(zzno.zzvQ());
        Collections.sort(list, (Comparator<? super Object>)new Comparator<zznq>() {
            public int zza(final zznq zznq, final zznq zznq2) {
                return zznq.getClass().getCanonicalName().compareTo(zznq2.getClass().getCanonicalName());
            }
        });
        final StringBuilder sb = new StringBuilder();
        final Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            final String string = ((zznq<?>)iterator.next()).toString();
            if (!TextUtils.isEmpty((CharSequence)string)) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(string);
            }
        }
        this.zzaDS.println(sb.toString());
    }
    
    @Override
    public Uri zzhe() {
        return zznn.zzaDR;
    }
}
