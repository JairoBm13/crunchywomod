// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.support.v7.app.MediaRouteChooserDialog;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.MediaRouteChooserDialogFragment;

public class CustomMediaRouteChooserDialogFragment extends MediaRouteChooserDialogFragment
{
    @Override
    public MediaRouteChooserDialog onCreateChooserDialog(final Context context, final Bundle bundle) {
        return new MediaRouteChooserDialog(context, 2131296445) {
            @Override
            protected void onCreate(final Bundle bundle) {
                super.onCreate(bundle);
                this.getWindow().setBackgroundDrawableResource(2130837680);
                this.getWindow().setFeatureDrawableResource(3, 2130837733);
            }
        };
    }
}
