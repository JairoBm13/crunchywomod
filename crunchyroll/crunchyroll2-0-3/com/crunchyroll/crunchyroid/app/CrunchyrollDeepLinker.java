// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import java.io.Serializable;
import android.os.Build$VERSION;
import android.content.Context;
import android.content.Intent;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.secondtv.android.ads.DeepLinker;

public class CrunchyrollDeepLinker extends DeepLinker
{
    public static final Parcelable$Creator<CrunchyrollDeepLinker> CREATOR;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<CrunchyrollDeepLinker>() {
            public CrunchyrollDeepLinker createFromParcel(final Parcel parcel) {
                return new CrunchyrollDeepLinker(parcel, null);
            }
            
            public CrunchyrollDeepLinker[] newArray(final int n) {
                return new CrunchyrollDeepLinker[n];
            }
        };
    }
    
    public CrunchyrollDeepLinker() {
    }
    
    private CrunchyrollDeepLinker(final Parcel parcel) {
    }
    
    private void goToMain(final Activity activity, final boolean b, final MainActivity.Type type, final String s) {
        final Intent intent = new Intent((Context)activity, (Class)MainActivity.class);
        intent.setFlags(268468224);
        if (Build$VERSION.SDK_INT < 16) {
            intent.setFlags(65536);
        }
        intent.putExtra("mainType", (Serializable)type);
        intent.putExtra("filter", s);
        activity.startActivity(intent);
        if (b) {
            activity.finish();
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean open(final FragmentActivity fragmentActivity, final boolean b, final Uri uri, final boolean b2) {
        if (uri != null && "crunchyroll".equalsIgnoreCase(uri.getScheme())) {
            if ("playmedia".equalsIgnoreCase(uri.getAuthority())) {
                try {
                    ApiManager.getInstance(fragmentActivity).getMediaInfoTask((long)Long.valueOf(Extras.getUriValue(uri, 0)), new PlayMediaListener(fragmentActivity, b));
                    return true;
                }
                catch (Exception ex) {
                    return false;
                }
            }
            if ("media".equalsIgnoreCase(uri.getAuthority())) {
                try {
                    ApiManager.getInstance(fragmentActivity).getMediaInfoTask((long)Long.valueOf(Extras.getUriValue(uri, 0)), new MediaInfoListener(fragmentActivity, b));
                    return true;
                }
                catch (Exception ex2) {
                    return false;
                }
            }
            if ("series".equalsIgnoreCase(uri.getAuthority())) {
                try {
                    ApiManager.getInstance(fragmentActivity).getSeriesInfo((long)Long.valueOf(Extras.getUriValue(uri, 0)), new SeriesListener(fragmentActivity, b, b2));
                    return true;
                }
                catch (Exception ex3) {
                    return false;
                }
            }
            if ("queue".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_QUEUE, null);
                return true;
            }
            if ("history".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_HISTORY, null);
                return true;
            }
            if ("create_account".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_CREATE_ACCOUNT, null);
                return true;
            }
            if ("upsell".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_UPSELL, null);
                return true;
            }
            if ("filter".equalsIgnoreCase(uri.getAuthority())) {
                Label_0412: {
                    try {
                        final String uriValue = Extras.getUriValue(uri, 0);
                        final String uriValue2 = Extras.getUriValue(uri, 1);
                        String addTag = "popular";
                        if ("alpha".equalsIgnoreCase(uriValue2)) {
                            addTag = "alpha";
                        }
                        else if ("seasons".equalsIgnoreCase(uriValue2) || "genres".equalsIgnoreCase(uriValue2)) {
                            addTag = Filters.addTag(Extras.getUriValue(uri, 2));
                        }
                        if ("anime".equalsIgnoreCase(uriValue)) {
                            this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_ANIME, addTag);
                        }
                        else {
                            if (!"drama".equalsIgnoreCase(uriValue)) {
                                break Label_0412;
                            }
                            this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_DRAMA, addTag);
                        }
                    }
                    catch (Exception ex4) {
                        this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_NORMAL, null);
                    }
                    return true;
                }
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_NORMAL, null);
                return true;
            }
            if ("this_season".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_THIS_SEASON, null);
                return true;
            }
            if ("updated".equalsIgnoreCase(uri.getAuthority())) {
                this.goToMain(fragmentActivity, b, MainActivity.Type.TYPE_UPDATED, null);
                return true;
            }
        }
        return false;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
    }
    
    private class MediaInfoListener extends BaseListener<Media>
    {
        private FragmentActivity mActivity;
        private boolean mIsFinishActivity;
        
        public MediaInfoListener(final FragmentActivity mActivity, final boolean mIsFinishActivity) {
            this.mActivity = mActivity;
            this.mIsFinishActivity = mIsFinishActivity;
        }
        
        @Override
        public void onException(final Exception ex) {
            final Intent intent = new Intent((Context)this.mActivity, (Class)MainActivity.class);
            intent.setFlags(268468224);
            if (Build$VERSION.SDK_INT < 16) {
                intent.setFlags(65536);
            }
            this.mActivity.startActivity(intent);
        }
        
        @Override
        public void onFinally() {
            super.onFinally();
            if (this.mIsFinishActivity) {
                this.mActivity.finish();
            }
        }
        
        @Override
        public void onSuccess(final Media media) {
            SeriesDetailActivity.startMediaInfo((Context)this.mActivity, (long)media.getSeriesId(), 1, media.getMediaId());
        }
    }
    
    private class PlayMediaListener extends BaseListener<Media>
    {
        private FragmentActivity mActivity;
        private boolean mIsFinishActivity;
        
        public PlayMediaListener(final FragmentActivity mActivity, final boolean mIsFinishActivity) {
            this.mActivity = mActivity;
            this.mIsFinishActivity = mIsFinishActivity;
        }
        
        @Override
        public void onException(final Exception ex) {
            final Intent intent = new Intent((Context)this.mActivity, (Class)MainActivity.class);
            intent.setFlags(268468224);
            if (Build$VERSION.SDK_INT < 16) {
                intent.setFlags(65536);
            }
            this.mActivity.startActivity(intent);
        }
        
        @Override
        public void onFinally() {
            super.onFinally();
            if (this.mIsFinishActivity) {
                this.mActivity.finish();
            }
        }
        
        @Override
        public void onSuccess(final Media media) {
            CrunchyrollApplication.getApp((Context)this.mActivity).prepareToWatch(this.mActivity, media, false, 1);
            SeriesDetailActivity.start((Context)this.mActivity, (long)media.getSeriesId(), 1, true);
        }
    }
    
    private class SeriesListener extends BaseListener<Series>
    {
        private Activity mActivity;
        private boolean mIsBackable;
        private boolean mIsFinishActivity;
        
        public SeriesListener(final Activity mActivity, final boolean mIsFinishActivity, final boolean mIsBackable) {
            this.mActivity = mActivity;
            this.mIsFinishActivity = mIsFinishActivity;
            this.mIsBackable = mIsBackable;
        }
        
        @Override
        public void onException(final Exception ex) {
            final Intent intent = new Intent((Context)this.mActivity, (Class)MainActivity.class);
            intent.setFlags(268468224);
            if (Build$VERSION.SDK_INT < 16) {
                intent.setFlags(65536);
            }
            this.mActivity.startActivity(intent);
        }
        
        @Override
        public void onFinally() {
            super.onFinally();
            if (this.mIsFinishActivity) {
                this.mActivity.finish();
            }
        }
        
        @Override
        public void onSuccess(final Series series) {
            int n = 1;
            if (this.mIsBackable) {
                n = 2;
            }
            SeriesDetailActivity.start((Context)this.mActivity, series.getSeriesId(), n, false);
        }
    }
}
