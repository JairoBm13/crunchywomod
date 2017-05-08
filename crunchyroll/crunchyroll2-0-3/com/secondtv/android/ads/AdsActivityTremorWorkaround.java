// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import com.secondtv.android.ads.ima.IMAActivity;
import com.secondtv.android.ads.vast.VastActivity;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import com.tremorvideo.sdk.android.videoad.TremorVideo;
import com.tremorvideo.sdk.android.videoad.TremorAdStateListener;
import android.app.Activity;

public class AdsActivityTremorWorkaround extends Activity implements AdShower, AdTracker, TremorAdStateListener
{
    AdHandler mAdHandler;
    private DeepLinker mDeepLinker;
    int mMaxAdStartSeconds;
    AdTrackListener mTrackListener;
    private String mTremorAppId;
    
    private void showTremor() {
        try {
            boolean b;
            if (this.mTremorAppId != null) {
                b = TremorVideo.showAd(this, this.mTremorAppId, 4919);
            }
            else {
                b = TremorVideo.showAd(this, 4919);
            }
            if (!b) {
                this.mAdHandler.onActivityResult(56026, 45243, null);
                if (this.mTrackListener != null) {
                    this.mTrackListener.onTremorRequestFail((Context)this);
                }
            }
            else if (this.mTrackListener != null) {
                this.mTrackListener.onTremorRequestSuccess((Context)this);
            }
        }
        catch (Exception ex) {
            this.mAdHandler.onActivityResult(56026, 45243, null);
        }
    }
    
    public static void start(final Activity activity, final AdRoll adRoll, final AdTrackListener adTrackListener, final int n, final DeepLinker deepLinker) {
        final Intent intent = new Intent((Context)activity, (Class)AdsActivityTremorWorkaround.class);
        intent.putExtra("adroll", (Serializable)adRoll);
        intent.putExtra("track_listener", (Serializable)adTrackListener);
        intent.putExtra("maxAdStartSeconds", n);
        intent.putExtra("deep_linker", (Parcelable)deepLinker);
        activity.startActivityForResult(intent, 56026);
    }
    
    public void adComplete(final boolean b, final int n) {
        if (b && this.mTrackListener != null) {
            this.mTrackListener.onTremorPlayed((Context)this);
        }
        try {
            this.mTrackListener.onTremorLoadAd((Context)this);
            TremorVideo.loadAd();
        }
        catch (Exception ex) {}
    }
    
    public void adReady(final boolean b) {
        this.mTrackListener.onTremorAdReady((Context)this, b);
    }
    
    public void adStart() {
    }
    
    public void finishAds() {
        this.setResult(-1);
        this.finish();
    }
    
    public void leftApp() {
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        switch (n) {
            default: {
                this.mAdHandler.onActivityResult(n, n2, intent);
            }
            case 4919: {
                this.mAdHandler.onActivityResult(n, 45242, intent);
            }
        }
    }
    
    public void onAdSlotStart() {
        if (this.mTrackListener != null) {
            this.mTrackListener.onAdSlotStart((Context)this);
        }
    }
    
    public void onBackPressed() {
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().addFlags(1024);
        this.setRequestedOrientation(6);
        this.setContentView(R.layout.activity_ads);
        TremorVideo.setAdStateListener(this);
        this.mTrackListener = (AdTrackListener)this.getIntent().getSerializableExtra("track_listener");
        final AdRoll adRoll = (AdRoll)this.getIntent().getSerializableExtra("adroll");
        this.mMaxAdStartSeconds = this.getIntent().getIntExtra("maxAdStartSeconds", Integer.MAX_VALUE);
        this.mDeepLinker = (DeepLinker)this.getIntent().getParcelableExtra("deep_linker");
        (this.mAdHandler = new AdHandler((AdHandler.AdTracker)this, (AdHandler.AdShower)this, adRoll, this.mMaxAdStartSeconds, this.mDeepLinker)).play();
    }
    
    public void showAd(final AdOption adOption) {
        final String type = adOption.getType();
        if (this.mTrackListener != null) {
            this.mTrackListener.onAdRequested((Context)this, type);
        }
        if ("tremor".equals(type)) {
            this.mTremorAppId = adOption.getParams().get("app_id");
            this.showTremor();
            return;
        }
        if ("vast".equals(type)) {
            VastActivity.start(this, 56026, adOption.getParams().get("url"), "close", "tap for more info", this.mTrackListener, this.mDeepLinker);
            return;
        }
        if ("googleIMA".equals(type)) {
            IMAActivity.start(this, 56026, adOption.getParams().get("url"), this.mTrackListener);
            return;
        }
        this.mAdHandler.next();
    }
}
