// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.Activity;
import com.secondtv.android.ads.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import com.secondtv.android.ads.DeepLinker;
import com.secondtv.android.ads.vast.widget.ClickthroughView;
import android.support.v4.app.Fragment;

public class ClickthroughFragment extends Fragment
{
    private ClickthroughView ctView;
    private String mCloseString;
    private DeepLinker mDeepLinker;
    private ClickthroughListener mListener;
    private String mUrl;
    
    public static ClickthroughFragment newInstance(final ClickthroughListener onClickthroughCompleteListener, final String closeString, final DeepLinker deepLinker) {
        final ClickthroughFragment clickthroughFragment = new ClickthroughFragment();
        clickthroughFragment.setCloseString(closeString);
        clickthroughFragment.setOnClickthroughCompleteListener(onClickthroughCompleteListener);
        clickthroughFragment.setDeepLinker(deepLinker);
        return clickthroughFragment;
    }
    
    public void load(final String mUrl) {
        this.mUrl = mUrl;
        if (this.ctView != null) {
            this.ctView.loadUrl("about:blank");
            this.ctView.loadUrl(this.mUrl);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R.layout.fragment_clickthrough, viewGroup, false);
        (this.ctView = (ClickthroughView)inflate.findViewById(R.id.clickthroughView)).setCloseString(this.mCloseString);
        this.ctView.setActivity(this.getActivity());
        this.ctView.setDeepLinker(this.mDeepLinker);
        this.ctView.setOnCloseListener((ClickthroughView.OnCloseListener)new ClickthroughView.OnCloseListener() {
            @Override
            public void onClose(final ClickthroughView clickthroughView) {
                ClickthroughFragment.this.mListener.onClickthroughDismissed();
            }
            
            @Override
            public boolean onMarketUrlOpened(final String s) throws RuntimeException {
                try {
                    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable((Context)ClickthroughFragment.this.getActivity()) == 0) {
                        final Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(s));
                        ClickthroughFragment.this.getActivity().startActivity(intent);
                        ClickthroughFragment.this.mListener.onClickthroughMarketLinkOpened();
                        return true;
                    }
                }
                catch (Exception ex) {}
                return false;
            }
        });
        if (this.mUrl != null) {
            this.ctView.loadUrl(this.mUrl);
        }
        return inflate;
    }
    
    void setCloseString(final String mCloseString) {
        this.mCloseString = mCloseString;
    }
    
    public void setDeepLinker(final DeepLinker mDeepLinker) {
        this.mDeepLinker = mDeepLinker;
    }
    
    public void setOnClickthroughCompleteListener(final ClickthroughListener mListener) {
        this.mListener = mListener;
    }
    
    public interface ClickthroughListener
    {
        void onClickthroughDismissed();
        
        void onClickthroughMarketLinkOpened();
    }
}
