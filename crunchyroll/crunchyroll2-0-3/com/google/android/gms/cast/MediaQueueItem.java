// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import org.json.JSONArray;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.internal.zzf;
import com.google.android.gms.internal.zzlh;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaQueueItem
{
    private JSONObject zzRJ;
    private MediaInfo zzRS;
    private int zzRT;
    private boolean zzRU;
    private double zzRV;
    private double zzRW;
    private double zzRX;
    private long[] zzRY;
    
    private MediaQueueItem(final MediaInfo zzRS) throws IllegalArgumentException {
        this.zzRT = 0;
        this.zzRU = true;
        this.zzRW = Double.POSITIVE_INFINITY;
        if (zzRS == null) {
            throw new IllegalArgumentException("media cannot be null.");
        }
        this.zzRS = zzRS;
    }
    
    MediaQueueItem(final JSONObject jsonObject) throws JSONException {
        this.zzRT = 0;
        this.zzRU = true;
        this.zzRW = Double.POSITIVE_INFINITY;
        this.zzg(jsonObject);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = true;
        final boolean b2 = false;
        boolean b3;
        if (this == o) {
            b3 = true;
        }
        else {
            b3 = b2;
            if (o instanceof MediaQueueItem) {
                final MediaQueueItem mediaQueueItem = (MediaQueueItem)o;
                int n;
                if (this.zzRJ == null) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                int n2;
                if (mediaQueueItem.zzRJ == null) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                b3 = b2;
                if (n == n2) {
                    if (this.zzRJ != null && mediaQueueItem.zzRJ != null) {
                        b3 = b2;
                        if (!zzlh.zzd(this.zzRJ, mediaQueueItem.zzRJ)) {
                            return b3;
                        }
                    }
                    return zzf.zza(this.zzRS, mediaQueueItem.zzRS) && this.zzRT == mediaQueueItem.zzRT && this.zzRU == mediaQueueItem.zzRU && this.zzRV == mediaQueueItem.zzRV && this.zzRW == mediaQueueItem.zzRW && this.zzRX == mediaQueueItem.zzRX && zzf.zza(this.zzRY, mediaQueueItem.zzRY) && b;
                }
            }
        }
        return b3;
    }
    
    public int getItemId() {
        return this.zzRT;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzRS, this.zzRT, this.zzRU, this.zzRV, this.zzRW, this.zzRX, this.zzRY, String.valueOf(this.zzRJ));
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("media", (Object)this.zzRS.toJson());
            if (this.zzRT != 0) {
                jsonObject.put("itemId", this.zzRT);
            }
            jsonObject.put("autoplay", this.zzRU);
            jsonObject.put("startTime", this.zzRV);
            if (this.zzRW != Double.POSITIVE_INFINITY) {
                jsonObject.put("playbackDuration", this.zzRW);
            }
            jsonObject.put("preloadTime", this.zzRX);
            if (this.zzRY != null && this.zzRY.length > 0) {
                final JSONArray jsonArray = new JSONArray();
                final long[] zzRY = this.zzRY;
                for (int length = zzRY.length, i = 0; i < length; ++i) {
                    jsonArray.put(zzRY[i]);
                }
                jsonObject.put("activeTrackIds", (Object)jsonArray);
            }
            if (this.zzRJ != null) {
                jsonObject.put("customData", (Object)this.zzRJ);
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    public boolean zzg(final JSONObject jsonObject) throws JSONException {
        boolean b;
        if (jsonObject.has("media")) {
            this.zzRS = new MediaInfo(jsonObject.getJSONObject("media"));
            b = true;
        }
        else {
            b = false;
        }
        boolean b2 = b;
        if (jsonObject.has("itemId")) {
            final int int1 = jsonObject.getInt("itemId");
            b2 = b;
            if (this.zzRT != int1) {
                this.zzRT = int1;
                b2 = true;
            }
        }
        boolean b3 = b2;
        if (jsonObject.has("autoplay")) {
            final boolean boolean1 = jsonObject.getBoolean("autoplay");
            b3 = b2;
            if (this.zzRU != boolean1) {
                this.zzRU = boolean1;
                b3 = true;
            }
        }
        boolean b4 = b3;
        if (jsonObject.has("startTime")) {
            final double double1 = jsonObject.getDouble("startTime");
            b4 = b3;
            if (Math.abs(double1 - this.zzRV) > 1.0E-7) {
                this.zzRV = double1;
                b4 = true;
            }
        }
        boolean b5 = b4;
        if (jsonObject.has("playbackDuration")) {
            final double double2 = jsonObject.getDouble("playbackDuration");
            b5 = b4;
            if (Math.abs(double2 - this.zzRW) > 1.0E-7) {
                this.zzRW = double2;
                b5 = true;
            }
        }
        boolean b6 = b5;
        if (jsonObject.has("preloadTime")) {
            final double double3 = jsonObject.getDouble("preloadTime");
            b6 = b5;
            if (Math.abs(double3 - this.zzRX) > 1.0E-7) {
                this.zzRX = double3;
                b6 = true;
            }
        }
        long[] zzRY = null;
        int n = 0;
        Label_0330: {
            if (jsonObject.has("activeTrackIds")) {
                final JSONArray jsonArray = jsonObject.getJSONArray("activeTrackIds");
                final int length = jsonArray.length();
                zzRY = new long[length];
                for (int i = 0; i < length; ++i) {
                    zzRY[i] = jsonArray.getLong(i);
                }
                if (this.zzRY == null) {
                    n = 1;
                }
                else if (this.zzRY.length != length) {
                    n = 1;
                }
                else {
                    for (int j = 0; j < length; ++j) {
                        if (this.zzRY[j] != zzRY[j]) {
                            n = 1;
                            break Label_0330;
                        }
                    }
                    n = 0;
                }
            }
            else {
                n = 0;
                zzRY = null;
            }
        }
        if (n != 0) {
            this.zzRY = zzRY;
            b6 = true;
        }
        if (jsonObject.has("customData")) {
            this.zzRJ = jsonObject.getJSONObject("customData");
            return true;
        }
        return b6;
    }
    
    void zzlq() throws IllegalArgumentException {
        if (this.zzRS == null) {
            throw new IllegalArgumentException("media cannot be null.");
        }
        if (Double.isNaN(this.zzRV) || this.zzRV < 0.0) {
            throw new IllegalArgumentException("startTime cannot be negative or NaN.");
        }
        if (Double.isNaN(this.zzRW)) {
            throw new IllegalArgumentException("playbackDuration cannot be NaN.");
        }
        if (Double.isNaN(this.zzRX) || this.zzRX < 0.0) {
            throw new IllegalArgumentException("preloadTime cannot be negative or Nan.");
        }
    }
    
    public static class Builder
    {
        private final MediaQueueItem zzRZ;
        
        public Builder(final MediaInfo mediaInfo) throws IllegalArgumentException {
            this.zzRZ = new MediaQueueItem(mediaInfo, null);
        }
        
        public MediaQueueItem build() {
            this.zzRZ.zzlq();
            return this.zzRZ;
        }
    }
}
