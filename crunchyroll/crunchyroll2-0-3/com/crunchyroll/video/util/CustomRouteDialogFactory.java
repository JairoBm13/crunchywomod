// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.util;

import com.crunchyroll.video.fragments.CustomRouteControllerDialogFragment;
import android.support.v7.app.MediaRouteControllerDialogFragment;
import com.crunchyroll.video.fragments.CustomMediaRouteChooserDialogFragment;
import android.support.v7.app.MediaRouteChooserDialogFragment;
import android.content.Context;
import android.support.v7.app.MediaRouteDialogFactory;

public class CustomRouteDialogFactory extends MediaRouteDialogFactory
{
    final Context mContext;
    
    public CustomRouteDialogFactory(final Context mContext) {
        this.mContext = mContext;
    }
    
    @Override
    public MediaRouteChooserDialogFragment onCreateChooserDialogFragment() {
        return new CustomMediaRouteChooserDialogFragment();
    }
    
    @Override
    public MediaRouteControllerDialogFragment onCreateControllerDialogFragment() {
        return new CustomRouteControllerDialogFragment();
    }
}
