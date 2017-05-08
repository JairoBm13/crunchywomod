// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class StringToIntConverter implements SafeParcelable, zza<String, Integer>
{
    public static final zzb CREATOR;
    private final int zzCY;
    private final HashMap<String, Integer> zzabB;
    private final HashMap<Integer, String> zzabC;
    private final ArrayList<Entry> zzabD;
    
    static {
        CREATOR = new zzb();
    }
    
    public StringToIntConverter() {
        this.zzCY = 1;
        this.zzabB = new HashMap<String, Integer>();
        this.zzabC = new HashMap<Integer, String>();
        this.zzabD = null;
    }
    
    StringToIntConverter(final int zzCY, final ArrayList<Entry> list) {
        this.zzCY = zzCY;
        this.zzabB = new HashMap<String, Integer>();
        this.zzabC = new HashMap<Integer, String>();
        this.zzabD = null;
        this.zzb(list);
    }
    
    private void zzb(final ArrayList<Entry> list) {
        for (final Entry entry : list) {
            this.zzh(entry.zzabE, entry.zzabF);
        }
    }
    
    public int describeContents() {
        final zzb creator = StringToIntConverter.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzb creator = StringToIntConverter.CREATOR;
        zzb.zza(this, parcel, n);
    }
    
    public String zzb(final Integer n) {
        String s2;
        final String s = s2 = this.zzabC.get(n);
        if (s == null) {
            s2 = s;
            if (this.zzabB.containsKey("gms_unknown")) {
                s2 = "gms_unknown";
            }
        }
        return s2;
    }
    
    public StringToIntConverter zzh(final String s, final int n) {
        this.zzabB.put(s, n);
        this.zzabC.put(n, s);
        return this;
    }
    
    ArrayList<Entry> zzoj() {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        for (final String s : this.zzabB.keySet()) {
            list.add(new Entry(s, this.zzabB.get(s)));
        }
        return list;
    }
    
    public static final class Entry implements SafeParcelable
    {
        public static final zzc CREATOR;
        final int versionCode;
        final String zzabE;
        final int zzabF;
        
        static {
            CREATOR = new zzc();
        }
        
        Entry(final int versionCode, final String zzabE, final int zzabF) {
            this.versionCode = versionCode;
            this.zzabE = zzabE;
            this.zzabF = zzabF;
        }
        
        Entry(final String zzabE, final int zzabF) {
            this.versionCode = 1;
            this.zzabE = zzabE;
            this.zzabF = zzabF;
        }
        
        public int describeContents() {
            final zzc creator = Entry.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzc creator = Entry.CREATOR;
            zzc.zza(this, parcel, n);
        }
    }
}
