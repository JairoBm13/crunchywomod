// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

final class WireFormat
{
    static final int MESSAGE_SET_ITEM_END_TAG;
    static final int MESSAGE_SET_ITEM_TAG;
    static final int MESSAGE_SET_MESSAGE_TAG;
    static final int MESSAGE_SET_TYPE_ID_TAG;
    
    static {
        MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
        MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
        MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
        MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);
    }
    
    static int makeTag(final int n, final int n2) {
        return n << 3 | n2;
    }
}
