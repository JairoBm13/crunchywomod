// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.ecommerce.Product;
import java.util.List;
import java.util.Map;
import com.google.android.gms.analytics.ecommerce.ProductAction;

public final class zzoa extends zznq<zzoa>
{
    private ProductAction zzIC;
    private final Map<String, List<Product>> zzID;
    private final List<Promotion> zzIE;
    private final List<Product> zzIF;
    
    public zzoa() {
        this.zzIF = new ArrayList<Product>();
        this.zzIE = new ArrayList<Promotion>();
        this.zzID = new HashMap<String, List<Product>>();
    }
    
    @Override
    public String toString() {
        final HashMap<String, List<Product>> hashMap = new HashMap<String, List<Product>>();
        if (!this.zzIF.isEmpty()) {
            hashMap.put("products", this.zzIF);
        }
        if (!this.zzIE.isEmpty()) {
            hashMap.put("promotions", (List<Product>)this.zzIE);
        }
        if (!this.zzID.isEmpty()) {
            hashMap.put("impressions", (List<Product>)this.zzID);
        }
        hashMap.put("productAction", (List<Product>)this.zzIC);
        return zznq.zzy(hashMap);
    }
    
    public void zza(final Product product, final String s) {
        if (product == null) {
            return;
        }
        String s2;
        if ((s2 = s) == null) {
            s2 = "";
        }
        if (!this.zzID.containsKey(s2)) {
            this.zzID.put(s2, new ArrayList<Product>());
        }
        this.zzID.get(s2).add(product);
    }
    
    @Override
    public void zza(final zzoa zzoa) {
        zzoa.zzIF.addAll(this.zzIF);
        zzoa.zzIE.addAll(this.zzIE);
        for (final Map.Entry<String, List<Product>> entry : this.zzID.entrySet()) {
            final String s = entry.getKey();
            final Iterator<Product> iterator2 = entry.getValue().iterator();
            while (iterator2.hasNext()) {
                zzoa.zza(iterator2.next(), s);
            }
        }
        if (this.zzIC != null) {
            zzoa.zzIC = this.zzIC;
        }
    }
    
    public ProductAction zzwu() {
        return this.zzIC;
    }
    
    public List<Product> zzwv() {
        return Collections.unmodifiableList((List<? extends Product>)this.zzIF);
    }
    
    public Map<String, List<Product>> zzww() {
        return this.zzID;
    }
    
    public List<Promotion> zzwx() {
        return Collections.unmodifiableList((List<? extends Promotion>)this.zzIE);
    }
}
