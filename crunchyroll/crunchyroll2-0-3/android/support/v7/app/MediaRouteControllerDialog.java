// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.content.IntentSender;
import android.os.Handler;
import android.content.IntentSender$OnFinished;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.SeekBar$OnSeekBarChangeListener;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.support.v7.media.MediaRouteSelector;
import android.text.TextUtils;
import android.support.v4.media.MediaMetadataCompat;
import android.os.RemoteException;
import android.util.Log;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.mediarouter.R;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.LinearLayout;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.TextView;
import android.support.v7.media.MediaRouter;
import android.widget.ImageButton;
import android.graphics.drawable.Drawable;
import android.support.v4.media.session.MediaControllerCompat;
import android.widget.Button;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.View;
import android.widget.ImageView;
import android.app.Dialog;

public class MediaRouteControllerDialog extends Dialog
{
    private static final String TAG = "MediaRouteControllerDialog";
    private static final int VOLUME_UPDATE_DELAY_MILLIS = 250;
    private ImageView mArtView;
    private boolean mAttachedToWindow;
    private final MediaRouterCallback mCallback;
    private View mControlView;
    private MediaControllerCallback mControllerCallback;
    private boolean mCreated;
    private MediaDescriptionCompat mDescription;
    private Button mDisconnectButton;
    private MediaControllerCompat mMediaController;
    private Drawable mMediaRouteConnectingDrawable;
    private Drawable mMediaRouteOnDrawable;
    private ImageButton mPlayPauseButton;
    private final MediaRouter.RouteInfo mRoute;
    private TextView mRouteNameView;
    private final MediaRouter mRouter;
    private ImageButton mSettingsButton;
    private PlaybackStateCompat mState;
    private Button mStopCastingButton;
    private TextView mSubtitleView;
    private TextView mTitleView;
    private View mTitlesWrapper;
    private boolean mVolumeControlEnabled;
    private LinearLayout mVolumeLayout;
    private SeekBar mVolumeSlider;
    private boolean mVolumeSliderTouched;
    
    public MediaRouteControllerDialog(final Context context) {
        this(context, 0);
    }
    
    public MediaRouteControllerDialog(Context context, final int n) {
        super(MediaRouterThemeHelper.createThemedContext(context), n);
        this.mVolumeControlEnabled = true;
        context = this.getContext();
        this.mControllerCallback = new MediaControllerCallback();
        this.mRouter = MediaRouter.getInstance(context);
        this.mCallback = new MediaRouterCallback();
        this.mRoute = this.mRouter.getSelectedRoute();
        this.setMediaSession(this.mRouter.getMediaSessionToken());
    }
    
    private Drawable getIconDrawable() {
        if (this.mRoute.isConnecting()) {
            if (this.mMediaRouteConnectingDrawable == null) {
                this.mMediaRouteConnectingDrawable = MediaRouterThemeHelper.getThemeDrawable(this.getContext(), R.attr.mediaRouteConnectingDrawable);
            }
            return this.mMediaRouteConnectingDrawable;
        }
        if (this.mMediaRouteOnDrawable == null) {
            this.mMediaRouteOnDrawable = MediaRouterThemeHelper.getThemeDrawable(this.getContext(), R.attr.mediaRouteOnDrawable);
        }
        return this.mMediaRouteOnDrawable;
    }
    
    private boolean isVolumeControlAvailable() {
        return this.mVolumeControlEnabled && this.mRoute.getVolumeHandling() == 1;
    }
    
    private void setMediaSession(final MediaSessionCompat.Token token) {
        final PlaybackStateCompat playbackStateCompat = null;
        if (this.mMediaController != null) {
            this.mMediaController.unregisterCallback((MediaControllerCompat.Callback)this.mControllerCallback);
            this.mMediaController = null;
        }
        if (token == null || !this.mAttachedToWindow) {
            return;
        }
        MediaMetadataCompat metadata = null;
        MediaDescriptionCompat description;
        PlaybackStateCompat playbackState;
        Label_0080_Outer:Label_0086_Outer:Label_0100_Outer:
        while (true) {
            while (true) {
            Label_0143:
                while (true) {
                Label_0135:
                    while (true) {
                        while (true) {
                            try {
                                this.mMediaController = new MediaControllerCompat(this.getContext(), token);
                                if (this.mMediaController != null) {
                                    this.mMediaController.registerCallback((MediaControllerCompat.Callback)this.mControllerCallback);
                                }
                                if (this.mMediaController == null) {
                                    metadata = null;
                                    if (metadata != null) {
                                        break Label_0135;
                                    }
                                    description = null;
                                    this.mDescription = description;
                                    if (this.mMediaController == null) {
                                        playbackState = playbackStateCompat;
                                        this.mState = playbackState;
                                        this.update();
                                        return;
                                    }
                                    break Label_0143;
                                }
                            }
                            catch (RemoteException ex) {
                                Log.e("MediaRouteControllerDialog", "Error creating media controller in setMediaSession.", (Throwable)ex);
                                continue Label_0080_Outer;
                            }
                            break;
                        }
                        metadata = this.mMediaController.getMetadata();
                        continue Label_0086_Outer;
                    }
                    description = metadata.getDescription();
                    continue Label_0100_Outer;
                }
                playbackState = this.mMediaController.getPlaybackState();
                continue;
            }
        }
    }
    
    private boolean update() {
        if (!this.mRoute.isSelected() || this.mRoute.isDefault()) {
            this.dismiss();
            return false;
        }
        if (!this.mCreated) {
            return false;
        }
        this.updateVolume();
        this.mRouteNameView.setText((CharSequence)this.mRoute.getName());
        if (this.mRoute.canDisconnect()) {
            this.mDisconnectButton.setVisibility(0);
        }
        else {
            this.mDisconnectButton.setVisibility(8);
        }
        if (this.mRoute.getSettingsIntent() != null) {
            this.mSettingsButton.setVisibility(0);
        }
        else {
            this.mSettingsButton.setVisibility(8);
        }
        if (this.mControlView == null) {
            if (this.mDescription != null) {
                if (this.mDescription.getIconBitmap() != null) {
                    this.mArtView.setImageBitmap(this.mDescription.getIconBitmap());
                    this.mArtView.setVisibility(0);
                }
                else if (this.mDescription.getIconUri() != null) {
                    this.mArtView.setImageURI(this.mDescription.getIconUri());
                    this.mArtView.setVisibility(0);
                }
                else {
                    this.mArtView.setImageDrawable((Drawable)null);
                    this.mArtView.setVisibility(8);
                }
                boolean b = false;
                final CharSequence title = this.mDescription.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    this.mTitleView.setText(title);
                    b = true;
                }
                else {
                    this.mTitleView.setText((CharSequence)null);
                    this.mTitleView.setVisibility(8);
                }
                if (!TextUtils.isEmpty(this.mDescription.getSubtitle())) {
                    this.mSubtitleView.setText(this.mDescription.getSubtitle());
                    b = true;
                }
                else {
                    this.mSubtitleView.setText((CharSequence)null);
                    this.mSubtitleView.setVisibility(8);
                }
                if (!b) {
                    this.mTitlesWrapper.setVisibility(8);
                }
                else {
                    this.mTitlesWrapper.setVisibility(0);
                }
            }
            else {
                this.mArtView.setVisibility(8);
                this.mTitlesWrapper.setVisibility(8);
            }
            if (this.mState != null) {
                boolean b2;
                if (this.mState.getState() == 6 || this.mState.getState() == 3) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                final boolean b3 = (this.mState.getActions() & 0x204L) != 0x0L;
                boolean b4;
                if ((this.mState.getActions() & 0x202L) != 0x0L) {
                    b4 = true;
                }
                else {
                    b4 = false;
                }
                if (b2 && b4) {
                    this.mPlayPauseButton.setVisibility(0);
                    this.mPlayPauseButton.setImageResource(MediaRouterThemeHelper.getThemeResource(this.getContext(), R.attr.mediaRoutePauseDrawable));
                    this.mPlayPauseButton.setContentDescription(this.getContext().getResources().getText(R.string.mr_media_route_controller_pause));
                }
                else if (!b2 && b3) {
                    this.mPlayPauseButton.setVisibility(0);
                    this.mPlayPauseButton.setImageResource(MediaRouterThemeHelper.getThemeResource(this.getContext(), R.attr.mediaRoutePlayDrawable));
                    this.mPlayPauseButton.setContentDescription(this.getContext().getResources().getText(R.string.mr_media_route_controller_play));
                }
                else {
                    this.mPlayPauseButton.setVisibility(8);
                }
            }
            else {
                this.mPlayPauseButton.setVisibility(8);
            }
        }
        return true;
    }
    
    private void updateVolume() {
        if (!this.mVolumeSliderTouched) {
            if (!this.isVolumeControlAvailable()) {
                this.mVolumeLayout.setVisibility(8);
                return;
            }
            this.mVolumeLayout.setVisibility(0);
            this.mVolumeSlider.setMax(this.mRoute.getVolumeMax());
            this.mVolumeSlider.setProgress(this.mRoute.getVolume());
        }
    }
    
    public View getMediaControlView() {
        return this.mControlView;
    }
    
    public MediaSessionCompat.Token getMediaSession() {
        if (this.mMediaController == null) {
            return null;
        }
        return this.mMediaController.getSessionToken();
    }
    
    public MediaRouter.RouteInfo getRoute() {
        return this.mRoute;
    }
    
    public boolean isVolumeControlEnabled() {
        return this.mVolumeControlEnabled;
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(MediaRouteSelector.EMPTY, (MediaRouter.Callback)this.mCallback, 2);
        this.setMediaSession(this.mRouter.getMediaSessionToken());
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().requestFeature(1);
        this.setContentView(R.layout.mr_media_route_controller_material_dialog_b);
        final ClickListener clickListener = new ClickListener();
        (this.mDisconnectButton = (Button)this.findViewById(R.id.disconnect)).setOnClickListener((View$OnClickListener)clickListener);
        (this.mStopCastingButton = (Button)this.findViewById(R.id.stop)).setOnClickListener((View$OnClickListener)clickListener);
        (this.mSettingsButton = (ImageButton)this.findViewById(R.id.settings)).setOnClickListener((View$OnClickListener)clickListener);
        this.mArtView = (ImageView)this.findViewById(R.id.art);
        this.mTitleView = (TextView)this.findViewById(R.id.title);
        this.mSubtitleView = (TextView)this.findViewById(R.id.subtitle);
        this.mTitlesWrapper = this.findViewById(R.id.text_wrapper);
        (this.mPlayPauseButton = (ImageButton)this.findViewById(R.id.play_pause)).setOnClickListener((View$OnClickListener)clickListener);
        this.mRouteNameView = (TextView)this.findViewById(R.id.route_name);
        this.mVolumeLayout = (LinearLayout)this.findViewById(R.id.media_route_volume_layout);
        (this.mVolumeSlider = (SeekBar)this.findViewById(R.id.media_route_volume_slider)).setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            private final Runnable mStopTrackingTouch = new Runnable() {
                @Override
                public void run() {
                    if (MediaRouteControllerDialog.this.mVolumeSliderTouched) {
                        MediaRouteControllerDialog.this.mVolumeSliderTouched = false;
                        MediaRouteControllerDialog.this.updateVolume();
                    }
                }
            };
            
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                if (b) {
                    MediaRouteControllerDialog.this.mRoute.requestSetVolume(n);
                }
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
                if (MediaRouteControllerDialog.this.mVolumeSliderTouched) {
                    MediaRouteControllerDialog.this.mVolumeSlider.removeCallbacks(this.mStopTrackingTouch);
                    return;
                }
                MediaRouteControllerDialog.this.mVolumeSliderTouched = true;
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                MediaRouteControllerDialog.this.mVolumeSlider.postDelayed(this.mStopTrackingTouch, 250L);
            }
        });
        this.mCreated = true;
        if (this.update()) {
            this.mControlView = this.onCreateMediaControlView(bundle);
            final FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.media_route_control_frame);
            if (this.mControlView != null) {
                frameLayout.findViewById(R.id.default_control_frame).setVisibility(8);
                frameLayout.addView(this.mControlView);
            }
        }
    }
    
    public View onCreateMediaControlView(final Bundle bundle) {
        return null;
    }
    
    public void onDetachedFromWindow() {
        this.mRouter.removeCallback((MediaRouter.Callback)this.mCallback);
        this.setMediaSession(null);
        this.mAttachedToWindow = false;
        super.onDetachedFromWindow();
    }
    
    public boolean onKeyDown(int n, final KeyEvent keyEvent) {
        if (n == 25 || n == 24) {
            final MediaRouter.RouteInfo mRoute = this.mRoute;
            if (n == 25) {
                n = -1;
            }
            else {
                n = 1;
            }
            mRoute.requestUpdateVolume(n);
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return n == 25 || n == 24 || super.onKeyUp(n, keyEvent);
    }
    
    public void setVolumeControlEnabled(final boolean mVolumeControlEnabled) {
        if (this.mVolumeControlEnabled != mVolumeControlEnabled) {
            this.mVolumeControlEnabled = mVolumeControlEnabled;
            if (this.mCreated) {
                this.updateVolume();
            }
        }
    }
    
    private final class ClickListener implements View$OnClickListener
    {
        public void onClick(final View view) {
            final int id = view.getId();
            if (id == R.id.stop || id == R.id.disconnect) {
                if (MediaRouteControllerDialog.this.mRoute.isSelected()) {
                    final MediaRouter access$1200 = MediaRouteControllerDialog.this.mRouter;
                    int n;
                    if (id == R.id.stop) {
                        n = 2;
                    }
                    else {
                        n = 1;
                    }
                    access$1200.unselect(n);
                }
                MediaRouteControllerDialog.this.dismiss();
            }
            else if (id == R.id.play_pause) {
                if (MediaRouteControllerDialog.this.mMediaController != null && MediaRouteControllerDialog.this.mState != null) {
                    if (MediaRouteControllerDialog.this.mState.getState() == 3) {
                        MediaRouteControllerDialog.this.mMediaController.getTransportControls().pause();
                        return;
                    }
                    MediaRouteControllerDialog.this.mMediaController.getTransportControls().play();
                }
            }
            else if (id == R.id.settings) {
                final IntentSender settingsIntent = MediaRouteControllerDialog.this.mRoute.getSettingsIntent();
                if (settingsIntent != null) {
                    try {
                        settingsIntent.sendIntent((Context)null, 0, (Intent)null, (IntentSender$OnFinished)null, (Handler)null);
                        MediaRouteControllerDialog.this.dismiss();
                    }
                    catch (Exception ex) {
                        Log.e("MediaRouteControllerDialog", "Error opening route settings.", (Throwable)ex);
                    }
                }
            }
        }
    }
    
    private final class MediaControllerCallback extends Callback
    {
        @Override
        public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) {
            final MediaRouteControllerDialog this$0 = MediaRouteControllerDialog.this;
            MediaDescriptionCompat description;
            if (mediaMetadataCompat == null) {
                description = null;
            }
            else {
                description = mediaMetadataCompat.getDescription();
            }
            this$0.mDescription = description;
            MediaRouteControllerDialog.this.update();
        }
        
        @Override
        public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) {
            MediaRouteControllerDialog.this.mState = playbackStateCompat;
            MediaRouteControllerDialog.this.update();
        }
        
        @Override
        public void onSessionDestroyed() {
            if (MediaRouteControllerDialog.this.mMediaController != null) {
                MediaRouteControllerDialog.this.mMediaController.unregisterCallback((MediaControllerCompat.Callback)MediaRouteControllerDialog.this.mControllerCallback);
                MediaRouteControllerDialog.this.mMediaController = null;
            }
        }
    }
    
    private final class MediaRouterCallback extends Callback
    {
        @Override
        public void onRouteChanged(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            MediaRouteControllerDialog.this.update();
        }
        
        @Override
        public void onRouteUnselected(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            MediaRouteControllerDialog.this.update();
        }
        
        @Override
        public void onRouteVolumeChanged(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            if (routeInfo == MediaRouteControllerDialog.this.mRoute) {
                MediaRouteControllerDialog.this.updateVolume();
            }
        }
    }
}
