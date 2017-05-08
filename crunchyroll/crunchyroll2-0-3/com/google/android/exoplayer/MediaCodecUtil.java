// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.media.MediaCodecList;
import java.io.IOException;
import android.text.TextUtils;
import android.media.MediaCodecInfo;
import android.util.Log;
import com.google.android.exoplayer.util.Util;
import android.media.MediaCodecInfo$CodecCapabilities;
import android.util.Pair;
import java.util.HashMap;
import android.annotation.TargetApi;

@TargetApi(16)
public final class MediaCodecUtil
{
    private static final HashMap<CodecKey, Pair<String, MediaCodecInfo$CodecCapabilities>> codecs;
    
    static {
        codecs = new HashMap<CodecKey, Pair<String, MediaCodecInfo$CodecCapabilities>>();
    }
    
    private static int avcLevelToMaxFrameSize(final int n) {
        int n2 = 25344;
        switch (n) {
            default: {
                n2 = -1;
                return n2;
            }
            case 1:
            case 2: {
                return n2;
            }
            case 8: {
                return 101376;
            }
            case 16: {
                return 101376;
            }
            case 32: {
                return 101376;
            }
            case 64: {
                return 202752;
            }
            case 128: {
                return 414720;
            }
            case 256: {
                return 414720;
            }
            case 512: {
                return 921600;
            }
            case 1024: {
                return 1310720;
            }
            case 2048: {
                return 2097152;
            }
            case 4096: {
                return 2097152;
            }
            case 8192: {
                return 2228224;
            }
            case 16384: {
                return 5652480;
            }
            case 32768: {
                return 9437184;
            }
        }
    }
    
    public static DecoderInfo getDecoderInfo(final String s, final boolean b) throws DecoderQueryException {
        final Pair<String, MediaCodecInfo$CodecCapabilities> mediaCodecInfo = getMediaCodecInfo(s, b);
        if (mediaCodecInfo == null) {
            return null;
        }
        return new DecoderInfo((String)mediaCodecInfo.first, isAdaptive((MediaCodecInfo$CodecCapabilities)mediaCodecInfo.second));
    }
    
    private static Pair<String, MediaCodecInfo$CodecCapabilities> getMediaCodecInfo(final CodecKey codecKey, final MediaCodecListCompat mediaCodecListCompat) throws DecoderQueryException {
        try {
            return getMediaCodecInfoInternal(codecKey, mediaCodecListCompat);
        }
        catch (Exception ex) {
            throw new DecoderQueryException((Throwable)ex);
        }
    }
    
    private static Pair<String, MediaCodecInfo$CodecCapabilities> getMediaCodecInfo(final String s, final boolean b) throws DecoderQueryException {
        while (true) {
            Object mediaCodecInfo;
            synchronized (MediaCodecUtil.class) {
                final CodecKey codecKey = new CodecKey(s, b);
                if (MediaCodecUtil.codecs.containsKey(codecKey)) {
                    return MediaCodecUtil.codecs.get(codecKey);
                }
                if (Util.SDK_INT >= 21) {
                    mediaCodecInfo = new MediaCodecListCompatV21(b);
                }
                else {
                    mediaCodecInfo = new MediaCodecListCompatV16();
                }
                final Pair<String, MediaCodecInfo$CodecCapabilities> pair2 = (Pair<String, MediaCodecInfo$CodecCapabilities>)(mediaCodecInfo = getMediaCodecInfo(codecKey, (MediaCodecListCompat)mediaCodecInfo));
                if (b && (mediaCodecInfo = pair2) == null) {
                    mediaCodecInfo = pair2;
                    if (Util.SDK_INT >= 21) {
                        final Pair<String, MediaCodecInfo$CodecCapabilities> mediaCodecInfo2 = getMediaCodecInfo(codecKey, (MediaCodecListCompat)new MediaCodecListCompatV16());
                        if ((mediaCodecInfo = mediaCodecInfo2) != null) {
                            Log.w("MediaCodecUtil", "MediaCodecList API didn't list secure decoder for: " + s + ". Assuming: " + (String)mediaCodecInfo2.first);
                            mediaCodecInfo = mediaCodecInfo2;
                        }
                    }
                }
            }
            return (Pair<String, MediaCodecInfo$CodecCapabilities>)mediaCodecInfo;
        }
    }
    
    private static Pair<String, MediaCodecInfo$CodecCapabilities> getMediaCodecInfoInternal(final CodecKey codecKey, final MediaCodecListCompat mediaCodecListCompat) {
        final String mimeType = codecKey.mimeType;
        final int codecCount = mediaCodecListCompat.getCodecCount();
        final boolean secureDecodersExplicit = mediaCodecListCompat.secureDecodersExplicit();
        for (int i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = mediaCodecListCompat.getCodecInfoAt(i);
            final String name = codecInfo.getName();
            if (isCodecUsableDecoder(codecInfo, name, secureDecodersExplicit)) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int j = 0; j < supportedTypes.length; ++j) {
                    final String s = supportedTypes[j];
                    if (s.equalsIgnoreCase(mimeType)) {
                        final MediaCodecInfo$CodecCapabilities capabilitiesForType = codecInfo.getCapabilitiesForType(s);
                        final boolean securePlaybackSupported = mediaCodecListCompat.isSecurePlaybackSupported(codecKey.mimeType, capabilitiesForType);
                        if (!secureDecodersExplicit) {
                            final HashMap<CodecKey, Pair<String, MediaCodecInfo$CodecCapabilities>> codecs = MediaCodecUtil.codecs;
                            CodecKey codecKey2;
                            if (codecKey.secure) {
                                codecKey2 = new CodecKey(mimeType, false);
                            }
                            else {
                                codecKey2 = codecKey;
                            }
                            codecs.put(codecKey2, (Pair<String, MediaCodecInfo$CodecCapabilities>)Pair.create((Object)name, (Object)capabilitiesForType));
                            if (securePlaybackSupported) {
                                final HashMap<CodecKey, Pair<String, MediaCodecInfo$CodecCapabilities>> codecs2 = MediaCodecUtil.codecs;
                                CodecKey codecKey3;
                                if (codecKey.secure) {
                                    codecKey3 = codecKey;
                                }
                                else {
                                    codecKey3 = new CodecKey(mimeType, true);
                                }
                                codecs2.put(codecKey3, (Pair<String, MediaCodecInfo$CodecCapabilities>)Pair.create((Object)(name + ".secure"), (Object)capabilitiesForType));
                            }
                        }
                        else {
                            final HashMap<CodecKey, Pair<String, MediaCodecInfo$CodecCapabilities>> codecs3 = MediaCodecUtil.codecs;
                            CodecKey codecKey4;
                            if (codecKey.secure == securePlaybackSupported) {
                                codecKey4 = codecKey;
                            }
                            else {
                                codecKey4 = new CodecKey(mimeType, securePlaybackSupported);
                            }
                            codecs3.put(codecKey4, (Pair<String, MediaCodecInfo$CodecCapabilities>)Pair.create((Object)name, (Object)capabilitiesForType));
                        }
                        if (MediaCodecUtil.codecs.containsKey(codecKey)) {
                            return MediaCodecUtil.codecs.get(codecKey);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean isAdaptive(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return Util.SDK_INT >= 19 && isAdaptiveV19(mediaCodecInfo$CodecCapabilities);
    }
    
    @TargetApi(19)
    private static boolean isAdaptiveV19(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return mediaCodecInfo$CodecCapabilities.isFeatureSupported("adaptive-playback");
    }
    
    private static boolean isCodecUsableDecoder(final MediaCodecInfo mediaCodecInfo, final String s, final boolean b) {
        return !mediaCodecInfo.isEncoder() && (b || !s.endsWith(".secure")) && (Util.SDK_INT != 16 || !"OMX.qcom.audio.decoder.mp3".equals(s) || (!"dlxu".equals(Util.DEVICE) && !"protou".equals(Util.DEVICE) && !"C6602".equals(Util.DEVICE) && !"C6603".equals(Util.DEVICE) && !"C6606".equals(Util.DEVICE) && !"C6616".equals(Util.DEVICE) && !"L36h".equals(Util.DEVICE) && !"SO-02E".equals(Util.DEVICE))) && (Util.SDK_INT != 16 || !"OMX.qcom.audio.decoder.aac".equals(s) || (!"C1504".equals(Util.DEVICE) && !"C1505".equals(Util.DEVICE) && !"C1604".equals(Util.DEVICE) && !"C1605".equals(Util.DEVICE))) && (Util.SDK_INT > 19 || Util.DEVICE == null || !Util.DEVICE.startsWith("serrano") || !"samsung".equals(Util.MANUFACTURER) || !s.equals("OMX.SEC.vp8.dec"));
    }
    
    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        int n = 0;
        final Pair<String, MediaCodecInfo$CodecCapabilities> mediaCodecInfo = getMediaCodecInfo("video/avc", false);
        if (mediaCodecInfo != null) {
            int max = 0;
            final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities = (MediaCodecInfo$CodecCapabilities)mediaCodecInfo.second;
            int n2 = 0;
            while (true) {
                n = max;
                if (n2 >= mediaCodecInfo$CodecCapabilities.profileLevels.length) {
                    break;
                }
                max = Math.max(avcLevelToMaxFrameSize(mediaCodecInfo$CodecCapabilities.profileLevels[n2].level), max);
                ++n2;
            }
        }
        return n;
    }
    
    private static final class CodecKey
    {
        public final String mimeType;
        public final boolean secure;
        
        public CodecKey(final String mimeType, final boolean secure) {
            this.mimeType = mimeType;
            this.secure = secure;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || o.getClass() != CodecKey.class) {
                    return false;
                }
                final CodecKey codecKey = (CodecKey)o;
                if (!TextUtils.equals((CharSequence)this.mimeType, (CharSequence)codecKey.mimeType) || this.secure != codecKey.secure) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.mimeType == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.mimeType.hashCode();
            }
            int n;
            if (this.secure) {
                n = 1231;
            }
            else {
                n = 1237;
            }
            return (hashCode + 31) * 31 + n;
        }
    }
    
    public static class DecoderQueryException extends IOException
    {
        private DecoderQueryException(final Throwable t) {
            super("Failed to query underlying media codecs", t);
        }
    }
    
    private interface MediaCodecListCompat
    {
        int getCodecCount();
        
        MediaCodecInfo getCodecInfoAt(final int p0);
        
        boolean isSecurePlaybackSupported(final String p0, final MediaCodecInfo$CodecCapabilities p1);
        
        boolean secureDecodersExplicit();
    }
    
    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat
    {
        @Override
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }
        
        @Override
        public MediaCodecInfo getCodecInfoAt(final int n) {
            return MediaCodecList.getCodecInfoAt(n);
        }
        
        @Override
        public boolean isSecurePlaybackSupported(final String s, final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
            return "video/avc".equals(s);
        }
        
        @Override
        public boolean secureDecodersExplicit() {
            return false;
        }
    }
    
    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat
    {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;
        
        public MediaCodecListCompatV21(final boolean b) {
            int codecKind;
            if (b) {
                codecKind = 1;
            }
            else {
                codecKind = 0;
            }
            this.codecKind = codecKind;
        }
        
        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
        
        @Override
        public int getCodecCount() {
            this.ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }
        
        @Override
        public MediaCodecInfo getCodecInfoAt(final int n) {
            this.ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[n];
        }
        
        @Override
        public boolean isSecurePlaybackSupported(final String s, final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
            return mediaCodecInfo$CodecCapabilities.isFeatureSupported("secure-playback");
        }
        
        @Override
        public boolean secureDecodersExplicit() {
            return true;
        }
    }
}
