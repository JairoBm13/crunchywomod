// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.test.shadow;

import com.xtremelabs.robolectric.internal.Implementation;
import android.os.Bundle;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(FragmentActivity.class)
public class ShadowFragmentActivity extends ShadowActivity
{
    @Implementation
    public FragmentManager getSupportFragmentManager() {
        return new FragmentManager() {
            @Override
            public void addOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
            }
            
            @Override
            public FragmentTransaction beginTransaction() {
                return new FragmentTransaction() {
                    @Override
                    public FragmentTransaction add(final int n, final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction add(final int n, final Fragment fragment, final String s) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction add(final Fragment fragment, final String s) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction addToBackStack(final String s) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction attach(final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public int commit() {
                        return 0;
                    }
                    
                    @Override
                    public int commitAllowingStateLoss() {
                        return 0;
                    }
                    
                    @Override
                    public FragmentTransaction detach(final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction disallowAddToBackStack() {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction hide(final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public boolean isAddToBackStackAllowed() {
                        return false;
                    }
                    
                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                    
                    @Override
                    public FragmentTransaction remove(final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction replace(final int n, final Fragment fragment) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction replace(final int n, final Fragment fragment, final String s) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setBreadCrumbShortTitle(final int n) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setBreadCrumbShortTitle(final CharSequence charSequence) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setBreadCrumbTitle(final int n) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setBreadCrumbTitle(final CharSequence charSequence) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setCustomAnimations(final int n, final int n2) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setCustomAnimations(final int n, final int n2, final int n3, final int n4) {
                        return this;
                    }
                    
                    @Override
                    public FragmentTransaction setTransition(final int n) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction setTransitionStyle(final int n) {
                        return null;
                    }
                    
                    @Override
                    public FragmentTransaction show(final Fragment fragment) {
                        return null;
                    }
                };
            }
            
            @Override
            public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            }
            
            @Override
            public boolean executePendingTransactions() {
                return false;
            }
            
            @Override
            public Fragment findFragmentById(final int n) {
                return null;
            }
            
            @Override
            public Fragment findFragmentByTag(final String s) {
                return null;
            }
            
            @Override
            public BackStackEntry getBackStackEntryAt(final int n) {
                return null;
            }
            
            @Override
            public int getBackStackEntryCount() {
                return 0;
            }
            
            @Override
            public Fragment getFragment(final Bundle bundle, final String s) {
                return null;
            }
            
            @Override
            public void popBackStack() {
            }
            
            @Override
            public void popBackStack(final int n, final int n2) {
            }
            
            @Override
            public void popBackStack(final String s, final int n) {
            }
            
            @Override
            public boolean popBackStackImmediate() {
                return false;
            }
            
            @Override
            public boolean popBackStackImmediate(final int n, final int n2) {
                return false;
            }
            
            @Override
            public boolean popBackStackImmediate(final String s, final int n) {
                return false;
            }
            
            @Override
            public void putFragment(final Bundle bundle, final String s, final Fragment fragment) {
            }
            
            @Override
            public void removeOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
            }
            
            @Override
            public Fragment.SavedState saveFragmentInstanceState(final Fragment fragment) {
                return null;
            }
        };
    }
}
