// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.util;

import com.crunchyroll.android.api.models.ImageSet;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.cast.model.CastInfo;
import com.crunchyroll.android.api.models.EpisodeInfo;

public class VideoUtil
{
    public static CastInfo toCastInfo(final Class clazz, final EpisodeInfo episodeInfo, final String s) {
        return toCastInfo(clazz, episodeInfo.getMedia(), s);
    }
    
    public static CastInfo toCastInfo(final Class clazz, final Media media, final String s) {
        return new CastInfo(clazz, media.getName(), media.getSeriesName().get(), media.getPlayhead().get(), media.getMediaId(), media.getScreenshotImage().get().getfWideUrl().get(), media.getScreenshotImage().get().getWideUrl().get(), media.getDuration().get(), s);
    }
}
