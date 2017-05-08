// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzae;
import java.util.Iterator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.ecommerce.Product;
import java.util.List;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import java.util.Map;

public class HitBuilders
{
    @Deprecated
    public static class AppViewBuilder extends HitBuilder<AppViewBuilder>
    {
        public AppViewBuilder() {
            this.set("&t", "screenview");
        }
    }
    
    public static class EventBuilder extends HitBuilder<EventBuilder>
    {
        public EventBuilder() {
            this.set("&t", "event");
        }
        
        public EventBuilder setAction(final String s) {
            this.set("&ea", s);
            return this;
        }
        
        public EventBuilder setCategory(final String s) {
            this.set("&ec", s);
            return this;
        }
        
        public EventBuilder setLabel(final String s) {
            this.set("&el", s);
            return this;
        }
        
        public EventBuilder setValue(final long n) {
            this.set("&ev", Long.toString(n));
            return this;
        }
    }
    
    protected static class HitBuilder<T extends HitBuilder>
    {
        private Map<String, String> zzIB;
        ProductAction zzIC;
        Map<String, List<Product>> zzID;
        List<Promotion> zzIE;
        List<Product> zzIF;
        
        protected HitBuilder() {
            this.zzIB = new HashMap<String, String>();
            this.zzID = new HashMap<String, List<Product>>();
            this.zzIE = new ArrayList<Promotion>();
            this.zzIF = new ArrayList<Product>();
        }
        
        public Map<String, String> build() {
            final HashMap<String, String> hashMap = new HashMap<String, String>(this.zzIB);
            if (this.zzIC != null) {
                hashMap.putAll((Map<?, ?>)this.zzIC.build());
            }
            final Iterator<Promotion> iterator = this.zzIE.iterator();
            int n = 1;
            while (iterator.hasNext()) {
                hashMap.putAll((Map<?, ?>)iterator.next().zzaQ(zzc.zzT(n)));
                ++n;
            }
            final Iterator<Product> iterator2 = this.zzIF.iterator();
            int n2 = 1;
            while (iterator2.hasNext()) {
                hashMap.putAll((Map<?, ?>)iterator2.next().zzaQ(zzc.zzR(n2)));
                ++n2;
            }
            final Iterator<Map.Entry<String, List<Product>>> iterator3 = this.zzID.entrySet().iterator();
            int n3 = 1;
            while (iterator3.hasNext()) {
                final Map.Entry<String, List<Product>> entry = iterator3.next();
                final List<Product> list = entry.getValue();
                final String zzW = zzc.zzW(n3);
                final Iterator<Product> iterator4 = list.iterator();
                int n4 = 1;
                while (iterator4.hasNext()) {
                    hashMap.putAll((Map<?, ?>)iterator4.next().zzaQ(zzW + zzc.zzV(n4)));
                    ++n4;
                }
                if (!TextUtils.isEmpty((CharSequence)entry.getKey())) {
                    hashMap.put(zzW + "nm", entry.getKey());
                }
                ++n3;
            }
            return hashMap;
        }
        
        public final T set(final String s, final String s2) {
            if (s != null) {
                this.zzIB.put(s, s2);
                return (T)this;
            }
            zzae.zzaC(" HitBuilder.set() called with a null paramName.");
            return (T)this;
        }
        
        public T setCustomDimension(final int n, final String s) {
            this.set(zzc.zzN(n), s);
            return (T)this;
        }
    }
}
