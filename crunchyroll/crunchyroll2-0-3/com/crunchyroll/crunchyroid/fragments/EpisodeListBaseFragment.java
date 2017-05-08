// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import java.util.Calendar;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import java.io.Serializable;
import java.text.ParseException;
import com.crunchyroll.crunchyroid.util.Util;
import java.util.GregorianCalendar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.crunchyroll.android.api.models.ImageSet;
import com.crunchyroll.crunchyroid.util.Functional;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.widget.ImageView;
import com.crunchyroll.android.api.models.Media;
import com.google.common.base.Optional;
import android.content.Context;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;

public abstract class EpisodeListBaseFragment extends BaseFragment implements OnRefreshListener
{
    private static final int LARGE_THUMBNAIL_WIDTH_THRESHOLD = 400;
    private SharedPreferences$OnSharedPreferenceChangeListener mPreferenceChangedListener;
    
    public static void loadImageIntoImageView(final Context context, final Optional<Media> optional, final ImageView imageView, final Integer n) {
        loadImageIntoImageView(context, optional, imageView, n, false);
    }
    
    private static void loadImageIntoImageView(final Context context, final Optional<Media> optional, final ImageView imageView, Integer n, boolean imageLoadingEnabled) {
        if (context != null) {
            boolean b = imageLoadingEnabled;
            if (n != null) {
                if (n > 400 || imageLoadingEnabled) {
                    b = true;
                }
                else {
                    b = false;
                }
            }
            imageLoadingEnabled = CrunchyrollApplication.getApp(context).getApplicationState().isImageLoadingEnabled();
            final Optional<ImageSet> screenshot = Functional.Media.getScreenshot(optional);
            Label_0253: {
                if (!imageLoadingEnabled || !screenshot.isPresent()) {
                    break Label_0253;
                }
                imageLoadingEnabled = (optional.isPresent() && optional.get().isFreeAvailable().or(Boolean.valueOf(false)));
                Optional<String> optional2;
                if (b && screenshot.get().getfWideUrl().isPresent()) {
                    optional2 = screenshot.get().getfWideUrl();
                }
                else {
                    optional2 = screenshot.get().getWideUrl();
                }
                if (optional2.isPresent()) {
                    ImageLoader.getInstance().displayImage(optional2.get(), imageView, CrunchyrollApplication.getDisplayImageOptionsPlaceholderWide());
                    return;
                }
                final GregorianCalendar gregorianCalendar = new GregorianCalendar();
                Serializable calendar;
                n = (Integer)(calendar = null);
                while (true) {
                    try {
                        if (optional.get().getAvailableTime() != null) {
                            calendar = Util.toCalendar(optional.get().getAvailableTime());
                        }
                        if (calendar != null && ((Calendar)calendar).after(gregorianCalendar)) {
                            imageView.setImageResource(2130837815);
                            return;
                        }
                        if (imageLoadingEnabled) {
                            imageView.setImageResource(2130837816);
                            return;
                        }
                        imageView.setImageResource(2130837817);
                        return;
                        imageView.setImageResource(2130837819);
                    }
                    catch (ParseException ex) {
                        calendar = n;
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public static void loadLargeImageIntoImageView(final Context context, final Optional<Media> optional, final ImageView imageView) {
        loadImageIntoImageView(context, optional, imageView, null, true);
    }
    
    public static void loadSmallImageIntoImageView(final Context context, final Optional<Media> optional, final ImageView imageView) {
        loadImageIntoImageView(context, optional, imageView, null, false);
    }
    
    protected View inflateLoadingView(final ViewGroup viewGroup) {
        final View inflate = this.getLayoutInflater(this.getArguments()).inflate(2130903153, viewGroup, false);
        inflate.getLayoutParams().height = this.getResources().getDimensionPixelSize(2131230729);
        return inflate;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mPreferenceChangedListener = (SharedPreferences$OnSharedPreferenceChangeListener)new SharedPreferences$OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
                if (s.equals("imageLoadingEnabled")) {
                    EpisodeListBaseFragment.this.onLoadImagesSettingChanged();
                }
            }
        };
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).registerOnSharedPreferenceChangeListener(this.mPreferenceChangedListener);
    }
    
    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).unregisterOnSharedPreferenceChangeListener(this.mPreferenceChangedListener);
        super.onDestroy();
    }
    
    protected void onLoadImagesSettingChanged() {
    }
}
