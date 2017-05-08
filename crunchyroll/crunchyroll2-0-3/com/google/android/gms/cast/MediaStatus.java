// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import java.util.ArrayList;
import android.util.SparseArray;
import java.util.List;
import org.json.JSONArray;
import com.google.android.gms.cast.internal.zzf;
import org.json.JSONException;
import org.json.JSONObject;

public final class MediaStatus
{
    private JSONObject zzRJ;
    private MediaInfo zzRK;
    private long[] zzRY;
    private int zzSa;
    private long zzSb;
    private double zzSc;
    private int zzSd;
    private int zzSe;
    private long zzSf;
    private long zzSg;
    private double zzSh;
    private boolean zzSi;
    private int zzSj;
    private int zzSk;
    private final zza zzSl;
    
    public MediaStatus(final JSONObject jsonObject) throws JSONException {
        this.zzSa = 0;
        this.zzSj = 0;
        this.zzSk = 0;
        this.zzSl = new zza();
        this.zza(jsonObject, 0);
    }
    
    private boolean zzh(final int n, final int n2) {
        return n == 1 && n2 == 0;
    }
    
    public int getIdleReason() {
        return this.zzSe;
    }
    
    public MediaInfo getMediaInfo() {
        return this.zzRK;
    }
    
    public double getPlaybackRate() {
        return this.zzSc;
    }
    
    public int getPlayerState() {
        return this.zzSd;
    }
    
    public MediaQueueItem getQueueItem(final int n) {
        return this.zzSl.zzaE(n);
    }
    
    public int getQueueItemCount() {
        return this.zzSl.getItemCount();
    }
    
    public long getStreamPosition() {
        return this.zzSf;
    }
    
    public int zza(final JSONObject jsonObject, int i) throws JSONException {
        final int n = 2;
        final int n2 = 1;
        final long long1 = jsonObject.getLong("mediaSessionId");
        boolean b;
        if (long1 != this.zzSb) {
            this.zzSb = long1;
            b = true;
        }
        else {
            b = false;
        }
        int n3 = b ? 1 : 0;
        if (jsonObject.has("playerState")) {
            final String string = jsonObject.getString("playerState");
            int zzSd;
            if (string.equals("IDLE")) {
                zzSd = 1;
            }
            else if (string.equals("PLAYING")) {
                zzSd = 2;
            }
            else if (string.equals("PAUSED")) {
                zzSd = 3;
            }
            else if (string.equals("BUFFERING")) {
                zzSd = 4;
            }
            else {
                zzSd = 0;
            }
            int n4 = b ? 1 : 0;
            if (zzSd != this.zzSd) {
                this.zzSd = zzSd;
                n4 = ((b ? 1 : 0) | 0x2);
            }
            n3 = n4;
            if (zzSd == 1) {
                n3 = n4;
                if (jsonObject.has("idleReason")) {
                    final String string2 = jsonObject.getString("idleReason");
                    int zzSe;
                    if (string2.equals("CANCELLED")) {
                        zzSe = n;
                    }
                    else if (string2.equals("INTERRUPTED")) {
                        zzSe = 3;
                    }
                    else if (string2.equals("FINISHED")) {
                        zzSe = 1;
                    }
                    else if (string2.equals("ERROR")) {
                        zzSe = 4;
                    }
                    else {
                        zzSe = 0;
                    }
                    n3 = n4;
                    if (zzSe != this.zzSe) {
                        this.zzSe = zzSe;
                        n3 = (n4 | 0x2);
                    }
                }
            }
        }
        int n5 = n3;
        if (jsonObject.has("playbackRate")) {
            final double double1 = jsonObject.getDouble("playbackRate");
            n5 = n3;
            if (this.zzSc != double1) {
                this.zzSc = double1;
                n5 = (n3 | 0x2);
            }
        }
        int n6 = n5;
        if (jsonObject.has("currentTime")) {
            n6 = n5;
            if ((i & 0x2) == 0x0) {
                final long zze = zzf.zze(jsonObject.getDouble("currentTime"));
                n6 = n5;
                if (zze != this.zzSf) {
                    this.zzSf = zze;
                    n6 = (n5 | 0x2);
                }
            }
        }
        int n7 = n6;
        if (jsonObject.has("supportedMediaCommands")) {
            final long long2 = jsonObject.getLong("supportedMediaCommands");
            n7 = n6;
            if (long2 != this.zzSg) {
                this.zzSg = long2;
                n7 = (n6 | 0x2);
            }
        }
        int n8 = n7;
        if (jsonObject.has("volume")) {
            n8 = n7;
            if ((i & 0x1) == 0x0) {
                final JSONObject jsonObject2 = jsonObject.getJSONObject("volume");
                final double double2 = jsonObject2.getDouble("level");
                i = n7;
                if (double2 != this.zzSh) {
                    this.zzSh = double2;
                    i = (n7 | 0x2);
                }
                final boolean boolean1 = jsonObject2.getBoolean("muted");
                n8 = i;
                if (boolean1 != this.zzSi) {
                    this.zzSi = boolean1;
                    n8 = (i | 0x2);
                }
            }
        }
        long[] array;
        int n9;
        if (jsonObject.has("activeTrackIds")) {
            final JSONArray jsonArray = jsonObject.getJSONArray("activeTrackIds");
            final int length = jsonArray.length();
            array = new long[length];
            for (i = 0; i < length; ++i) {
                array[i] = jsonArray.getLong(i);
            }
            Label_0567: {
                if (this.zzRY == null) {
                    i = n2;
                }
                else {
                    i = n2;
                    if (this.zzRY.length == length) {
                        for (int j = 0; j < length; ++j) {
                            i = n2;
                            if (this.zzRY[j] != array[j]) {
                                break Label_0567;
                            }
                        }
                        i = 0;
                    }
                }
            }
            if (i != 0) {
                this.zzRY = array;
            }
            n9 = i;
        }
        else if (this.zzRY != null) {
            n9 = 1;
            array = null;
        }
        else {
            array = null;
            n9 = 0;
        }
        i = n8;
        if (n9 != 0) {
            this.zzRY = array;
            i = (n8 | 0x2);
        }
        int n10 = i;
        if (jsonObject.has("customData")) {
            this.zzRJ = jsonObject.getJSONObject("customData");
            n10 = (i | 0x2);
        }
        i = n10;
        if (jsonObject.has("media")) {
            final JSONObject jsonObject3 = jsonObject.getJSONObject("media");
            this.zzRK = new MediaInfo(jsonObject3);
            final int n11 = i = (n10 | 0x2);
            if (jsonObject3.has("metadata")) {
                i = (n11 | 0x4);
            }
        }
        int n12 = i;
        if (jsonObject.has("currentItemId")) {
            final int int1 = jsonObject.getInt("currentItemId");
            n12 = i;
            if (this.zzSa != int1) {
                this.zzSa = int1;
                n12 = (i | 0x2);
            }
        }
        final int optInt = jsonObject.optInt("preloadedItemId", 0);
        i = n12;
        if (this.zzSk != optInt) {
            this.zzSk = optInt;
            i = (n12 | 0x10);
        }
        final int optInt2 = jsonObject.optInt("loadingItemId", 0);
        int n13 = i;
        if (this.zzSj != optInt2) {
            this.zzSj = optInt2;
            n13 = (i | 0x2);
        }
        if (!this.zzh(this.zzSd, this.zzSj)) {
            i = n13;
            if (this.zzSl.zzg(jsonObject)) {
                i = (n13 | 0x8);
            }
        }
        else {
            this.zzSa = 0;
            this.zzSj = 0;
            this.zzSk = 0;
            i = n13;
            if (this.zzSl.getItemCount() > 0) {
                this.zzSl.clear();
                return n13 | 0x8;
            }
        }
        return i;
    }
    
    public long zzls() {
        return this.zzSb;
    }
    
    private class zza
    {
        private int zzSm;
        private List<MediaQueueItem> zzSn;
        private SparseArray<Integer> zzSo;
        
        zza() {
            this.zzSm = 0;
            this.zzSn = new ArrayList<MediaQueueItem>();
            this.zzSo = (SparseArray<Integer>)new SparseArray();
        }
        
        private void clear() {
            this.zzSm = 0;
            this.zzSn.clear();
            this.zzSo.clear();
        }
        
        private void zza(final MediaQueueItem[] array) {
            this.zzSn.clear();
            this.zzSo.clear();
            for (int i = 0; i < array.length; ++i) {
                final MediaQueueItem mediaQueueItem = array[i];
                this.zzSn.add(mediaQueueItem);
                this.zzSo.put(mediaQueueItem.getItemId(), (Object)i);
            }
        }
        
        private Integer zzaF(final int n) {
            return (Integer)this.zzSo.get(n);
        }
        
        private boolean zzg(final JSONObject jsonObject) throws JSONException {
            int zzSm = 2;
            final boolean b = true;
            while (true) {
                Label_0466: {
                    if (!jsonObject.has("repeatMode")) {
                        break Label_0466;
                    }
                    final int zzSm2 = this.zzSm;
                    final String string = jsonObject.getString("repeatMode");
                    int n = -1;
                    switch (string.hashCode()) {
                        case 1645952171: {
                            if (string.equals("REPEAT_OFF")) {
                                n = 0;
                                break;
                            }
                            break;
                        }
                        case 1645938909: {
                            if (string.equals("REPEAT_ALL")) {
                                n = 1;
                                break;
                            }
                            break;
                        }
                        case -962896020: {
                            if (string.equals("REPEAT_SINGLE")) {
                                n = 2;
                                break;
                            }
                            break;
                        }
                        case -1118317585: {
                            if (string.equals("REPEAT_ALL_AND_SHUFFLE")) {
                                n = 3;
                                break;
                            }
                            break;
                        }
                    }
                    boolean b2 = false;
                    Label_0111: {
                        switch (n) {
                            default: {
                                zzSm = zzSm2;
                                break Label_0111;
                            }
                            case 3: {
                                zzSm = 3;
                                break Label_0111;
                            }
                            case 1: {
                                zzSm = 1;
                                break Label_0111;
                            }
                            case 0: {
                                zzSm = 0;
                            }
                            case 2: {
                                if (this.zzSm != zzSm) {
                                    this.zzSm = zzSm;
                                    b2 = true;
                                    break;
                                }
                                break Label_0466;
                            }
                        }
                    }
                    if (jsonObject.has("items")) {
                        final JSONArray jsonArray = jsonObject.getJSONArray("items");
                        final int length = jsonArray.length();
                        final SparseArray sparseArray = new SparseArray();
                        for (int i = 0; i < length; ++i) {
                            sparseArray.put(i, (Object)jsonArray.getJSONObject(i).getInt("itemId"));
                        }
                        final MediaQueueItem[] array = new MediaQueueItem[length];
                        for (int j = 0; j < length; ++j) {
                            final Integer n2 = (Integer)sparseArray.get(j);
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                            final MediaQueueItem zzaD = this.zzaD(n2);
                            if (zzaD != null) {
                                final boolean zzg = zzaD.zzg(jsonObject2);
                                array[j] = zzaD;
                                b2 = (j != this.zzaF(n2) || (b2 | zzg));
                            }
                            else if (n2 == MediaStatus.this.zzSa) {
                                (array[j] = new MediaQueueItem.Builder(MediaStatus.this.zzRK).build()).zzg(jsonObject2);
                                b2 = true;
                            }
                            else {
                                array[j] = new MediaQueueItem(jsonObject2);
                                b2 = true;
                            }
                        }
                        if (this.zzSn.size() != length) {
                            b2 = b;
                        }
                        this.zza(array);
                        return b2;
                    }
                    return b2;
                }
                boolean b2 = false;
                continue;
            }
        }
        
        public int getItemCount() {
            return this.zzSn.size();
        }
        
        public MediaQueueItem zzaD(final int n) {
            final Integer n2 = (Integer)this.zzSo.get(n);
            if (n2 == null) {
                return null;
            }
            return this.zzSn.get(n2);
        }
        
        public MediaQueueItem zzaE(final int n) {
            if (n < 0 || n >= this.zzSn.size()) {
                return null;
            }
            return this.zzSn.get(n);
        }
    }
}
