// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.audio;

import java.util.Arrays;
import android.annotation.TargetApi;

@TargetApi(21)
public final class AudioCapabilities
{
    private static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES;
    private final int maxChannelCount;
    private final int[] supportedEncodings;
    
    static {
        DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(new int[] { 2 }, 2);
    }
    
    AudioCapabilities(final int[] array, final int maxChannelCount) {
        if (array != null) {
            Arrays.sort(this.supportedEncodings = Arrays.copyOf(array, array.length));
        }
        else {
            this.supportedEncodings = new int[0];
        }
        this.maxChannelCount = maxChannelCount;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof AudioCapabilities)) {
                return false;
            }
            final AudioCapabilities audioCapabilities = (AudioCapabilities)o;
            if (!Arrays.equals(this.supportedEncodings, audioCapabilities.supportedEncodings) || this.maxChannelCount != audioCapabilities.maxChannelCount) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.maxChannelCount + Arrays.hashCode(this.supportedEncodings) * 31;
    }
    
    public boolean supportsEncoding(final int n) {
        return Arrays.binarySearch(this.supportedEncodings, n) >= 0;
    }
    
    @Override
    public String toString() {
        return "AudioCapabilities[maxChannelCount=" + this.maxChannelCount + ", supportedEncodings=" + Arrays.toString(this.supportedEncodings) + "]";
    }
}
