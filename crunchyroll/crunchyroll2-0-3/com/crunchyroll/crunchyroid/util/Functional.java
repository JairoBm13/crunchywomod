// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.text.TextUtils;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.ImageSet;
import com.crunchyroll.android.api.models.Media;
import com.google.common.base.Function;

public final class Functional
{
    private static final Function<com.crunchyroll.android.api.models.Media, com.crunchyroll.android.api.models.ImageSet> screenshotImageFunction;
    
    static {
        screenshotImageFunction = new Function<com.crunchyroll.android.api.models.Media, com.crunchyroll.android.api.models.ImageSet>() {
            @Override
            public com.crunchyroll.android.api.models.ImageSet apply(final com.crunchyroll.android.api.models.Media media) {
                return media.getScreenshotImage().or(com.crunchyroll.android.api.models.ImageSet.DEFAULT);
            }
        };
    }
    
    public static final class ImageSet
    {
        public static final Optional<String> getFWideStarUrl(final Optional<com.crunchyroll.android.api.models.ImageSet> optional) {
            if (optional.isPresent()) {
                return optional.get().getfWideStarUrl();
            }
            return Optional.absent();
        }
        
        public static final Optional<String> getFWideUrl(final Optional<com.crunchyroll.android.api.models.ImageSet> optional) {
            if (optional.isPresent()) {
                return optional.get().getfWideUrl();
            }
            return Optional.absent();
        }
        
        public static final Optional<String> getWideStarUrl(final Optional<com.crunchyroll.android.api.models.ImageSet> optional) {
            if (optional.isPresent()) {
                return optional.get().getWideStarUrl();
            }
            return Optional.absent();
        }
        
        public static final Optional<String> getWideUrl(final Optional<com.crunchyroll.android.api.models.ImageSet> optional) {
            if (optional.isPresent()) {
                return optional.get().getWideUrl();
            }
            return Optional.absent();
        }
    }
    
    public static final class Media
    {
        public static final String getEpisodeNumber(final com.crunchyroll.android.api.models.Media media) {
            final String episodeNumber = media.getEpisodeNumber();
            final StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty((CharSequence)episodeNumber)) {
                sb.append(LocalizedStrings.EPISODE.get(episodeNumber));
            }
            return sb.toString();
        }
        
        public static final String getEpisodeSubtitle(final com.crunchyroll.android.api.models.Media media) {
            final String name = media.getName();
            final StringBuilder sb = new StringBuilder();
            sb.append(getEpisodeNumber(media));
            if (!TextUtils.isEmpty((CharSequence)name)) {
                if (sb.length() > 0) {
                    sb.append(" - ");
                }
                sb.append(name);
            }
            return sb.toString();
        }
        
        public static final String getEpisodeSubtitle(final Optional<com.crunchyroll.android.api.models.Media> optional) {
            if (optional.isPresent()) {
                return getEpisodeSubtitle(optional.get());
            }
            return "";
        }
        
        public static final Optional<com.crunchyroll.android.api.models.ImageSet> getScreenshot(final Optional<com.crunchyroll.android.api.models.Media> optional) {
            return optional.transform(Functional.screenshotImageFunction);
        }
        
        public static final String getShortEpisodeSubtitle(final com.crunchyroll.android.api.models.Media media) {
            final String episodeNumber = media.getEpisodeNumber();
            final String name = media.getName();
            final StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty((CharSequence)episodeNumber)) {
                if (TextUtils.isEmpty((CharSequence)name)) {
                    sb.append(LocalizedStrings.EPISODE.get(episodeNumber));
                }
                else {
                    sb.append(LocalizedStrings.EP.get(episodeNumber));
                }
            }
            if (!TextUtils.isEmpty((CharSequence)name)) {
                if (sb.length() > 0) {
                    sb.append(" - ");
                }
                sb.append(name);
            }
            return sb.toString();
        }
    }
}
