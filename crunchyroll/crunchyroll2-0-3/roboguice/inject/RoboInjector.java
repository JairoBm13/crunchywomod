// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.support.v4.app.Fragment;
import android.app.Activity;
import com.google.inject.Injector;

public interface RoboInjector extends Injector
{
    void injectMembersWithoutViews(final Object p0);
    
    void injectViewMembers(final Activity p0);
    
    void injectViewMembers(final Fragment p0);
}
