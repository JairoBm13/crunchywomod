// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

public class CardTypeEvent
{
    public static final int CARD_TYPE_LARGE = 3;
    public static final int CARD_TYPE_LIST = 1;
    public static final int CARD_TYPE_SMALL = 2;
    private int mType;
    
    public CardTypeEvent(final int mType) {
        this.mType = mType;
    }
    
    public int getType() {
        return this.mType;
    }
}
