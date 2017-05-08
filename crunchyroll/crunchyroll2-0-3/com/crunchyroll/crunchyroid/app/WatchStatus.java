// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.StreamData;
import com.crunchyroll.android.api.models.Media;
import android.util.SparseIntArray;

public class WatchStatus
{
    public static final int ANONYMOUS_USER = 1;
    public static final int CLEAR_TO_WATCH = 0;
    public static final int ENCODE_MISSING = 4;
    public static final int FREE_USER = 2;
    public static final int NEEDS_ALL_ACCESS = 6;
    public static final int NEEDS_PREMIUM = 5;
    public static final int NOT_AVAILABLE = 3;
    public static final int STREAM_NOT_PRESENT = 256;
    public static final int STREAM_PRESENT = 512;
    public static final int USER_ANONYMOUS = 16;
    public static final int USER_FREE = 32;
    public static final int USER_PREMIUM = 48;
    public static final int VIDEO_COMING_SOON = 3;
    public static final int VIDEO_FREE = 1;
    public static final int VIDEO_PREMIUM = 2;
    private static final SparseIntArray mWatchMap;
    
    static {
        (mWatchMap = new SparseIntArray(18)).put(273, 4);
        WatchStatus.mWatchMap.put(289, 4);
        WatchStatus.mWatchMap.put(305, 4);
        WatchStatus.mWatchMap.put(274, 5);
        WatchStatus.mWatchMap.put(290, 6);
        WatchStatus.mWatchMap.put(306, 4);
        WatchStatus.mWatchMap.put(275, 3);
        WatchStatus.mWatchMap.put(291, 3);
        WatchStatus.mWatchMap.put(307, 3);
        WatchStatus.mWatchMap.put(529, 1);
        WatchStatus.mWatchMap.put(545, 2);
        WatchStatus.mWatchMap.put(561, 0);
        WatchStatus.mWatchMap.put(530, 5);
        WatchStatus.mWatchMap.put(546, 6);
        WatchStatus.mWatchMap.put(562, 0);
        WatchStatus.mWatchMap.put(531, 3);
        WatchStatus.mWatchMap.put(547, 3);
        WatchStatus.mWatchMap.put(563, 3);
    }
    
    public static int get(final ApplicationState applicationState, final Media media) {
        final int n = 256;
        final Optional<StreamData> streamData = media.getStreamData();
        int n2 = n;
        if (streamData.isPresent()) {
            n2 = n;
            if (streamData.get().getStreams() != null) {
                n2 = n;
                if (streamData.get().getStreams().size() > 0) {
                    n2 = 512;
                }
            }
        }
        return WatchStatus.mWatchMap.get(n2 + getUserType(applicationState, media) + getVideoType(media), 3);
    }
    
    public static int getUserType(final ApplicationState applicationState, final Media media) {
        int n = 48;
        if (!applicationState.isUserPremiumForMediaType(media.getMediaType().orNull())) {
            if (!applicationState.getLoggedInUser().isPresent()) {
                return 16;
            }
            n = 32;
        }
        return n;
    }
    
    public static int getVideoType(final Media media) {
        int n = 1;
        if (!media.isFreeAvailable().or(Boolean.valueOf(true))) {
            if (!media.isPremiumAvailable().or(Boolean.valueOf(false))) {
                return 3;
            }
            n = 2;
        }
        return n;
    }
}
