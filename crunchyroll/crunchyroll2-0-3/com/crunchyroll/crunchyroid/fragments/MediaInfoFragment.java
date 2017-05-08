// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import java.io.Serializable;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import java.util.HashMap;
import com.crunchyroll.android.api.ApiManager;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.events.PopupCloseEvent;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.ActionsUtil;
import android.view.View;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.crunchyroll.android.api.models.Media;

public class MediaInfoFragment extends BaseFragment
{
    private Media mMedia;
    private long mMediaId;
    private ViewGroup mParent;
    
    private void finalizeUI() {
        final String episodeNumber = this.mMedia.getEpisodeNumber();
        final String name = this.mMedia.getName();
        boolean b;
        if (!TextUtils.isEmpty((CharSequence)name)) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (!TextUtils.isEmpty((CharSequence)episodeNumber)) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final View viewById = this.mParent.findViewById(2131624066);
        final TextView textView = (TextView)this.mParent.findViewById(2131624134);
        final TextView textView2 = (TextView)this.mParent.findViewById(2131624201);
        final TextView textView3 = (TextView)this.mParent.findViewById(2131624202);
        final TextView textView4 = (TextView)this.mParent.findViewById(2131624154);
        final TextView textView5 = (TextView)this.mParent.findViewById(2131624203);
        final TextView textView6 = (TextView)this.mParent.findViewById(2131624204);
        final View viewById2 = this.mParent.findViewById(2131624081);
        final ImageView imageView = (ImageView)this.mParent.findViewById(2131624016);
        viewById.setVisibility(0);
        textView.setText((CharSequence)this.mMedia.getSeriesName().orNull());
        if (this.mMedia.getEpisodeNumber() != null && this.mMedia.getEpisodeNumber().length() > 0) {
            textView2.setText((CharSequence)String.format(String.format(LocalizedStrings.EPISODE.get(), this.mMedia.getEpisodeNumber()), new Object[0]));
        }
        else {
            textView2.setVisibility(8);
        }
        if (b && b2) {
            textView3.setVisibility(0);
            textView3.setText((CharSequence)name);
        }
        else {
            textView3.setVisibility(8);
        }
        textView4.setText((CharSequence)this.mMedia.getDescription());
        textView5.setText((CharSequence)LocalizedStrings.SHARE.get());
        textView5.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ActionsUtil.share((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mMedia.getSeriesName().orNull(), MediaInfoFragment.this.mMedia.getEpisodeNumber(), MediaInfoFragment.this.mMedia.getUrl());
                Tracker.episodeInfoActions("share", MediaInfoFragment.this.mMedia.getSeriesName().or(""), "episode-" + MediaInfoFragment.this.mMedia.getEpisodeNumber());
            }
        });
        textView6.setText((CharSequence)LocalizedStrings.PLAY.get());
        textView6.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.episodeInfoPlay((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mMedia);
                Tracker.episodeInfoActions("play", MediaInfoFragment.this.mMedia.getSeriesName().or(""), "episode-" + MediaInfoFragment.this.mMedia.getEpisodeNumber());
                if (!CrunchyrollApplication.getApp((Context)MediaInfoFragment.this.getActivity()).isPrepareToWatchLoading()) {
                    final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)MediaInfoFragment.this.getActivity()).prepareToWatch(MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mMedia, false, 0);
                    prepareToWatch.prepare(new BaseListener<Void>() {
                        @Override
                        public void onException(final Exception ex) {
                            if (ex instanceof ApiNetworkException) {
                                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                                return;
                            }
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                        }
                        
                        @Override
                        public void onFinally() {
                            Util.hideProgressBar((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mParent);
                        }
                        
                        @Override
                        public void onPreExecute() {
                            Util.showProgressBar((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mParent, MediaInfoFragment.this.getResources().getColor(2131558518));
                        }
                        
                        @Override
                        public void onSuccess(final Void void1) {
                            prepareToWatch.go(PrepareToWatch.Event.NONE);
                        }
                    });
                }
            }
        });
        viewById2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new PopupCloseEvent());
                Tracker.episodeInfoActions("exit", MediaInfoFragment.this.mMedia.getSeriesName().or(""), "episode-" + MediaInfoFragment.this.mMedia.getEpisodeNumber());
            }
        });
        EpisodeListBaseFragment.loadLargeImageIntoImageView((Context)this.getActivity(), Optional.of(this.mMedia), imageView);
    }
    
    public static MediaInfoFragment newInstance(final long n) {
        final MediaInfoFragment mediaInfoFragment = new MediaInfoFragment();
        final Bundle arguments = new Bundle();
        Extras.putLong(arguments, "mediaId", Long.valueOf(n));
        mediaInfoFragment.setArguments(arguments);
        return mediaInfoFragment;
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        Tracker.browseMedia((Context)this.getActivity(), this.mMediaId);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mMediaId = Extras.getLong(this.getArguments(), "mediaId").get();
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mParent = (ViewGroup)layoutInflater.inflate(2130903120, viewGroup, false);
        if (bundle == null) {
            ApiManager.getInstance(this.getActivity()).getMediaInfoTask(this.mMediaId, new BaseListener<Media>() {
                @Override
                public void onException(final Exception ex) {
                    MediaInfoFragment.this.mParent.findViewById(2131624081).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            EventBus.getDefault().post(new PopupCloseEvent());
                        }
                    });
                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                }
                
                @Override
                public void onFinally() {
                    Util.hideProgressBarHideChildren((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mParent);
                }
                
                @Override
                public void onInterrupted(final Exception ex) {
                    this.onException(ex);
                }
                
                @Override
                public void onPreExecute() {
                    Util.showProgressBarHideChildren((Context)MediaInfoFragment.this.getActivity(), MediaInfoFragment.this.mParent, MediaInfoFragment.this.getResources().getColor(2131558536));
                }
                
                @Override
                public void onSuccess(final Media media) {
                    super.onSuccess(media);
                    MediaInfoFragment.this.mMedia = media;
                    MediaInfoFragment.this.finalizeUI();
                    MediaInfoFragment.this.trackViewWithExtras(MediaInfoFragment.this.mMedia.getSeriesName().get(), "episode_" + media.getEpisodeNumber());
                    if (MediaInfoFragment.this.mMedia.getMediaType().or("").equalsIgnoreCase("anime")) {
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("series", MediaInfoFragment.this.mMedia.getSeriesName().or(""));
                        hashMap.put("series-episode", "episode-" + media.getEpisodeNumber());
                        Tracker.swrveScreenView("anime-episode-info", hashMap);
                    }
                    else if (MediaInfoFragment.this.mMedia.getMediaType().or("").equalsIgnoreCase("drama")) {
                        final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                        hashMap2.put("series", MediaInfoFragment.this.mMedia.getSeriesName().or(""));
                        hashMap2.put("series-episode", "episode-" + media.getEpisodeNumber());
                        Tracker.swrveScreenView("drama-episode-info", hashMap2);
                    }
                }
            });
        }
        else {
            this.mMedia = (Media)bundle.getSerializable("media");
            this.trackViewWithExtras(this.mMedia.getSeriesName().get(), "episode_" + this.mMedia.getEpisodeNumber());
            this.finalizeUI();
        }
        return (View)this.mParent;
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("media", (Serializable)this.mMedia);
    }
}
