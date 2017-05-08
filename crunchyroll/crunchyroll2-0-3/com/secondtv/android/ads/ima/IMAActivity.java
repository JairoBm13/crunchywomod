// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.ima;

import android.view.KeyEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import android.os.Bundle;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.secondtv.android.ads.R;
import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import com.secondtv.android.ads.AdTrackListener;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import android.app.Activity;

public class IMAActivity extends Activity implements AdErrorListener, AdEventListener, AdsLoadedListener, CompleteCallback
{
    AdsLoader adsLoader;
    AdsManager adsManager;
    boolean isAdStarted;
    AdTrackListener mTrackListener;
    IMAPlayer videoPlayer;
    
    public static void start(final Activity activity, final int n, final String s, final AdTrackListener adTrackListener) {
        final Intent intent = new Intent((Context)activity, (Class)IMAActivity.class);
        intent.putExtra("url", s);
        intent.putExtra("track_listener", (Serializable)adTrackListener);
        activity.startActivityForResult(intent, n);
    }
    
    public void onAdError(final AdErrorEvent adErrorEvent) {
        if (this.mTrackListener != null) {
            this.mTrackListener.onAdUnfulfilled((Context)this, "googleIMA");
        }
        this.setResult(45244, this.getIntent());
        this.finish();
        this.overridePendingTransition(0, 0);
    }
    
    public void onAdEvent(final AdEvent adEvent) {
        switch (adEvent.getType()) {
            default: {}
            case LOADED: {
                if (this.mTrackListener != null) {
                    this.mTrackListener.onAdStart(this.getApplicationContext(), "googleIMA");
                }
                this.adsManager.start();
            }
            case CONTENT_RESUME_REQUESTED: {
                if (this.mTrackListener != null) {
                    this.mTrackListener.onIMAPlayed((Context)this);
                }
                this.setResult(45242, this.getIntent());
                this.finish();
                this.overridePendingTransition(0, 0);
            }
            case STARTED: {
                this.findViewById(R.id.progress_bar).setVisibility(4);
                this.isAdStarted = true;
            }
            case COMPLETED: {
                this.isAdStarted = false;
            }
            case ALL_ADS_COMPLETED: {
                this.isAdStarted = false;
                this.adsManager.destroy();
            }
        }
    }
    
    public void onAdsManagerLoaded(final AdsManagerLoadedEvent adsManagerLoadedEvent) {
        (this.adsManager = adsManagerLoadedEvent.getAdsManager()).addAdErrorListener(this);
        this.adsManager.addAdEventListener(this);
        this.adsManager.init();
    }
    
    public void onComplete() {
        this.adsLoader.contentComplete();
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_ima_wrapper);
        this.getWindow().addFlags(1024);
        (this.videoPlayer = (IMAPlayer)this.findViewById(R.id.ima_player)).setCompleteCallback(this);
        final String stringExtra = this.getIntent().getStringExtra("url");
        this.mTrackListener = (AdTrackListener)this.getIntent().getSerializableExtra("track_listener");
        final ImaSdkFactory instance = ImaSdkFactory.getInstance();
        (this.adsLoader = instance.createAdsLoader((Context)this, instance.createImaSdkSettings())).addAdErrorListener(this);
        this.adsLoader.addAdsLoadedListener((AdsLoader.AdsLoadedListener)this);
        final AdDisplayContainer adDisplayContainer = instance.createAdDisplayContainer();
        adDisplayContainer.setPlayer(this.videoPlayer);
        adDisplayContainer.setAdContainer(this.videoPlayer.getUiContainer());
        final AdsRequest adsRequest = instance.createAdsRequest();
        adsRequest.setAdTagUrl(stringExtra);
        adsRequest.setAdDisplayContainer(adDisplayContainer);
        this.adsLoader.requestAds(adsRequest);
        if (this.mTrackListener != null) {
            this.mTrackListener.onIMARequest((Context)this);
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 4 || super.onKeyDown(n, keyEvent);
    }
    
    protected void onPause() {
        super.onPause();
        if (this.videoPlayer != null) {
            this.videoPlayer.savePosition();
        }
    }
    
    protected void onResume() {
        super.onResume();
        if (this.videoPlayer != null) {
            this.videoPlayer.restorePosition();
            if (this.isAdStarted) {
                this.videoPlayer.resumeAd();
            }
        }
    }
}
