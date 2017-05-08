// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot;
import java.util.Collection;
import android.view.ViewGroup;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;

public class a implements AdDisplayContainer
{
    private static int e;
    private VideoAdPlayer a;
    private ViewGroup b;
    private Collection<CompanionAdSlot> c;
    private Map<String, CompanionAdSlot> d;
    
    static {
        a.e = 0;
    }
    
    public a() {
        this.c = (Collection<CompanionAdSlot>)Collections.emptyList();
        this.d = null;
    }
    
    public Map<String, CompanionAdSlot> a() {
        if (this.d == null) {
            this.d = new HashMap<String, CompanionAdSlot>();
            for (final CompanionAdSlot companionAdSlot : this.c) {
                final Map<String, CompanionAdSlot> d = this.d;
                final StringBuilder append = new StringBuilder().append("compSlot_");
                final int e = com.google.ads.interactivemedia.v3.b.a.e;
                com.google.ads.interactivemedia.v3.b.a.e = e + 1;
                d.put(append.append(e).toString(), companionAdSlot);
            }
        }
        return this.d;
    }
    
    @Override
    public ViewGroup getAdContainer() {
        return this.b;
    }
    
    @Override
    public VideoAdPlayer getPlayer() {
        return this.a;
    }
    
    @Override
    public void setAdContainer(final ViewGroup b) {
        this.b = b;
    }
    
    @Override
    public void setPlayer(final VideoAdPlayer a) {
        this.a = a;
    }
}
