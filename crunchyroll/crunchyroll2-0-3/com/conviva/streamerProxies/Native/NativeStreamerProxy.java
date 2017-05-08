// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.streamerProxies.Native;

import com.conviva.StreamerError;
import java.util.Map;
import java.util.HashMap;
import android.os.Build$VERSION;
import com.conviva.utils.PlatformUtils;
import com.conviva.monitor.IMonitorNotifier;
import android.media.MediaPlayer;
import com.conviva.ConvivaStreamerProxy;
import android.media.MediaPlayer$OnInfoListener;
import android.media.MediaPlayer$OnErrorListener;

public abstract class NativeStreamerProxy implements MediaPlayer$OnErrorListener, MediaPlayer$OnInfoListener, ConvivaStreamerProxy
{
    private static final String ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK";
    private static final String ERR_SERVERDIED = "MEDIA_ERROR_SERVER_DIED";
    private static final String ERR_UNKNOWN = "MEDIA_ERROR_UNKNOWN";
    private final int MEDIA_INFO_BUFFERING_END;
    private final int MEDIA_INFO_BUFFERING_START;
    private int _apiLevel;
    protected int _duration;
    protected boolean _inListener;
    protected MediaPlayer _mPlayer;
    protected IMonitorNotifier _notifier;
    protected MediaPlayer$OnErrorListener _onErrorListenerOrig;
    protected MediaPlayer$OnInfoListener _onInfoListenerOrig;
    private boolean _preparedState;
    protected PlatformUtils _utils;
    
    public NativeStreamerProxy() throws Exception {
        this._mPlayer = null;
        this._utils = null;
        this._notifier = null;
        this._duration = -1;
        this.MEDIA_INFO_BUFFERING_START = 701;
        this.MEDIA_INFO_BUFFERING_END = 702;
        this._onErrorListenerOrig = null;
        this._onInfoListenerOrig = null;
        this._apiLevel = 0;
        this._preparedState = false;
        this._inListener = false;
        this._utils = PlatformUtils.getInstance();
        this._apiLevel = Build$VERSION.SDK_INT;
    }
    
    public void Cleanup() {
        this.Log("NativeStreamerProxy: clean up callbacks");
        if (this._mPlayer != null) {
            this._mPlayer.setOnErrorListener(this._onErrorListenerOrig);
            this._mPlayer.setOnInfoListener(this._onInfoListenerOrig);
            this._mPlayer = null;
        }
        this._onErrorListenerOrig = null;
        this._onInfoListenerOrig = null;
    }
    
    public int GetCapabilities() {
        return 3;
    }
    
    public int GetDroppedFrames() {
        return -1;
    }
    
    public int GetIsLive() {
        return 0;
    }
    
    public int GetMinBufferLengthMs() {
        return 1500;
    }
    
    public int GetPlayheadTimeMs() {
        try {
            if (this._mPlayer != null) {
                if (!this._preparedState) {
                    this._preparedState = true;
                    return -1;
                }
                final int currentPosition = this._mPlayer.getCurrentPosition();
                if (currentPosition > 0 && this._duration == -1) {
                    this._duration = this._mPlayer.getDuration();
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("duration", String.valueOf(this._duration));
                    this._notifier.OnMetadata(hashMap);
                }
                return currentPosition;
            }
        }
        catch (IllegalStateException ex) {}
        return -1;
    }
    
    public double GetRenderedFrameRate() {
        return -1.0;
    }
    
    public String GetServerAddress() {
        return null;
    }
    
    public int GetStartingBufferLengthMs() {
        return 11000;
    }
    
    public String GetStreamerVersion() {
        return null;
    }
    
    public void Log(final String s) {
        if (this._notifier != null) {
            this._notifier.Log(s);
            return;
        }
        this._utils.log(s);
    }
    
    public int getApiLevel() {
        return this._apiLevel;
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this._inListener) {
            return true;
        }
        this.Log("Proxy: onError (" + n + ", " + n2 + ")");
        Label_0108: {
            if (n != 1) {
                break Label_0108;
            }
            Object unscopedError = "MEDIA_ERROR_UNKNOWN";
            Block_6_Outer:Block_7_Outer:
            while (true) {
                unscopedError = StreamerError.makeUnscopedError((String)unscopedError, 1);
                this._notifier.OnError((StreamerError)unscopedError);
                if (this._onErrorListenerOrig == null) {
                    return true;
                }
                this._inListener = true;
                try {
                    return this._onErrorListenerOrig.onError(mediaPlayer, n, n2);
                    // iftrue(Label_0121:, n != 100)
                    while (true) {
                        while (true) {
                            unscopedError = "MEDIA_ERROR_SERVER_DIED";
                            continue Block_6_Outer;
                            unscopedError = "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK";
                            continue Block_6_Outer;
                            continue Block_7_Outer;
                        }
                        Label_0135: {
                            unscopedError = "MEDIA_ERROR_UNKNOWN";
                        }
                        continue Block_6_Outer;
                        Label_0121:
                        continue;
                    }
                }
                // iftrue(Label_0135:, n != 200)
                finally {
                    this._inListener = false;
                }
                break;
            }
        }
        return true;
    }
    
    public boolean onInfo(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this._inListener) {
            return true;
        }
        this.Log("Proxy: onInfo");
        int n3 = 0;
    Block_8_Outer:
        while (true) {
            Label_0067: {
                if (this._apiLevel < 9) {
                    n3 = 100;
                    break Label_0067;
                }
                if (n != 701) {
                    break Block_8_Outer;
                }
                this.Log("Buffering start event");
                final int n4 = 6;
                n3 = n4;
                if (n4 != 100) {
                    this._notifier.SetPlayingState(n4);
                    n3 = n4;
                }
            }
            if (this._onInfoListenerOrig == null) {
                return n3 != 100;
            }
            try {
                this._inListener = true;
                return this._onInfoListenerOrig.onInfo(mediaPlayer, n, n2);
                // iftrue(Label_0120:, n != 702)
                while (true) {
                    this.Log("Buffering end event");
                    final int n4 = 3;
                    continue Block_8_Outer;
                    continue;
                }
                Label_0120: {
                    this.Log("other events: " + n + " : ignored");
                }
                final int n4 = 100;
                continue Block_8_Outer;
            }
            finally {
                this._inListener = false;
            }
            break;
        }
        return n3 != 100;
    }
    
    public void setMonitoringNotifier(final IMonitorNotifier notifier) {
        this._notifier = notifier;
    }
    
    public void start() {
    }
}
