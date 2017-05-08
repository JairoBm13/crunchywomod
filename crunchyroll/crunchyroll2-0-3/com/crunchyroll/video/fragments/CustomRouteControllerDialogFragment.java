// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.view.ViewGroup;
import android.app.Activity;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import com.google.common.base.Optional;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.util.Functional;
import com.crunchyroll.android.api.models.EpisodeInfo;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.widget.Button;
import com.crunchyroll.cast.CastHandler;
import android.support.v7.app.MediaRouteControllerDialog;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.MediaRouteControllerDialogFragment;

public class CustomRouteControllerDialogFragment extends MediaRouteControllerDialogFragment
{
    @Override
    public MediaRouteControllerDialog onCreateControllerDialog(final Context context, final Bundle bundle) {
        if (CastHandler.get().isShowingMedia()) {
            final CustomRouteControllerDialog customRouteControllerDialog = new CustomRouteControllerDialog(context);
            customRouteControllerDialog.setVolumeControlEnabled(false);
            return customRouteControllerDialog;
        }
        final MediaRouteControllerDialog mediaRouteControllerDialog = new MediaRouteControllerDialog(context) {
            @Override
            protected void onCreate(final Bundle bundle) {
                super.onCreate(bundle);
                this.getWindow().setBackgroundDrawableResource(2130837680);
                final Button button = (Button)this.findViewById(2131624318);
                button.setText((CharSequence)LocalizedStrings.DISCONNECT.get().toUpperCase());
                button.setTextColor(CustomRouteControllerDialogFragment.this.getResources().getColor(2131558477));
                final TextView textView = (TextView)this.findViewById(2131624307);
                textView.setTextColor(CustomRouteControllerDialogFragment.this.getResources().getColor(2131558537));
                textView.setCompoundDrawablesWithIntrinsicBounds(2130837720, 0, 0, 0);
                textView.setCompoundDrawablePadding(15);
            }
        };
        mediaRouteControllerDialog.setVolumeControlEnabled(false);
        return mediaRouteControllerDialog;
    }
    
    public class CustomRouteControllerDialog extends MediaRouteControllerDialog
    {
        private CastHandler mCastService;
        private CastHandler.OnMediaChangedListener mMediaChangedListener;
        private CastHandler.OnPlaybackStateChangedListener mPlaystateListener;
        
        public CustomRouteControllerDialog(final Context context) {
            super(context);
            this.mPlaystateListener = new CastHandler.OnPlaybackStateChangedListener() {
                @Override
                public void onPlaybackChanged(final int n) {
                    final View mediaControlView = CustomRouteControllerDialog.this.getMediaControlView();
                    if (mediaControlView != null) {
                        final ImageButton imageButton = (ImageButton)mediaControlView.findViewById(2131624135);
                        if (n != 3) {
                            imageButton.setImageResource(2130837700);
                            return;
                        }
                        imageButton.setImageResource(2130837704);
                    }
                }
                
                @Override
                public void onSuspendedStateChanged(final boolean b) {
                    final View mediaControlView = CustomRouteControllerDialog.this.getMediaControlView();
                    if (mediaControlView != null) {
                        final ImageButton imageButton = (ImageButton)mediaControlView.findViewById(2131624135);
                        if (imageButton != null) {
                            imageButton.setEnabled(!b);
                        }
                    }
                }
            };
            this.mMediaChangedListener = new CastHandler.OnMediaChangedListener() {
                @Override
                public void onMediaChanged() {
                    final View mediaControlView = CustomRouteControllerDialog.this.getMediaControlView();
                    if (mediaControlView != null) {
                        CustomRouteControllerDialog.this.setupCastContent(mediaControlView);
                    }
                }
            };
            this.mCastService = CastHandler.get();
        }
        
        private void setupCastContent(final View view) {
            ImageLoader.getInstance().displayImage(this.mCastService.getVideoImageUrl().toString(), (ImageView)view.findViewById(2131624016), CrunchyrollApplication.getDisplayImageOptions());
            final Optional<EpisodeInfo> currentlyCastingEpisode = CrunchyrollApplication.getApp(view.getContext()).getCurrentlyCastingEpisode();
            final TextView textView = (TextView)view.findViewById(2131624134);
            final TextView textView2 = (TextView)view.findViewById(2131624136);
            if (currentlyCastingEpisode.isPresent()) {
                textView.setText((CharSequence)currentlyCastingEpisode.get().getMedia().getSeriesName().get());
                textView2.setText((CharSequence)Functional.Media.getEpisodeSubtitle(currentlyCastingEpisode.get().getMedia()));
            }
            else {
                textView.setText((CharSequence)"");
                textView2.setText((CharSequence)this.mCastService.getVideoTitle());
            }
            final ImageButton imageButton = (ImageButton)view.findViewById(2131624135);
            if (this.mCastService.isPlaying()) {
                imageButton.setImageResource(2130837700);
            }
            else {
                imageButton.setImageResource(2130837704);
            }
            imageButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final ImageButton imageButton = (ImageButton)view;
                    if (CustomRouteControllerDialog.this.mCastService.isPlaying()) {
                        imageButton.setImageResource(2130837704);
                        CustomRouteControllerDialog.this.mCastService.pause();
                        return;
                    }
                    imageButton.setImageResource(2130837700);
                    CustomRouteControllerDialog.this.mCastService.resume();
                }
            });
            final Optional<EpisodeInfo> currentlyCastingEpisode2 = CrunchyrollApplication.getApp(view.getContext()).getCurrentlyCastingEpisode();
            if (currentlyCastingEpisode2.isPresent()) {
                view.setClickable(true);
                view.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        VideoPlayerActivity.start(CustomRouteControllerDialogFragment.this.getActivity(), currentlyCastingEpisode2.get(), false, 0);
                        CustomRouteControllerDialog.this.dismiss();
                    }
                });
                return;
            }
            view.setClickable(false);
        }
        
        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mCastService.registerOnPlaybackStateChangedListener(this.mPlaystateListener);
            this.mCastService.registerOnMediaImageChangedListener(this.mMediaChangedListener);
        }
        
        @Override
        protected void onCreate(final Bundle bundle) {
            super.onCreate(bundle);
            this.getWindow().setBackgroundDrawableResource(2130837680);
            final Button button = (Button)this.findViewById(2131624318);
            button.setText((CharSequence)LocalizedStrings.DISCONNECT.get().toUpperCase());
            button.setTextColor(CustomRouteControllerDialogFragment.this.getResources().getColor(2131558477));
            final TextView textView = (TextView)this.findViewById(2131624307);
            textView.setTextColor(CustomRouteControllerDialogFragment.this.getResources().getColor(2131558537));
            textView.setCompoundDrawablesWithIntrinsicBounds(2130837720, 0, 0, 0);
            textView.setCompoundDrawablePadding(15);
        }
        
        @Override
        public View onCreateMediaControlView(final Bundle bundle) {
            final View inflate = this.getLayoutInflater().inflate(2130903111, (ViewGroup)null);
            this.setupCastContent(inflate);
            return inflate;
        }
        
        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mCastService.unregisterOnPlaybackStateChangedListener(this.mPlaystateListener);
            this.mCastService.unregisterOnMediaImageChangedListener(this.mMediaChangedListener);
        }
    }
}
