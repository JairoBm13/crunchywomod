// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import android.text.TextUtils;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.internal.zzf;
import com.google.android.gms.internal.zzlh;
import org.json.JSONException;
import org.json.JSONObject;

public final class MediaTrack
{
    private String mName;
    private long zzOw;
    private String zzRA;
    private String zzRC;
    private String zzRE;
    private JSONObject zzRJ;
    private int zzSq;
    private int zzSr;
    
    MediaTrack(final JSONObject jsonObject) throws JSONException {
        this.zzf(jsonObject);
    }
    
    private void clear() {
        this.zzOw = 0L;
        this.zzSq = 0;
        this.zzRC = null;
        this.mName = null;
        this.zzRA = null;
        this.zzSr = -1;
        this.zzRJ = null;
    }
    
    private void zzf(final JSONObject jsonObject) throws JSONException {
        this.clear();
        this.zzOw = jsonObject.getLong("trackId");
        final String string = jsonObject.getString("type");
        if ("TEXT".equals(string)) {
            this.zzSq = 1;
        }
        else if ("AUDIO".equals(string)) {
            this.zzSq = 2;
        }
        else {
            if (!"VIDEO".equals(string)) {
                throw new JSONException("invalid type: " + string);
            }
            this.zzSq = 3;
        }
        this.zzRC = jsonObject.optString("trackContentId", (String)null);
        this.zzRE = jsonObject.optString("trackContentType", (String)null);
        this.mName = jsonObject.optString("name", (String)null);
        this.zzRA = jsonObject.optString("language", (String)null);
        if (jsonObject.has("subtype")) {
            final String string2 = jsonObject.getString("subtype");
            if ("SUBTITLES".equals(string2)) {
                this.zzSr = 1;
            }
            else if ("CAPTIONS".equals(string2)) {
                this.zzSr = 2;
            }
            else if ("DESCRIPTIONS".equals(string2)) {
                this.zzSr = 3;
            }
            else if ("CHAPTERS".equals(string2)) {
                this.zzSr = 4;
            }
            else {
                if (!"METADATA".equals(string2)) {
                    throw new JSONException("invalid subtype: " + string2);
                }
                this.zzSr = 5;
            }
        }
        else {
            this.zzSr = 0;
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
            if (o instanceof MediaTrack) {
                final MediaTrack mediaTrack = (MediaTrack)o;
                int n;
                if (this.zzRJ == null) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                int n2;
                if (mediaTrack.zzRJ == null) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                b3 = b2;
                if (n == n2) {
                    if (this.zzRJ != null && mediaTrack.zzRJ != null) {
                        b3 = b2;
                        if (!zzlh.zzd(this.zzRJ, mediaTrack.zzRJ)) {
                            return b3;
                        }
                    }
                    return this.zzOw == mediaTrack.zzOw && this.zzSq == mediaTrack.zzSq && zzf.zza(this.zzRC, mediaTrack.zzRC) && zzf.zza(this.zzRE, mediaTrack.zzRE) && zzf.zza(this.mName, mediaTrack.mName) && zzf.zza(this.zzRA, mediaTrack.zzRA) && this.zzSr == mediaTrack.zzSr && b;
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzOw, this.zzSq, this.zzRC, this.zzRE, this.mName, this.zzRA, this.zzSr, this.zzRJ);
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trackId", this.zzOw);
            switch (this.zzSq) {
                case 1: {
                    jsonObject.put("type", (Object)"TEXT");
                    break;
                }
                case 2: {
                    jsonObject.put("type", (Object)"AUDIO");
                    break;
                }
                case 3: {
                    jsonObject.put("type", (Object)"VIDEO");
                    break;
                }
            }
            if (this.zzRC != null) {
                jsonObject.put("trackContentId", (Object)this.zzRC);
            }
            if (this.zzRE != null) {
                jsonObject.put("trackContentType", (Object)this.zzRE);
            }
            if (this.mName != null) {
                jsonObject.put("name", (Object)this.mName);
            }
            if (!TextUtils.isEmpty((CharSequence)this.zzRA)) {
                jsonObject.put("language", (Object)this.zzRA);
            }
            switch (this.zzSr) {
                case 1: {
                    jsonObject.put("subtype", (Object)"SUBTITLES");
                    break;
                }
                case 2: {
                    jsonObject.put("subtype", (Object)"CAPTIONS");
                    break;
                }
                case 3: {
                    jsonObject.put("subtype", (Object)"DESCRIPTIONS");
                    break;
                }
                case 4: {
                    jsonObject.put("subtype", (Object)"CHAPTERS");
                    break;
                }
                case 5: {
                    jsonObject.put("subtype", (Object)"METADATA");
                    break;
                }
            }
            if (this.zzRJ != null) {
                jsonObject.put("customData", (Object)this.zzRJ);
                return jsonObject;
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
}
