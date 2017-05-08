// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.zzu;
import java.util.HashMap;
import java.util.ArrayList;
import android.os.Parcel;
import android.util.Log;
import android.database.CursorWindow;
import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class DataHolder implements SafeParcelable
{
    public static final zze CREATOR;
    private static final zza zzYE;
    boolean mClosed;
    private final int zzCY;
    private final int zzTS;
    int[] zzYA;
    int zzYB;
    private Object zzYC;
    private boolean zzYD;
    private final String[] zzYw;
    Bundle zzYx;
    private final CursorWindow[] zzYy;
    private final Bundle zzYz;
    
    static {
        CREATOR = new zze();
        zzYE = (zza)new zza(new String[0], null) {};
    }
    
    DataHolder(final int zzCY, final String[] zzYw, final CursorWindow[] zzYy, final int zzTS, final Bundle zzYz) {
        this.mClosed = false;
        this.zzYD = true;
        this.zzCY = zzCY;
        this.zzYw = zzYw;
        this.zzYy = zzYy;
        this.zzTS = zzTS;
        this.zzYz = zzYz;
    }
    
    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.zzYy.length; ++i) {
                    this.zzYy[i].close();
                }
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.zzYD && this.zzYy.length > 0 && !this.isClosed()) {
                String s;
                if (this.zzYC == null) {
                    s = "internal object: " + this.toString();
                }
                else {
                    s = this.zzYC.toString();
                }
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (" + s + ")");
                this.close();
            }
        }
        finally {
            super.finalize();
        }
    }
    
    public int getStatusCode() {
        return this.zzTS;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.mClosed;
        }
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
    
    public Bundle zznb() {
        return this.zzYz;
    }
    
    public void zznf() {
        final int n = 0;
        this.zzYx = new Bundle();
        for (int i = 0; i < this.zzYw.length; ++i) {
            this.zzYx.putInt(this.zzYw[i], i);
        }
        this.zzYA = new int[this.zzYy.length];
        final int n2 = 0;
        int j = n;
        int zzYB = n2;
        while (j < this.zzYy.length) {
            this.zzYA[j] = zzYB;
            zzYB += this.zzYy[j].getNumRows() - (zzYB - this.zzYy[j].getStartPosition());
            ++j;
        }
        this.zzYB = zzYB;
    }
    
    String[] zzng() {
        return this.zzYw;
    }
    
    CursorWindow[] zznh() {
        return this.zzYy;
    }
    
    public static class zza
    {
        private final ArrayList<HashMap<String, Object>> zzYF;
        private final String zzYG;
        private final HashMap<Object, Integer> zzYH;
        private boolean zzYI;
        private String zzYJ;
        private final String[] zzYw;
        
        private zza(final String[] array, final String zzYG) {
            this.zzYw = zzu.zzu(array);
            this.zzYF = new ArrayList<HashMap<String, Object>>();
            this.zzYG = zzYG;
            this.zzYH = new HashMap<Object, Integer>();
            this.zzYI = false;
            this.zzYJ = null;
        }
    }
}
