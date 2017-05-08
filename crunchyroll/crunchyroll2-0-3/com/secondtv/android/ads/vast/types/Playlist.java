// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.types;

import java.util.Collections;
import java.util.ArrayList;
import com.secondtv.android.ads.vast.LinearAd;
import java.util.List;
import java.io.Serializable;

public class Playlist implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<LinearAd> linearAds;
    
    public Playlist() {
        this.linearAds = new ArrayList<LinearAd>();
    }
    
    public void addLinearAd(final LinearAd linearAd) {
        this.linearAds.add(linearAd);
    }
    
    public LinearAd getLinearAd(final int n) {
        try {
            return this.linearAds.get(n);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    public List<LinearAd> getLinearAds() {
        return Collections.unmodifiableList((List<? extends LinearAd>)this.linearAds);
    }
    
    public boolean isEmpty() {
        return this.linearAds.isEmpty();
    }
    
    public int size() {
        return this.linearAds.size();
    }
}
