// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.net.Uri;
import java.util.Map;
import android.content.Context;

class zzd implements zzb
{
    private final Context zzpH;
    
    public zzd(final Context zzpH) {
        this.zzpH = zzpH;
    }
    
    @Override
    public void zzF(final Map<String, Object> map) {
        final Map<String, Object> value = map.get("gtm.url");
        while (true) {
            Label_0083: {
                if (value != null) {
                    break Label_0083;
                }
                final Map<String, Object> value2 = map.get("gtm");
                if (value2 == null || !(value2 instanceof Map)) {
                    break Label_0083;
                }
                final Object value3 = value2.get("url");
                if (value3 != null && value3 instanceof String) {
                    final String queryParameter = Uri.parse((String)value3).getQueryParameter("referrer");
                    if (queryParameter != null) {
                        zzax.zzk(this.zzpH, queryParameter);
                        return;
                    }
                }
                return;
            }
            final Object value3 = value;
            continue;
        }
    }
}
