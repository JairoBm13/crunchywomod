// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{
    private String getGATrackingName() {
        final int identifier = this.getResources().getIdentifier(this.getClass().getCanonicalName(), "string", this.getActivity().getPackageName());
        if (identifier > 0) {
            return this.getActivity().getResources().getString(identifier);
        }
        return null;
    }
    
    private String parseExtras(final String... array) {
        final StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < array.length; ++i) {
            final String s = array[i];
            if (i != array.length - 1) {
                sb.append(s.replace(" ", "_")).append("_");
            }
            else {
                sb.append(s.replace(" ", "_"));
            }
        }
        return sb.toString().toLowerCase();
    }
    
    protected ApplicationState getApplicationState() {
        return CrunchyrollApplication.getApp(this).getApplicationState();
    }
    
    public void trackView() {
        Tracker.trackView(this);
    }
    
    protected void trackViewExtras(final String... array) {
        Tracker.trackView(this, this.parseExtras(array));
    }
    
    protected void trackViewWithExtras(final String... array) {
        Tracker.trackView(this, this.getGATrackingName() + "_" + this.parseExtras(array));
    }
}
