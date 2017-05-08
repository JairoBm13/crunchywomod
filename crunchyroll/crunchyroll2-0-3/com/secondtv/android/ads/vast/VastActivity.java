// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import android.view.KeyEvent;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.secondtv.android.ads.R;
import android.net.Uri;
import com.secondtv.android.ads.AdTrackListener;
import com.secondtv.android.ads.DeepLinker;
import android.support.v4.app.FragmentActivity;

public class VastActivity extends FragmentActivity
{
    ClickthroughFragment.ClickthroughListener clickthroughListener;
    private String mCloseString;
    private ClickthroughFragment mCtFragment;
    private DeepLinker mDeepLinker;
    private String mInfoString;
    private AdTrackListener mTrackListener;
    private VastFragment mVastFragment;
    VastFragment.OnStatusUpdateListener vastListener;
    
    public VastActivity() {
        this.vastListener = new VastFragment.OnStatusUpdateListener() {
            @Override
            public void onClickthrough(final String s) {
                VastActivity.this.mVastFragment.pause();
                VastActivity.this.mCtFragment = ClickthroughFragment.newInstance(VastActivity.this.clickthroughListener, VastActivity.this.mCloseString, VastActivity.this.mDeepLinker);
                final Uri parse = Uri.parse(s);
                if (parse.getScheme().equals("crunchyroll") && VastActivity.this.mDeepLinker != null) {
                    VastActivity.this.mDeepLinker.open(VastActivity.this, true, parse, true);
                    return;
                }
                VastActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, VastActivity.this.mCtFragment).hide(VastActivity.this.mVastFragment).commit();
                VastActivity.this.mCtFragment.load(s);
            }
            
            @Override
            public void onComplete(final int n) {
                if (VastActivity.this.mTrackListener != null) {
                    VastActivity.this.mTrackListener.onVastPlayed((Context)VastActivity.this);
                }
                VastActivity.this.setResult(45242, VastActivity.this.getIntent());
                VastActivity.this.finish();
                VastActivity.this.overridePendingTransition(0, 0);
            }
            
            @Override
            public void onError(final Exception ex) {
                VastActivity.this.setResult(45244, VastActivity.this.getIntent());
                VastActivity.this.finish();
                VastActivity.this.overridePendingTransition(0, 0);
            }
            
            @Override
            public void onPrepared() {
                if (VastActivity.this.mTrackListener != null) {
                    VastActivity.this.mTrackListener.onAdStart(VastActivity.this.getApplicationContext(), "vast");
                }
                VastActivity.this.mVastFragment.play();
            }
        };
        this.clickthroughListener = new ClickthroughFragment.ClickthroughListener() {
            @Override
            public void onClickthroughDismissed() {
                VastActivity.this.getSupportFragmentManager().beginTransaction().remove(VastActivity.this.mCtFragment).show(VastActivity.this.mVastFragment).commit();
                VastActivity.this.getSupportFragmentManager().executePendingTransactions();
                VastActivity.this.mVastFragment.resume();
            }
            
            @Override
            public void onClickthroughMarketLinkOpened() {
                VastActivity.this.getSupportFragmentManager().beginTransaction().remove(VastActivity.this.mCtFragment).show(VastActivity.this.mVastFragment).commit();
                VastActivity.this.getSupportFragmentManager().executePendingTransactions();
            }
        };
    }
    
    public static void start(final Activity activity, final int n, final String s, final String s2, final String s3, final AdTrackListener adTrackListener, final DeepLinker deepLinker) {
        final Intent intent = new Intent((Context)activity, (Class)VastActivity.class);
        intent.putExtra("url", s);
        intent.putExtra("close_string", s2);
        intent.putExtra("info_string", s3);
        intent.putExtra("track_listener", (Serializable)adTrackListener);
        intent.putExtra("deep_linker", (Parcelable)deepLinker);
        activity.startActivityForResult(intent, n);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().addFlags(1024);
        this.setRequestedOrientation(6);
        final String stringExtra = this.getIntent().getStringExtra("url");
        this.mCloseString = this.getIntent().getStringExtra("close_string");
        this.mInfoString = this.getIntent().getStringExtra("info_string");
        this.mTrackListener = (AdTrackListener)this.getIntent().getSerializableExtra("track_listener");
        this.mDeepLinker = (DeepLinker)this.getIntent().getParcelableExtra("deep_linker");
        this.setContentView(R.layout.activity_ads_fragment_container);
        this.mVastFragment = VastFragment.newInstance(this.vastListener, this.mInfoString, this.mTrackListener);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.mVastFragment).commit();
        this.mVastFragment.load((Context)this, stringExtra);
        if (this.mTrackListener != null) {
            this.mTrackListener.onVastRequest((Context)this);
        }
    }
    
    @Override
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 4 || super.onKeyDown(n, keyEvent);
    }
}
