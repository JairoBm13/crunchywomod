// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.internal.zzjz;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.google.android.gms.common.images.WebImage;
import java.util.List;
import android.os.Bundle;

public class MediaMetadata
{
    private static final String[] zzRL;
    private static final zza zzRM;
    private final Bundle zzRN;
    private int zzRO;
    private final List<WebImage> zzvi;
    
    static {
        zzRL = new String[] { null, "String", "int", "double", "ISO-8601 date String" };
        zzRM = new zza().zzb("com.google.android.gms.cast.metadata.CREATION_DATE", "creationDateTime", 4).zzb("com.google.android.gms.cast.metadata.RELEASE_DATE", "releaseDate", 4).zzb("com.google.android.gms.cast.metadata.BROADCAST_DATE", "originalAirdate", 4).zzb("com.google.android.gms.cast.metadata.TITLE", "title", 1).zzb("com.google.android.gms.cast.metadata.SUBTITLE", "subtitle", 1).zzb("com.google.android.gms.cast.metadata.ARTIST", "artist", 1).zzb("com.google.android.gms.cast.metadata.ALBUM_ARTIST", "albumArtist", 1).zzb("com.google.android.gms.cast.metadata.ALBUM_TITLE", "albumName", 1).zzb("com.google.android.gms.cast.metadata.COMPOSER", "composer", 1).zzb("com.google.android.gms.cast.metadata.DISC_NUMBER", "discNumber", 2).zzb("com.google.android.gms.cast.metadata.TRACK_NUMBER", "trackNumber", 2).zzb("com.google.android.gms.cast.metadata.SEASON_NUMBER", "season", 2).zzb("com.google.android.gms.cast.metadata.EPISODE_NUMBER", "episode", 2).zzb("com.google.android.gms.cast.metadata.SERIES_TITLE", "seriesTitle", 1).zzb("com.google.android.gms.cast.metadata.STUDIO", "studio", 1).zzb("com.google.android.gms.cast.metadata.WIDTH", "width", 2).zzb("com.google.android.gms.cast.metadata.HEIGHT", "height", 2).zzb("com.google.android.gms.cast.metadata.LOCATION_NAME", "location", 1).zzb("com.google.android.gms.cast.metadata.LOCATION_LATITUDE", "latitude", 3).zzb("com.google.android.gms.cast.metadata.LOCATION_LONGITUDE", "longitude", 3);
    }
    
    public MediaMetadata() {
        this(0);
    }
    
    public MediaMetadata(final int zzRO) {
        this.zzvi = new ArrayList<WebImage>();
        this.zzRN = new Bundle();
        this.zzRO = zzRO;
    }
    
    private void zza(final JSONObject jsonObject, final String... array) {
        try {
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                if (this.zzRN.containsKey(s)) {
                    switch (MediaMetadata.zzRM.zzby(s)) {
                        case 1:
                        case 4: {
                            jsonObject.put(MediaMetadata.zzRM.zzbw(s), (Object)this.zzRN.getString(s));
                            break;
                        }
                        case 2: {
                            jsonObject.put(MediaMetadata.zzRM.zzbw(s), this.zzRN.getInt(s));
                            break;
                        }
                        case 3: {
                            jsonObject.put(MediaMetadata.zzRM.zzbw(s), this.zzRN.getDouble(s));
                            break;
                        }
                    }
                }
            }
            for (final String s2 : this.zzRN.keySet()) {
                if (!s2.startsWith("com.google.")) {
                    final Object value = this.zzRN.get(s2);
                    if (value instanceof String) {
                        jsonObject.put(s2, value);
                    }
                    else if (value instanceof Integer) {
                        jsonObject.put(s2, value);
                    }
                    else {
                        if (!(value instanceof Double)) {
                            continue;
                        }
                        jsonObject.put(s2, value);
                    }
                }
            }
        }
        catch (JSONException ex) {}
    }
    
    private void zzb(final JSONObject jsonObject, String... array) {
        array = (String[])(Object)new HashSet((Collection<? extends E>)Arrays.asList(array));
        try {
            final Iterator keys = jsonObject.keys();
        Block_15_Outer:
            while (keys.hasNext()) {
                final String s = keys.next();
                if (!"metadataType".equals(s)) {
                    final String zzbx = MediaMetadata.zzRM.zzbx(s);
                    Label_0245: {
                        if (zzbx == null) {
                            break Label_0245;
                        }
                        if (!((Set)(Object)array).contains(zzbx)) {
                            continue;
                        }
                        try {
                            final Object value = jsonObject.get(s);
                            if (value == null) {
                                continue Block_15_Outer;
                            }
                            switch (MediaMetadata.zzRM.zzby(zzbx)) {
                                case 1: {
                                    if (value instanceof String) {
                                        this.zzRN.putString(zzbx, (String)value);
                                        continue Block_15_Outer;
                                    }
                                    continue Block_15_Outer;
                                }
                                case 4: {
                                    if (value instanceof String && zzjz.zzbK((String)value) != null) {
                                        this.zzRN.putString(zzbx, (String)value);
                                        continue Block_15_Outer;
                                    }
                                    continue Block_15_Outer;
                                }
                                case 2: {
                                    if (value instanceof Integer) {
                                        this.zzRN.putInt(zzbx, (int)value);
                                        continue Block_15_Outer;
                                    }
                                    continue Block_15_Outer;
                                }
                                case 3: {
                                    if (value instanceof Double) {
                                        this.zzRN.putDouble(zzbx, (double)value);
                                        continue Block_15_Outer;
                                    }
                                    continue Block_15_Outer;
                                }
                                default: {
                                    continue Block_15_Outer;
                                }
                            }
                            // iftrue(Label_0306:, !value2 instanceof Integer)
                            // iftrue(Label_0018:, !value2 instanceof Double)
                            // iftrue(Label_0278:, !value2 instanceof String)
                            Object value2 = null;
                            Block_16:Block_17_Outer:
                            while (true) {
                                this.zzRN.putString(s, (String)value2);
                                continue Block_15_Outer;
                                Label_0278: {
                                    break Block_16;
                                }
                                while (true) {
                                    this.zzRN.putDouble(s, (double)value2);
                                    continue Block_15_Outer;
                                    Label_0306:
                                    continue;
                                }
                                value2 = jsonObject.get(s);
                                continue Block_17_Outer;
                            }
                            this.zzRN.putInt(s, (int)value2);
                        }
                        catch (JSONException ex) {}
                    }
                }
            }
        }
        catch (JSONException ex2) {}
    }
    
    private boolean zzb(final Bundle bundle, final Bundle bundle2) {
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        for (final String s : bundle.keySet()) {
            final Object value = bundle.get(s);
            final Object value2 = bundle2.get(s);
            if (value instanceof Bundle && value2 instanceof Bundle && !this.zzb((Bundle)value, (Bundle)value2)) {
                return false;
            }
            if (value == null) {
                if (value2 != null || !bundle2.containsKey(s)) {
                    return false;
                }
                continue;
            }
            else {
                if (!value.equals(value2)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public void clear() {
        this.zzRN.clear();
        this.zzvi.clear();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof MediaMetadata)) {
                return false;
            }
            final MediaMetadata mediaMetadata = (MediaMetadata)o;
            if (!this.zzb(this.zzRN, mediaMetadata.zzRN) || !this.zzvi.equals(mediaMetadata.zzvi)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final Iterator<String> iterator = this.zzRN.keySet().iterator();
        int n = 17;
        while (iterator.hasNext()) {
            n = this.zzRN.get((String)iterator.next()).hashCode() + n * 31;
        }
        return n * 31 + this.zzvi.hashCode();
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        while (true) {
            try {
                jsonObject.put("metadataType", this.zzRO);
                zzjz.zza(jsonObject, this.zzvi);
                switch (this.zzRO) {
                    default: {
                        this.zza(jsonObject, new String[0]);
                        return jsonObject;
                    }
                    case 0: {
                        this.zza(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.SUBTITLE", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                        return jsonObject;
                    }
                    case 1: {
                        this.zza(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.STUDIO", "com.google.android.gms.cast.metadata.SUBTITLE", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                        return jsonObject;
                    }
                    case 2: {
                        this.zza(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.SERIES_TITLE", "com.google.android.gms.cast.metadata.SEASON_NUMBER", "com.google.android.gms.cast.metadata.EPISODE_NUMBER", "com.google.android.gms.cast.metadata.BROADCAST_DATE");
                        return jsonObject;
                    }
                    case 3: {
                        this.zza(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.ALBUM_TITLE", "com.google.android.gms.cast.metadata.ALBUM_ARTIST", "com.google.android.gms.cast.metadata.COMPOSER", "com.google.android.gms.cast.metadata.TRACK_NUMBER", "com.google.android.gms.cast.metadata.DISC_NUMBER", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                        return jsonObject;
                    }
                    case 4: {
                        this.zza(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.LOCATION_NAME", "com.google.android.gms.cast.metadata.LOCATION_LATITUDE", "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE", "com.google.android.gms.cast.metadata.WIDTH", "com.google.android.gms.cast.metadata.HEIGHT", "com.google.android.gms.cast.metadata.CREATION_DATE");
                        return jsonObject;
                    }
                }
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public void zzf(final JSONObject jsonObject) {
        this.clear();
        this.zzRO = 0;
        while (true) {
            try {
                this.zzRO = jsonObject.getInt("metadataType");
                zzjz.zza(this.zzvi, jsonObject);
                switch (this.zzRO) {
                    default: {
                        this.zzb(jsonObject, new String[0]);
                    }
                    case 0: {
                        this.zzb(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.SUBTITLE", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                    }
                    case 1: {
                        this.zzb(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.STUDIO", "com.google.android.gms.cast.metadata.SUBTITLE", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                    }
                    case 2: {
                        this.zzb(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.SERIES_TITLE", "com.google.android.gms.cast.metadata.SEASON_NUMBER", "com.google.android.gms.cast.metadata.EPISODE_NUMBER", "com.google.android.gms.cast.metadata.BROADCAST_DATE");
                    }
                    case 3: {
                        this.zzb(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ALBUM_TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.ALBUM_ARTIST", "com.google.android.gms.cast.metadata.COMPOSER", "com.google.android.gms.cast.metadata.TRACK_NUMBER", "com.google.android.gms.cast.metadata.DISC_NUMBER", "com.google.android.gms.cast.metadata.RELEASE_DATE");
                    }
                    case 4: {
                        this.zzb(jsonObject, "com.google.android.gms.cast.metadata.TITLE", "com.google.android.gms.cast.metadata.ARTIST", "com.google.android.gms.cast.metadata.LOCATION_NAME", "com.google.android.gms.cast.metadata.LOCATION_LATITUDE", "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE", "com.google.android.gms.cast.metadata.WIDTH", "com.google.android.gms.cast.metadata.HEIGHT", "com.google.android.gms.cast.metadata.CREATION_DATE");
                    }
                }
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    private static class zza
    {
        private final Map<String, String> zzRP;
        private final Map<String, String> zzRQ;
        private final Map<String, Integer> zzRR;
        
        public zza() {
            this.zzRP = new HashMap<String, String>();
            this.zzRQ = new HashMap<String, String>();
            this.zzRR = new HashMap<String, Integer>();
        }
        
        public zza zzb(final String s, final String s2, final int n) {
            this.zzRP.put(s, s2);
            this.zzRQ.put(s2, s);
            this.zzRR.put(s, n);
            return this;
        }
        
        public String zzbw(final String s) {
            return this.zzRP.get(s);
        }
        
        public String zzbx(final String s) {
            return this.zzRQ.get(s);
        }
        
        public int zzby(final String s) {
            final Integer n = this.zzRR.get(s);
            if (n != null) {
                return n;
            }
            return 0;
        }
    }
}
