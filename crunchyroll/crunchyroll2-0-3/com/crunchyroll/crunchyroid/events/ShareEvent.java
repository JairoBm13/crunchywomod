// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

public class ShareEvent
{
    private final int mId;
    
    public ShareEvent(final int mId) {
        this.mId = mId;
    }
    
    public int getId() {
        return this.mId;
    }
}
