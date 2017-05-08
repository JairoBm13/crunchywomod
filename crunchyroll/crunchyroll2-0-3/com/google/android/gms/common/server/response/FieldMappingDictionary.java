// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import android.os.Parcel;
import java.util.Iterator;
import com.google.android.gms.common.internal.zzu;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FieldMappingDictionary implements SafeParcelable
{
    public static final zzc CREATOR;
    private final int zzCY;
    private final HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zzabQ;
    private final ArrayList<Entry> zzabR;
    private final String zzabS;
    
    static {
        CREATOR = new zzc();
    }
    
    FieldMappingDictionary(final int zzCY, final ArrayList<Entry> list, final String s) {
        this.zzCY = zzCY;
        this.zzabR = null;
        this.zzabQ = zzc(list);
        this.zzabS = zzu.zzu(s);
        this.zzoz();
    }
    
    private static HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zzc(final ArrayList<Entry> list) {
        final HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> hashMap = new HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>>();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Entry entry = list.get(i);
            hashMap.put(entry.className, entry.zzoD());
        }
        return hashMap;
    }
    
    public int describeContents() {
        final zzc creator = FieldMappingDictionary.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String s : this.zzabQ.keySet()) {
            sb.append(s).append(":\n");
            final Map<String, FastJsonResponse.Field<?, ?>> map = this.zzabQ.get(s);
            for (final String s2 : map.keySet()) {
                sb.append("  ").append(s2).append(": ");
                sb.append(map.get(s2));
            }
        }
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzc creator = FieldMappingDictionary.CREATOR;
        zzc.zza(this, parcel, n);
    }
    
    public Map<String, FastJsonResponse.Field<?, ?>> zzco(final String s) {
        return this.zzabQ.get(s);
    }
    
    ArrayList<Entry> zzoB() {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        for (final String s : this.zzabQ.keySet()) {
            list.add(new Entry(s, this.zzabQ.get(s)));
        }
        return list;
    }
    
    public String zzoC() {
        return this.zzabS;
    }
    
    public void zzoz() {
        final Iterator<String> iterator = this.zzabQ.keySet().iterator();
        while (iterator.hasNext()) {
            final Map<String, FastJsonResponse.Field<?, ?>> map = this.zzabQ.get(iterator.next());
            final Iterator<String> iterator2 = map.keySet().iterator();
            while (iterator2.hasNext()) {
                ((FastJsonResponse.Field)map.get(iterator2.next())).zza(this);
            }
        }
    }
    
    public static class Entry implements SafeParcelable
    {
        public static final zzd CREATOR;
        final String className;
        final int versionCode;
        final ArrayList<FieldMapPair> zzabT;
        
        static {
            CREATOR = new zzd();
        }
        
        Entry(final int versionCode, final String className, final ArrayList<FieldMapPair> zzabT) {
            this.versionCode = versionCode;
            this.className = className;
            this.zzabT = zzabT;
        }
        
        Entry(final String className, final Map<String, FastJsonResponse.Field<?, ?>> map) {
            this.versionCode = 1;
            this.className = className;
            this.zzabT = zzB(map);
        }
        
        private static ArrayList<FieldMapPair> zzB(final Map<String, FastJsonResponse.Field<?, ?>> map) {
            if (map == null) {
                return null;
            }
            final ArrayList<FieldMapPair> list = new ArrayList<FieldMapPair>();
            for (final String s : map.keySet()) {
                list.add(new FieldMapPair(s, map.get(s)));
            }
            return list;
        }
        
        public int describeContents() {
            final zzd creator = Entry.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzd creator = Entry.CREATOR;
            zzd.zza(this, parcel, n);
        }
        
        HashMap<String, FastJsonResponse.Field<?, ?>> zzoD() {
            final HashMap<String, FastJsonResponse.Field<?, ?>> hashMap = new HashMap<String, FastJsonResponse.Field<?, ?>>();
            for (int size = this.zzabT.size(), i = 0; i < size; ++i) {
                final FieldMapPair fieldMapPair = this.zzabT.get(i);
                hashMap.put(fieldMapPair.zzaC, fieldMapPair.zzabU);
            }
            return hashMap;
        }
    }
    
    public static class FieldMapPair implements SafeParcelable
    {
        public static final zzb CREATOR;
        final int versionCode;
        final String zzaC;
        final FastJsonResponse.Field<?, ?> zzabU;
        
        static {
            CREATOR = new zzb();
        }
        
        FieldMapPair(final int versionCode, final String zzaC, final FastJsonResponse.Field<?, ?> zzabU) {
            this.versionCode = versionCode;
            this.zzaC = zzaC;
            this.zzabU = zzabU;
        }
        
        FieldMapPair(final String zzaC, final FastJsonResponse.Field<?, ?> zzabU) {
            this.versionCode = 1;
            this.zzaC = zzaC;
            this.zzabU = zzabU;
        }
        
        public int describeContents() {
            final zzb creator = FieldMapPair.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzb creator = FieldMapPair.CREATOR;
            zzb.zza(this, parcel, n);
        }
    }
}
