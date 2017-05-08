// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.content.Context;
import roboguice.RoboGuice;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public abstract class RoboListFragment extends ListFragment
{
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        RoboGuice.getInjector((Context)this.getActivity()).injectMembersWithoutViews(this);
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        RoboGuice.getInjector((Context)this.getActivity()).injectViewMembers(this);
    }
}
