// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appindexing;

import android.net.Uri;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzu;
import android.os.Bundle;

public class Thing
{
    final Bundle zzNV;
    
    Thing(final Bundle zzNV) {
        this.zzNV = zzNV;
    }
    
    public Bundle zzkP() {
        return this.zzNV;
    }
    
    public static class Builder
    {
        final Bundle zzNW;
        
        public Builder() {
            this.zzNW = new Bundle();
        }
        
        public Thing build() {
            return new Thing(this.zzNW);
        }
        
        public Builder put(final String s, final Thing thing) {
            zzu.zzu(s);
            if (thing != null) {
                this.zzNW.putParcelable(s, (Parcelable)thing.zzNV);
            }
            return this;
        }
        
        public Builder put(final String s, final String s2) {
            zzu.zzu(s);
            if (s2 != null) {
                this.zzNW.putString(s, s2);
            }
            return this;
        }
        
        public Builder setId(final String s) {
            if (s != null) {
                this.put("id", s);
            }
            return this;
        }
        
        public Builder setName(final String s) {
            zzu.zzu(s);
            this.put("name", s);
            return this;
        }
        
        public Builder setUrl(final Uri uri) {
            zzu.zzu(uri);
            this.put("url", uri.toString());
            return this;
        }
    }
}
