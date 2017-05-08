// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.test.shadow;

import android.os.Bundle;
import android.app.Activity;
import com.xtremelabs.robolectric.internal.Implementation;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import com.xtremelabs.robolectric.internal.Implements;

@Implements(Fragment.class)
public class ShadowFragment
{
    protected FragmentActivity activity;
    protected View view;
    
    @Implementation
    public FragmentActivity getActivity() {
        return this.activity;
    }
    
    @Implementation
    public View getView() {
        return this.view;
    }
    
    @Implementation
    public void onAttach(final Activity activity) {
        this.activity = (FragmentActivity)activity;
    }
    
    @Implementation
    public void onViewCreated(final View view, final Bundle bundle) {
        this.view = view;
    }
}
