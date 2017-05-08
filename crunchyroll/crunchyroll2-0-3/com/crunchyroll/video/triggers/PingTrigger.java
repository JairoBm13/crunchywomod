// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.triggers;

import com.crunchyroll.android.api.models.Stream;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import com.crunchyroll.video.fragments.AbstractVideoPlayerFragment;
import com.crunchyroll.android.util.LoggerFactory;
import com.google.common.base.Optional;
import java.util.concurrent.atomic.AtomicLong;
import com.crunchyroll.android.util.Logger;

public final class PingTrigger extends AbstractVideoTrigger
{
    private static final Logger log;
    private final AtomicLong deltaElapsedMs;
    private final AtomicLong elapsedMs;
    private final Long mediaId;
    private AtomicLong numberPings;
    private Optional<Long> queueTriggerMark;
    
    static {
        log = LoggerFactory.getLogger(PingTrigger.class);
    }
    
    public PingTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final Bundle bundle, final Long mediaId, final Optional<Integer> optional) {
        super(abstractVideoPlayerFragment);
        this.mediaId = mediaId;
        this.numberPings = new AtomicLong(0L);
        this.elapsedMs = new AtomicLong(0L);
        this.deltaElapsedMs = new AtomicLong(0L);
        if (optional.isPresent()) {
            this.queueTriggerMark = Optional.of((long)(Object)Double.valueOf(Math.ceil(optional.get() * 0.81f)) * 1000L);
        }
        if (bundle != null) {
            final Bundle bundle2 = bundle.getBundle("PingTrigger");
            this.elapsedMs.set(Extras.getLong(bundle2, "elapsedMs").or(Long.valueOf(0L)));
            this.deltaElapsedMs.set(Extras.getLong(bundle2, "deltaElapsedMs").or(Long.valueOf(0L)));
            this.queueTriggerMark = Extras.getLong(bundle2, "queueTriggerMark");
        }
    }
    
    public long getNumberOfPings() {
        return this.numberPings.longValue();
    }
    
    @Override
    protected void runTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final int n) throws Exception {
        this.runTrigger(abstractVideoPlayerFragment, n, false);
    }
    
    public void runTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, int n, final boolean b) throws Exception {
        long n2;
        if (b) {
            n2 = this.elapsedMs.get();
        }
        else {
            n2 = this.elapsedMs.addAndGet(1000L);
        }
        long n3;
        if (b) {
            n3 = this.deltaElapsedMs.get();
        }
        else {
            n3 = this.deltaElapsedMs.addAndGet(1000L);
        }
        if (b) {
            PingTrigger.log.debug("PINGING: %s ms elapsed %s ms delta FORCED PING", n2, n3);
        }
        else if (this.queueTriggerMark.isPresent() && n > this.queueTriggerMark.get()) {
            PingTrigger.log.debug("PINGING: %s ms elapsed %s ms delta QUEUE PING", n2, n3);
            this.queueTriggerMark = Optional.absent();
            LocalBroadcastManager.getInstance((Context)abstractVideoPlayerFragment.getActivity()).sendBroadcast(new Intent("QUEUE_UPDATED"));
        }
        else if (n2 == 30000L) {
            PingTrigger.log.debug("PINGING: %s ms elapsed %s ms delta FIRST PING", n2, n3);
        }
        else {
            if (n2 % 120000L != 0L) {
                return;
            }
            PingTrigger.log.debug("PINGING: %s ms elapsed %s ms delta ", n2, n3);
        }
        final Stream activeStream = abstractVideoPlayerFragment.getActiveStream();
        final Long videoId = activeStream.getVideoId();
        final Long videoEncodeId = activeStream.getVideoEncodeId();
        final int intValue = (int)(Object)Long.valueOf(this.elapsedMs.get() / 1000L);
        final int intValue2 = (int)(Object)Long.valueOf(this.deltaElapsedMs.get() / 1000L);
        n /= 1000;
        Tracker.playbackProgress((Context)abstractVideoPlayerFragment.getActivity(), this.mediaId, videoId, videoEncodeId, n, intValue, intValue2);
        this.deltaElapsedMs.set(0L);
        this.numberPings.incrementAndGet();
    }
    
    @Override
    public void saveInstanceState(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        final Bundle bundle2 = new Bundle();
        Extras.putLong(bundle2, "elapsedMs", Long.valueOf(this.elapsedMs.get()));
        Extras.putLong(bundle2, "deltaElapsedMs", Long.valueOf(this.deltaElapsedMs.get()));
        if (this.queueTriggerMark.isPresent()) {
            Extras.putLong(bundle2, "queueTriggerMark", this.queueTriggerMark.get());
        }
        bundle.putBundle("PingTrigger", bundle2);
    }
}
