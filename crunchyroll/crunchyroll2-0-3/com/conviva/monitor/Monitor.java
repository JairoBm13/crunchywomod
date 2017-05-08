// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.monitor;

import com.conviva.StreamerError;
import java.util.Iterator;
import com.conviva.streamerProxies.Native.NativeStreamerProxy;
import java.util.HashMap;
import com.conviva.ConvivaStreamerProxy;
import java.util.TimerTask;
import com.conviva.utils.SlidingWindow;
import com.conviva.session.EventQueue;
import java.util.Map;
import com.conviva.ConvivaContentInfo;
import com.conviva.platforms.PlatformApi;
import com.conviva.utils.PlatformUtils;

public class Monitor implements IMonitorNotifier
{
    private static PlatformUtils _utils;
    private final int _NUM_PHT_OBSERVATIONS;
    private final int _PHT_MIN_SAMPLES;
    private final int _PHT_PAUSED_EXPECTED_SPEED;
    private final int _PHT_PLAY_EXPECTED_SPEED;
    private final float _PHT_TOLERANCE;
    private double _adStartTimeMs;
    private int _adTimeMs;
    private PlatformApi _api;
    private int _bitrateKbps;
    private int _bufferingEventCount;
    private String _cdn;
    private ConvivaContentInfo _contentInfo;
    private int _contentLenMs;
    private Map<Integer, Integer> _cumulativeTimePerState;
    private int _encodedFps;
    private EventQueue _eventQueue;
    private boolean _explicitPlayerPaused;
    private boolean _externalBitrateReporting;
    private boolean _isProxyCreatedOutside;
    private int _joinTimeMs;
    private SlidingWindow<Number> _lastPHTSpeeds;
    private int _lastPlayHeadTimeMs;
    private double _lastPollTimeMs;
    private double _lastStateUpdateTimeMs;
    private double _nominalPlayingBitsTotal;
    private int _playingFpsObservationCount;
    private double _playingFpsTotal;
    private int _playingState;
    private TimerTask _pollStreamerTask;
    private boolean _preGingerBread;
    private String _resource;
    private int _sessionFlags;
    private int _sessionId;
    private double _startTimeMs;
    private ConvivaStreamerProxy _streamer;
    private Object _streamerObject;
    
    static {
        Monitor._utils = null;
    }
    
    public Monitor(final Object streamerObject, final EventQueue eventQueue, final ConvivaContentInfo contentInfo, final Map<String, Object> map, final int sessionId, final PlatformApi api) throws Exception {
        final boolean b = true;
        this._sessionId = 0;
        this._streamer = null;
        this._streamerObject = null;
        this._eventQueue = null;
        this._contentInfo = null;
        this._api = null;
        this._startTimeMs = 0.0;
        this._adStartTimeMs = 0.0;
        this._adTimeMs = 0;
        this._lastStateUpdateTimeMs = 0.0;
        this._explicitPlayerPaused = false;
        this._externalBitrateReporting = false;
        this._preGingerBread = false;
        this._isProxyCreatedOutside = false;
        this._playingState = 100;
        this._bitrateKbps = -1;
        this._cdn = null;
        this._resource = null;
        this._sessionFlags = 1;
        this._cumulativeTimePerState = null;
        this._joinTimeMs = -1;
        this._bufferingEventCount = 0;
        this._nominalPlayingBitsTotal = 0.0;
        this._encodedFps = -1;
        this._contentLenMs = -1;
        this._playingFpsObservationCount = 0;
        this._playingFpsTotal = 0.0;
        this._NUM_PHT_OBSERVATIONS = 5;
        this._PHT_MIN_SAMPLES = 3;
        this._PHT_PLAY_EXPECTED_SPEED = 1;
        this._PHT_PAUSED_EXPECTED_SPEED = 0;
        this._PHT_TOLERANCE = 0.5f;
        this._lastPHTSpeeds = null;
        this._pollStreamerTask = new TimerTask() {
            @Override
            public void run() {
                Monitor.this.pollStreamer();
            }
        };
        this._lastPollTimeMs = 0.0;
        this._lastPlayHeadTimeMs = 0;
        this._eventQueue = eventQueue;
        Monitor._utils = PlatformUtils.getInstance();
        this._contentInfo = contentInfo;
        this._sessionId = sessionId;
        this._api = api;
        this._startTimeMs = 0.0;
        this._adStartTimeMs = 0.0;
        this._adTimeMs = 0;
        this._lastStateUpdateTimeMs = 0.0;
        this._cumulativeTimePerState = new HashMap<Integer, Integer>();
        final Iterator<Map.Entry<String, Integer>> iterator = PlayerStates.stateToInt.entrySet().iterator();
        while (iterator.hasNext()) {
            this._cumulativeTimePerState.put(iterator.next().getValue(), 0);
        }
        this._joinTimeMs = -1;
        this._bufferingEventCount = 0;
        this._nominalPlayingBitsTotal = 0.0;
        this._encodedFps = -1;
        this._contentLenMs = -1;
        this._playingFpsObservationCount = 0;
        this._playingFpsTotal = 0.0;
        this._streamerObject = streamerObject;
        final NativeStreamerProxy nativeStreamerProxy = (NativeStreamerProxy)this._streamer;
        if (nativeStreamerProxy != null && nativeStreamerProxy.getApiLevel() < 9) {
            this._preGingerBread = true;
        }
        if (map != null) {
            this._explicitPlayerPaused = (map.containsKey("explicitPlayerPaused") && map.get("explicitPlayerPaused"));
            this._externalBitrateReporting = (map.containsKey("externalBitrateReporting") && map.get("externalBitrateReporting") && b);
        }
    }
    
    private void InferPlayingState(final double n) {
        if (this._streamer != null) {
            if (this._joinTimeMs < 0 && this._streamer.GetRenderedFrameRate() > 0.0) {
                this._joinTimeMs = -3;
                this.Log("infer state to PLAYING, rendered frame rate is " + this._streamer.GetRenderedFrameRate());
                this.SetPlayingState(3);
                return;
            }
            if (NativeStreamerProxy.class.isInstance(this._streamer) && this._lastPHTSpeeds != null && this._lastPHTSpeeds.size() >= 3) {
                final Iterator<Number> iterator = this._lastPHTSpeeds.getSlots().iterator();
                float n2 = 0.0f;
                while (iterator.hasNext()) {
                    n2 += iterator.next().floatValue();
                }
                final float n3 = n2 / this._lastPHTSpeeds.size();
                if (this._playingState != 3 && Math.abs(n3 - 1.0f) < 0.5f) {
                    this.Log("infer state PLAYING, PHT is moving");
                    this.SetPlayingState(3);
                    return;
                }
                if (this._joinTimeMs >= 0 && n3 == 0.0f) {
                    if (this._playingState != 1 && (this._lastPlayHeadTimeMs == 0 || this._lastPlayHeadTimeMs == this._contentLenMs)) {
                        this.Log("infer STOPPED state, PHT is not moving and is 0");
                        this.SetPlayingState(1);
                        return;
                    }
                    final int getBufferLengthMs = this._streamer.GetBufferLengthMs();
                    if (this._playingState == 3 || this._playingState == 100) {
                        if (getBufferLengthMs >= 0) {
                            int getMinBufferLengthMs;
                            if ((getMinBufferLengthMs = this._streamer.GetMinBufferLengthMs()) > this._contentLenMs - this._lastPlayHeadTimeMs) {
                                getMinBufferLengthMs = this._contentLenMs - this._lastPlayHeadTimeMs;
                            }
                            if (getBufferLengthMs < getMinBufferLengthMs) {
                                this.Log("infer BUFFERING state, PHT is not moving and buffer length is less than minBufferlen");
                                this.SetPlayingState(6);
                            }
                            else {
                                this.Log("infer PAUSED state, PHT is not moving");
                                this.SetPlayingState(12);
                            }
                        }
                        else if (this._explicitPlayerPaused || this._preGingerBread) {
                            this.Log("infer BUFFERING state, PHT is not moving and buffer length is unavailable");
                            this.SetPlayingState(6);
                        }
                        else {
                            this.Log("infer PAUSED state, PHT is not moving and buffer events are available");
                            this.SetPlayingState(12);
                        }
                    }
                    if (this._playingState == 6 && this._streamer.GetStartingBufferLengthMs() > 0 && getBufferLengthMs > this._streamer.GetStartingBufferLengthMs()) {
                        this.Log("infer PAUSED state, buffer length grew longer than starting bufferLen.");
                        this.SetPlayingState(12);
                    }
                }
            }
        }
    }
    
    private void clearPHTSamples() {
        if (this._lastPHTSpeeds != null) {
            this._lastPHTSpeeds.clear();
        }
    }
    
    private void enqueueBitrateChangeEvent(final int bitrateKbps) {
        if (bitrateKbps <= 0 || this._bitrateKbps == bitrateKbps) {
            return;
        }
        this.Log("enqueueBitrateChangeEvent, new bitrate: " + bitrateKbps);
        Object value = null;
        if (this._bitrateKbps > 0) {
            value = this._bitrateKbps;
        }
        this._bitrateKbps = bitrateKbps;
        this.updateStateCumulativeTime();
        this.enqueueStateChange("br", value, bitrateKbps);
    }
    
    private void enqueueEvent(final String s, final Map<String, Object> map) {
        if (this._eventQueue != null) {
            this._eventQueue.enqueueEvent(s, map, (int)(Monitor._utils.epochTimeMs() - this._startTimeMs));
        }
    }
    
    private void enqueueStateChange(final String s, Object o, final Object o2) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (o != null) {
            final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
            hashMap2.put(s, o);
            hashMap.put("old", hashMap2);
        }
        o = new HashMap();
        ((HashMap<String, Object>)o).put(s, o2);
        hashMap.put("new", o);
        this.enqueueEvent("CwsStateChangeEvent", hashMap);
    }
    
    private void pollStreamer() {
        if (this._streamer == null) {
            return;
        }
        final int getPlayheadTimeMs = this._streamer.GetPlayheadTimeMs();
        final double epochTimeMs = Monitor._utils.epochTimeMs();
        if (getPlayheadTimeMs >= 0 && this._lastPollTimeMs > 0.0 && epochTimeMs > this._lastPollTimeMs) {
            this._lastPHTSpeeds.add((getPlayheadTimeMs - this._lastPlayHeadTimeMs) / (float)(epochTimeMs - this._lastPollTimeMs));
        }
        this._lastPollTimeMs = epochTimeMs;
        this._lastPlayHeadTimeMs = getPlayheadTimeMs;
        this.InferPlayingState(epochTimeMs);
    }
    
    private void updateMetrics() {
        if (this._streamer == null) {
            return;
        }
        if (this._playingState == 3) {
            final double getRenderedFrameRate = this._streamer.GetRenderedFrameRate();
            if (getRenderedFrameRate >= 0.0) {
                this._playingFpsTotal += getRenderedFrameRate;
                ++this._playingFpsObservationCount;
            }
        }
        this.updateStateCumulativeTime();
    }
    
    private void updateStateCumulativeTime() {
        final double epochTimeMs = Monitor._utils.epochTimeMs();
        if (this._playingState != 100) {
            final int n = (int)(epochTimeMs - this._lastStateUpdateTimeMs);
            this._cumulativeTimePerState.put(this._playingState, this._cumulativeTimePerState.get(this._playingState) + n);
            if (this._playingState == 3 && this._bitrateKbps != -1) {
                this._nominalPlayingBitsTotal += this._bitrateKbps * n;
            }
        }
        this._lastStateUpdateTimeMs = epochTimeMs;
    }
    
    private ConvivaStreamerProxy wrapInConvivaStreamerProxy(final Object o) throws Exception {
        if (ConvivaStreamerProxy.class.isInstance(o)) {
            this._isProxyCreatedOutside = true;
            this.Log("streamer is ConvivaStreamerProxy");
            return (ConvivaStreamerProxy)o;
        }
        this._isProxyCreatedOutside = false;
        Monitor._utils.err("The streamer is not recognizable, class: " + o.getClass().getName());
        throw new Exception("Monitoring unimplemented for streamer");
    }
    
    @Override
    public void Log(final String s) {
        Monitor._utils.logSession(s, this._sessionId);
    }
    
    @Override
    public void OnError(final StreamerError streamerError) {
        boolean b = true;
        this.Log("Enqueue CwsErrorEvent");
        if (streamerError.getSeverity() != 1) {
            b = false;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("ft", b);
        hashMap.put("err", streamerError.getErrorCode().toString());
        this.enqueueEvent("CwsErrorEvent", (Map<String, Object>)hashMap);
        if (b && this._joinTimeMs < 0) {
            this._joinTimeMs = -2;
        }
    }
    
    @Override
    public void OnMetadata(final Map<String, String> map) {
        try {
            if (map.containsKey("framerate")) {
                this._encodedFps = Monitor._utils.parseInt(map.get("framerate"), -1);
            }
            if (map.containsKey("duration")) {
                this._contentLenMs = Monitor._utils.parseInt(map.get("duration"), -1);
            }
        }
        catch (Exception ex) {
            this.Log("monitor.OnMetadata() error: " + ex.toString());
        }
    }
    
    @Override
    public void SetPlayingState(final int playingState) {
        final boolean b = false;
        if (this._playingState == playingState) {
            return;
        }
        this.Log("Change state to " + playingState);
        this.updateStateCumulativeTime();
        boolean b2;
        if (this._joinTimeMs >= 0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        boolean b3 = b;
        if (!b2) {
            b3 = b;
            if (playingState == 3) {
                this._cumulativeTimePerState.put(6, 0);
                if (this._adStartTimeMs > 0.0) {
                    this.adEnd();
                }
                this._joinTimeMs = (int)(Monitor._utils.epochTimeMs() - this._startTimeMs - this._adTimeMs);
                if (this._joinTimeMs < 0) {
                    this._joinTimeMs = 0;
                }
                b3 = true;
            }
        }
        boolean b4 = b3;
        if (b2) {
            b4 = b3;
            if (playingState == 6) {
                ++this._bufferingEventCount;
                b4 = true;
            }
        }
        boolean b5 = b4;
        if (this._playingState == 6) {
            b5 = b4;
            if (playingState == 3) {
                b5 = true;
            }
        }
        if (b5) {
            this.clearPHTSamples();
        }
        this.enqueueStateChange("ps", this._playingState, playingState);
        this._playingState = playingState;
    }
    
    @Override
    public void SetStream(final int n, final String cdn, final String resource) {
        if (!this._externalBitrateReporting) {
            this.enqueueBitrateChangeEvent(n);
        }
        if (cdn != null && !this._cdn.equals(cdn)) {
            this.Log("monitor.SetStream(): cdn changed to: " + cdn);
            this.enqueueStateChange("cdn", this._cdn, cdn);
            this._cdn = cdn;
        }
        if (resource != null && !this._resource.equals(resource)) {
            this.Log("monitor.SetStream(): resource changed to: " + resource);
            this.enqueueStateChange("rs", this._resource, resource);
            this._resource = resource;
        }
    }
    
    public void adEnd() {
        this.Log("monitor.adEnd()");
        final Boolean value = this._adStartTimeMs > 0.0;
        if (value) {
            this._adTimeMs += (int)(Monitor._utils.epochTimeMs() - this._adStartTimeMs);
        }
        this._adStartTimeMs = 0.0;
        this.enqueueStateChange("pj", value, false);
    }
    
    public void adStart() {
        this.Log("monitor.adStart()");
        final boolean b = this._adStartTimeMs > 0.0;
        this._adStartTimeMs = Monitor._utils.epochTimeMs();
        this.enqueueStateChange("pj", b, true);
    }
    
    public void attachStreamer(final Object o) throws Exception {
        if (o == null) {
            this.Log("monitor.attachStreamer() received a null streamer");
            this.SetPlayingState(98);
            return;
        }
        this.SetPlayingState(100);
        this.Log("monitor.attachStreamer()");
        this._streamer = this.wrapInConvivaStreamerProxy(o);
        this._sessionFlags = this._streamer.GetCapabilities();
        if (this._externalBitrateReporting) {
            this._sessionFlags |= 0x10;
        }
        this._streamer.setMonitoringNotifier(this);
        this._lastPollTimeMs = 0.0;
        this._api.createPollTask(this._pollStreamerTask, 200);
    }
    
    public void cleanup() {
        this.Log("monitor.cleanup()");
        this.pauseMonitor();
        this._api = null;
        this._eventQueue = null;
        this._contentInfo = null;
    }
    
    public ConvivaStreamerProxy getStreamer() {
        return this._streamer;
    }
    
    public void pauseMonitor() {
        this.Log("monitor.pauseMonitor()");
        this.updateMetrics();
        if (this._streamer != null) {
            this._streamer.setMonitoringNotifier(null);
            if (!this._isProxyCreatedOutside) {
                final ConvivaStreamerProxy streamer = this._streamer;
                this._streamer = null;
                streamer.Cleanup();
            }
            else {
                this.Log("Streamer Proxy is not created by Conviva and will not be cleaned up by Conviva Library");
                this._streamer = null;
            }
        }
        if (this._lastPHTSpeeds != null) {
            this._lastPHTSpeeds.clear();
        }
        this.SetPlayingState(98);
    }
    
    public void playerPaused(final boolean b) {
        if (b) {
            this.Log("Player state is paused via explicit call");
            this.SetPlayingState(12);
        }
        else {
            this.Log("Player state is un-paused via explicit call");
            this.SetPlayingState(100);
        }
        this.clearPHTSamples();
    }
    
    public void setBitrate(final int n) {
        if (this._externalBitrateReporting) {
            this.enqueueBitrateChangeEvent(n);
            return;
        }
        this.Log("setBitrate(): call ignored, enable external bitrate reporting first");
    }
    
    public void start(final double n) throws Exception {
        this.Log("monitor starts");
        this._startTimeMs = n;
        this._lastStateUpdateTimeMs = n;
        if (this._contentInfo != null) {
            this._cdn = this._contentInfo.defaultReportingCdnName;
            if (this._contentInfo.defaultReportingResource == null) {
                this._resource = this._cdn;
            }
            else {
                this._resource = this._contentInfo.defaultReportingResource;
            }
            this._bitrateKbps = this._contentInfo.defaultReportingBitrateKbps;
        }
        else {
            this._cdn = "OTHER";
            this._resource = "OTHER";
        }
        this.attachStreamer(this._streamerObject);
        this._streamerObject = null;
        this._lastPHTSpeeds = new SlidingWindow<Number>(5);
        this._lastPlayHeadTimeMs = 0;
    }
    
    public void updateHeartbeat(final Map<String, Object> map) {
        boolean b = false;
        this.updateMetrics();
        final int intValue = this._cumulativeTimePerState.get(3);
        boolean b2;
        if (this._joinTimeMs >= 0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        int intValue2;
        if (b2) {
            intValue2 = this._cumulativeTimePerState.get(6);
        }
        else {
            intValue2 = 0;
        }
        map.put("ps", this._playingState);
        if (this._adStartTimeMs > 0.0) {
            b = true;
        }
        map.put("pj", b);
        map.put("sf", this._sessionFlags);
        if (this._streamer != null) {
            map.put("fw", this._streamer.GetStreamerType());
            final String getStreamerVersion = this._streamer.GetStreamerVersion();
            if (getStreamerVersion != null) {
                map.put("fwv", getStreamerVersion);
            }
        }
        if (this._contentInfo != null && this._contentInfo.streamUrl != null) {
            map.put("url", this._contentInfo.streamUrl);
        }
        map.put("rs", this._resource);
        map.put("cdn", this._cdn);
        if (this._contentLenMs >= 0) {
            map.put("cl", (int)(this._contentLenMs / 1000.0));
        }
        if (this._encodedFps >= 0) {
            map.put("efps", this._encodedFps);
        }
        int n;
        if (this._playingFpsObservationCount > 0) {
            n = (int)((this._playingFpsTotal + 0.0) / this._playingFpsObservationCount);
        }
        else {
            n = -1;
        }
        if (n >= 0) {
            map.put("afps", n);
        }
        map.put("tpt", intValue);
        map.put("tbt", intValue2);
        map.put("tpst", this._cumulativeTimePerState.get(12));
        map.put("tst", this._cumulativeTimePerState.get(1));
        map.put("jt", this._joinTimeMs);
        map.put("tbe", this._bufferingEventCount);
        if (this._bitrateKbps > 0) {
            map.put("cbr", this._bitrateKbps);
            map.put("br", this._bitrateKbps);
        }
        int n2;
        if (b2 && this._nominalPlayingBitsTotal >= 0.0) {
            n2 = (int)((this._nominalPlayingBitsTotal + 0.0) / intValue);
        }
        else {
            n2 = -1;
        }
        if (n2 > 0) {
            map.put("abr", n2);
        }
    }
}
