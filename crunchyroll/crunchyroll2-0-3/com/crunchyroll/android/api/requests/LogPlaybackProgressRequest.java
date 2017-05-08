// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class LogPlaybackProgressRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 6647962069489934393L;
    private final Integer elapsed;
    private final Integer elapsedDelta;
    private final String event;
    private final Long mediaId;
    private final Integer playhead;
    private final Optional<Long> videoEncodeId;
    private final Optional<Long> videoId;
    
    public LogPlaybackProgressRequest(final String event, final Long mediaId, final Long n, final Long n2, final Integer playhead, final Integer elapsed, final Integer elapsedDelta) {
        this.event = event;
        this.mediaId = mediaId;
        this.videoId = Optional.fromNullable(n);
        this.videoEncodeId = Optional.fromNullable(n2);
        this.playhead = playhead;
        this.elapsed = elapsed;
        this.elapsedDelta = elapsedDelta;
    }
    
    @Override
    public String getApiMethod() {
        return "log";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("event", this.event);
        builder.put("media_id", this.mediaId.toString());
        builder.put("playhead", this.playhead.toString());
        builder.put("elapsed", this.elapsed.toString());
        builder.put("elapsed_delta", this.elapsedDelta.toString());
        if (this.videoId.isPresent()) {
            builder.put("video_id", this.videoId.get().toString());
        }
        if (this.videoEncodeId.isPresent()) {
            builder.put("video_encode_id", this.videoEncodeId.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "LogPlaybackProgressRequest [getParams()=" + this.getParams() + "]";
    }
}
