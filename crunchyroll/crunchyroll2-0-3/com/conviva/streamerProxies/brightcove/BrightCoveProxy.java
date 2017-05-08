// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.streamerProxies.brightcove;

import java.util.Map;
import java.util.HashMap;
import com.conviva.StreamerError;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventListener;
import com.conviva.utils.PlatformUtils;
import com.brightcove.player.view.SeamlessVideoView;
import com.brightcove.player.view.BrightcoveVideoView;
import com.conviva.monitor.IMonitorNotifier;
import com.conviva.ConvivaStreamerProxy;

public class BrightCoveProxy implements ConvivaStreamerProxy
{
    private boolean HLS_PLAYER;
    protected int _duration;
    private String _mVersion;
    private IMonitorNotifier _notifier;
    protected boolean _phtUpdated;
    private boolean _phtref;
    protected long _previousPosition;
    private int _previousState;
    private long _previousTime;
    private BrightcoveVideoView _streamer_brightcove;
    private SeamlessVideoView _streamer_seamless;
    protected PlatformUtils _utils;
    private boolean isPaused;
    
    public BrightCoveProxy(final Object o, final String mVersion, final boolean hls_PLAYER) throws Exception {
        this._utils = null;
        this._duration = -1;
        this._previousPosition = -1L;
        this._phtUpdated = false;
        this._streamer_seamless = null;
        this._streamer_brightcove = null;
        this._notifier = null;
        this._mVersion = null;
        this.HLS_PLAYER = false;
        this._previousTime = 0L;
        this._phtref = false;
        this.isPaused = false;
        this._previousState = 100;
        final EventListener eventListener = (EventListener)new EventListener() {
            public void processEvent(final Event event) {
                if (BrightCoveProxy.this._notifier != null) {
                    if (event.getType().equals("didPause")) {
                        BrightCoveProxy.this._notifier.SetPlayingState(12);
                        BrightCoveProxy.this._previousState = 12;
                    }
                    if (event.getType().equals("didStop")) {
                        BrightCoveProxy.this._notifier.SetPlayingState(1);
                        BrightCoveProxy.this._previousState = 1;
                    }
                    if (event.getType().equals("completed")) {
                        BrightCoveProxy.this._notifier.SetPlayingState(1);
                        BrightCoveProxy.this._previousState = 1;
                    }
                    if (event.getType().equals("didSetSource")) {
                        BrightCoveProxy.this._notifier.SetPlayingState(6);
                    }
                    if (event.getType().equals("sourceNotPlayable") && BrightCoveProxy.this._notifier != null) {
                        BrightCoveProxy.this._notifier.OnError(StreamerError.makeUnscopedError("sourceNotPlayable", 1));
                    }
                    if (event.getType().equals("sourceNotFound") && BrightCoveProxy.this._notifier != null) {
                        BrightCoveProxy.this._notifier.OnError(StreamerError.makeUnscopedError("sourceNotFound", 1));
                    }
                    if (event.getType().equals("bitrateChanged")) {
                        BrightCoveProxy.this._notifier.SetStream(event.getIntegerProperty("bitrate") / 1024, null, null);
                    }
                }
            }
        };
        this._utils = PlatformUtils.getInstance();
        this._mVersion = mVersion;
        this.HLS_PLAYER = hls_PLAYER;
        this._duration = -1;
        if (this.HLS_PLAYER) {
            this._streamer_seamless = (SeamlessVideoView)o;
            if (this._streamer_seamless != null) {
                this._streamer_seamless.getEventEmitter().on("didStop", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("didPause", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("error", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("sourceNotPlayable", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("sourceNotFound", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("bufferedUpdate", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("didSetSource", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("completed", (EventListener)eventListener);
                this._streamer_seamless.getEventEmitter().on("bitrateChanged", (EventListener)eventListener);
            }
        }
        else {
            this._streamer_brightcove = (BrightcoveVideoView)o;
            if (this._streamer_brightcove != null) {
                this._streamer_brightcove.getEventEmitter().on("didStop", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("didPause", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("error", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("sourceNotPlayable", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("sourceNotFound", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("bufferedUpdate", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("didSetSource", (EventListener)eventListener);
                this._streamer_brightcove.getEventEmitter().on("completed", (EventListener)eventListener);
            }
        }
    }
    
    private void updateState(final int previousState) {
        if (this._previousState != previousState) {
            this._previousState = previousState;
            this._notifier.SetPlayingState(previousState);
        }
    }
    
    @Override
    public void Cleanup() {
    }
    
    @Override
    public int GetBufferLengthMs() {
        return -1;
    }
    
    @Override
    public int GetCapabilities() {
        if (this.HLS_PLAYER) {
            return 7;
        }
        return 3;
    }
    
    @Override
    public int GetDroppedFrames() {
        return -1;
    }
    
    @Override
    public int GetIsLive() {
        return 0;
    }
    
    @Override
    public int GetMinBufferLengthMs() {
        return 0;
    }
    
    @Override
    public int GetPlayheadTimeMs() {
        try {
            if (this.HLS_PLAYER) {
                if (this._streamer_seamless != null) {
                    final long previousPosition = this._streamer_seamless.getCurrentPosition();
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (this._streamer_seamless.isPlaying()) {
                        if (this._phtref && currentTimeMillis - this._previousTime <= 600L) {
                            return (int)previousPosition;
                        }
                        this._phtref = true;
                        this._previousTime = currentTimeMillis;
                        if (previousPosition != this._previousPosition) {
                            this._previousPosition = previousPosition;
                            if (this._phtUpdated && this._previousState != 3) {
                                this.updateState(3);
                            }
                            this._phtUpdated = true;
                        }
                        else {
                            if (this._previousState != 6) {
                                this.updateState(6);
                            }
                            this._phtUpdated = true;
                        }
                    }
                    else if (this.isPaused) {
                        this.updateState(12);
                        this.isPaused = false;
                    }
                    if (previousPosition > 0L && this._duration == -1) {
                        this._duration = this._streamer_seamless.getDuration();
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("duration", String.valueOf(this._duration));
                        this._notifier.OnMetadata(hashMap);
                    }
                    return (int)previousPosition;
                }
            }
            else {
                if (this._streamer_brightcove == null) {
                    return (int)(-1L);
                }
                final long previousPosition2 = this._streamer_brightcove.getCurrentPosition();
                final long currentTimeMillis2 = System.currentTimeMillis();
                if (this._streamer_brightcove.isPlaying()) {
                    if (this._phtref && currentTimeMillis2 - this._previousTime <= 600L) {
                        return (int)previousPosition2;
                    }
                    this._phtref = true;
                    this._previousTime = currentTimeMillis2;
                    if (previousPosition2 != this._previousPosition) {
                        this._previousPosition = previousPosition2;
                        if (this._phtUpdated && this._previousState != 3) {
                            this.updateState(3);
                        }
                        this._phtUpdated = true;
                    }
                    else {
                        if (this._previousState != 6) {
                            this.updateState(6);
                        }
                        this._phtUpdated = true;
                    }
                }
                else if (this.isPaused) {
                    this.updateState(12);
                    this.isPaused = false;
                }
                if (previousPosition2 > 0L && this._duration == -1) {
                    this._duration = this._streamer_brightcove.getDuration();
                    final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                    hashMap2.put("duration", String.valueOf(this._duration));
                    this._notifier.OnMetadata(hashMap2);
                    return (int)previousPosition2;
                }
                return (int)previousPosition2;
            }
        }
        catch (Exception ex) {
            return -1;
        }
        return (int)(-1L);
    }
    
    @Override
    public double GetRenderedFrameRate() {
        return 0.0;
    }
    
    @Override
    public String GetServerAddress() {
        return null;
    }
    
    @Override
    public int GetStartingBufferLengthMs() {
        return 0;
    }
    
    @Override
    public String GetStreamerType() {
        if (this.HLS_PLAYER) {
            return "SeamlessVideoView";
        }
        return "BrightcoveVideoView";
    }
    
    @Override
    public String GetStreamerVersion() {
        return this._mVersion;
    }
    
    @Override
    public void setMonitoringNotifier(final IMonitorNotifier notifier) {
        final boolean b = true;
        boolean isPaused = true;
        if (notifier != null) {
            this._previousState = 100;
        }
        else {
            if (this.HLS_PLAYER) {
                if (this._streamer_seamless != null) {
                    if (this._streamer_seamless.isPlaying()) {
                        isPaused = false;
                    }
                    this.isPaused = isPaused;
                }
            }
            else if (this._streamer_brightcove != null) {
                this.isPaused = (!this._streamer_brightcove.isPlaying() && b);
            }
            this._previousState = 98;
        }
        this._notifier = notifier;
    }
}
