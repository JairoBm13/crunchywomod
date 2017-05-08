// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import java.util.Iterator;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.internal.zzlh;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;
import com.google.android.gms.cast.internal.zzf;
import android.text.TextUtils;
import org.json.JSONObject;
import java.util.List;

public final class MediaInfo
{
    private final String zzRC;
    private int zzRD;
    private String zzRE;
    private MediaMetadata zzRF;
    private long zzRG;
    private List<MediaTrack> zzRH;
    private TextTrackStyle zzRI;
    private JSONObject zzRJ;
    
    MediaInfo(final String zzRC) throws IllegalArgumentException {
        if (TextUtils.isEmpty((CharSequence)zzRC)) {
            throw new IllegalArgumentException("content ID cannot be null or empty");
        }
        this.zzRC = zzRC;
        this.zzRD = -1;
        this.zzRG = -1L;
    }
    
    MediaInfo(final JSONObject jsonObject) throws JSONException {
        int i = 0;
        this.zzRC = jsonObject.getString("contentId");
        final String string = jsonObject.getString("streamType");
        if ("NONE".equals(string)) {
            this.zzRD = 0;
        }
        else if ("BUFFERED".equals(string)) {
            this.zzRD = 1;
        }
        else if ("LIVE".equals(string)) {
            this.zzRD = 2;
        }
        else {
            this.zzRD = -1;
        }
        this.zzRE = jsonObject.getString("contentType");
        if (jsonObject.has("metadata")) {
            final JSONObject jsonObject2 = jsonObject.getJSONObject("metadata");
            (this.zzRF = new MediaMetadata(jsonObject2.getInt("metadataType"))).zzf(jsonObject2);
        }
        this.zzRG = -1L;
        if (jsonObject.has("duration") && !jsonObject.isNull("duration")) {
            final double optDouble = jsonObject.optDouble("duration", 0.0);
            if (!Double.isNaN(optDouble) && !Double.isInfinite(optDouble)) {
                this.zzRG = zzf.zze(optDouble);
            }
        }
        if (jsonObject.has("tracks")) {
            this.zzRH = new ArrayList<MediaTrack>();
            for (JSONArray jsonArray = jsonObject.getJSONArray("tracks"); i < jsonArray.length(); ++i) {
                this.zzRH.add(new MediaTrack(jsonArray.getJSONObject(i)));
            }
        }
        else {
            this.zzRH = null;
        }
        if (jsonObject.has("textTrackStyle")) {
            final JSONObject jsonObject3 = jsonObject.getJSONObject("textTrackStyle");
            final TextTrackStyle zzRI = new TextTrackStyle();
            zzRI.zzf(jsonObject3);
            this.zzRI = zzRI;
        }
        else {
            this.zzRI = null;
        }
        this.zzRJ = jsonObject.optJSONObject("customData");
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
            if (o instanceof MediaInfo) {
                final MediaInfo mediaInfo = (MediaInfo)o;
                int n;
                if (this.zzRJ == null) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                int n2;
                if (mediaInfo.zzRJ == null) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                b3 = b2;
                if (n == n2) {
                    if (this.zzRJ != null && mediaInfo.zzRJ != null) {
                        b3 = b2;
                        if (!zzlh.zzd(this.zzRJ, mediaInfo.zzRJ)) {
                            return b3;
                        }
                    }
                    return zzf.zza(this.zzRC, mediaInfo.zzRC) && this.zzRD == mediaInfo.zzRD && zzf.zza(this.zzRE, mediaInfo.zzRE) && zzf.zza(this.zzRF, mediaInfo.zzRF) && this.zzRG == mediaInfo.zzRG && b;
                }
            }
        }
        return b3;
    }
    
    public long getStreamDuration() {
        return this.zzRG;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzRC, this.zzRD, this.zzRE, this.zzRF, this.zzRG, String.valueOf(this.zzRJ));
    }
    
    void setContentType(final String zzRE) throws IllegalArgumentException {
        if (TextUtils.isEmpty((CharSequence)zzRE)) {
            throw new IllegalArgumentException("content type cannot be null or empty");
        }
        this.zzRE = zzRE;
    }
    
    void setStreamType(final int zzRD) throws IllegalArgumentException {
        if (zzRD < -1 || zzRD > 2) {
            throw new IllegalArgumentException("invalid stream type");
        }
        this.zzRD = zzRD;
    }
    
    public JSONObject toJson() {
        JSONObject jsonObject;
        while (true) {
            jsonObject = new JSONObject();
            while (true) {
                Label_0247: {
                    Label_0241: {
                        try {
                            jsonObject.put("contentId", (Object)this.zzRC);
                            switch (this.zzRD) {
                                default: {
                                    final String s = "NONE";
                                    jsonObject.put("streamType", (Object)s);
                                    if (this.zzRE != null) {
                                        jsonObject.put("contentType", (Object)this.zzRE);
                                    }
                                    if (this.zzRF != null) {
                                        jsonObject.put("metadata", (Object)this.zzRF.toJson());
                                    }
                                    if (this.zzRG <= -1L) {
                                        jsonObject.put("duration", JSONObject.NULL);
                                    }
                                    else {
                                        jsonObject.put("duration", zzf.zzA(this.zzRG));
                                    }
                                    if (this.zzRH != null) {
                                        final JSONArray jsonArray = new JSONArray();
                                        final Iterator<MediaTrack> iterator = this.zzRH.iterator();
                                        while (iterator.hasNext()) {
                                            jsonArray.put((Object)iterator.next().toJson());
                                        }
                                        jsonObject.put("tracks", (Object)jsonArray);
                                    }
                                    if (this.zzRI != null) {
                                        jsonObject.put("textTrackStyle", (Object)this.zzRI.toJson());
                                    }
                                    if (this.zzRJ != null) {
                                        jsonObject.put("customData", (Object)this.zzRJ);
                                        return jsonObject;
                                    }
                                    break;
                                }
                                case 1: {
                                    break Label_0241;
                                }
                                case 2: {
                                    break Label_0247;
                                }
                            }
                        }
                        catch (JSONException ex) {}
                        break;
                    }
                    final String s = "BUFFERED";
                    continue;
                }
                final String s = "LIVE";
                continue;
            }
        }
        return jsonObject;
    }
    
    void zzlq() throws IllegalArgumentException {
        if (TextUtils.isEmpty((CharSequence)this.zzRC)) {
            throw new IllegalArgumentException("content ID cannot be null or empty");
        }
        if (TextUtils.isEmpty((CharSequence)this.zzRE)) {
            throw new IllegalArgumentException("content type cannot be null or empty");
        }
        if (this.zzRD == -1) {
            throw new IllegalArgumentException("a valid stream type must be specified");
        }
    }
    
    public static class Builder
    {
        private final MediaInfo zzRK;
        
        public Builder(final String s) throws IllegalArgumentException {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("Content ID cannot be empty");
            }
            this.zzRK = new MediaInfo(s);
        }
        
        public MediaInfo build() throws IllegalArgumentException {
            this.zzRK.zzlq();
            return this.zzRK;
        }
        
        public Builder setContentType(final String contentType) throws IllegalArgumentException {
            this.zzRK.setContentType(contentType);
            return this;
        }
        
        public Builder setStreamType(final int streamType) throws IllegalArgumentException {
            this.zzRK.setStreamType(streamType);
            return this;
        }
    }
}
