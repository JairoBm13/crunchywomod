// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

import android.support.v4.app.Fragment;

public class PopupNewFragmentEvent
{
    private final Fragment mFragment;
    
    public PopupNewFragmentEvent(final Fragment mFragment) {
        this.mFragment = mFragment;
    }
    
    public Fragment getFragment() {
        return this.mFragment;
    }
}
