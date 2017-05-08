// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import com.google.android.gms.cast.TextTrackStyle;
import org.json.JSONArray;
import com.google.android.gms.cast.MediaQueueItem;
import java.io.IOException;
import com.google.android.gms.cast.MediaInfo;
import org.json.JSONException;
import java.util.Iterator;
import android.os.SystemClock;
import org.json.JSONObject;
import java.util.ArrayList;
import com.google.android.gms.cast.MediaStatus;
import java.util.List;

public class zzm extends zzc
{
    private static final String NAMESPACE;
    private final List<zzp> zzTo;
    private long zzUY;
    private MediaStatus zzUZ;
    private final zzp zzVa;
    private final zzp zzVb;
    private final zzp zzVc;
    private final zzp zzVd;
    private final zzp zzVe;
    private final zzp zzVf;
    private final zzp zzVg;
    private final zzp zzVh;
    private final zzp zzVi;
    private final zzp zzVj;
    private final zzp zzVk;
    private final zzp zzVl;
    private final zzp zzVm;
    private final zzp zzVn;
    
    static {
        NAMESPACE = zzf.zzbE("com.google.cast.media");
    }
    
    public zzm(final String s) {
        super(zzm.NAMESPACE, "MediaControlChannel", s, 1000L);
        this.zzVa = new zzp(86400000L);
        this.zzVb = new zzp(86400000L);
        this.zzVc = new zzp(86400000L);
        this.zzVd = new zzp(86400000L);
        this.zzVe = new zzp(86400000L);
        this.zzVf = new zzp(86400000L);
        this.zzVg = new zzp(86400000L);
        this.zzVh = new zzp(86400000L);
        this.zzVi = new zzp(86400000L);
        this.zzVj = new zzp(86400000L);
        this.zzVk = new zzp(86400000L);
        this.zzVl = new zzp(86400000L);
        this.zzVm = new zzp(86400000L);
        this.zzVn = new zzp(86400000L);
        (this.zzTo = new ArrayList<zzp>()).add(this.zzVa);
        this.zzTo.add(this.zzVb);
        this.zzTo.add(this.zzVc);
        this.zzTo.add(this.zzVd);
        this.zzTo.add(this.zzVe);
        this.zzTo.add(this.zzVf);
        this.zzTo.add(this.zzVg);
        this.zzTo.add(this.zzVh);
        this.zzTo.add(this.zzVi);
        this.zzTo.add(this.zzVj);
        this.zzTo.add(this.zzVk);
        this.zzTo.add(this.zzVl);
        this.zzTo.add(this.zzVm);
        this.zzTo.add(this.zzVn);
        this.zzmc();
    }
    
    private void zza(final long n, final JSONObject jsonObject) throws JSONException {
        final boolean b = true;
        final boolean zzB = this.zzVa.zzB(n);
        int n2;
        if (this.zzVe.zzme() && !this.zzVe.zzB(n)) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        boolean b2 = false;
        Label_0087: {
            if (this.zzVf.zzme()) {
                b2 = b;
                if (!this.zzVf.zzB(n)) {
                    break Label_0087;
                }
            }
            b2 = (this.zzVg.zzme() && !this.zzVg.zzB(n) && b);
        }
        int n3;
        if (n2 != 0) {
            n3 = 2;
        }
        else {
            n3 = 0;
        }
        int n4 = n3;
        if (b2) {
            n4 = (n3 | 0x1);
        }
        int zza;
        if (zzB || this.zzUZ == null) {
            this.zzUZ = new MediaStatus(jsonObject);
            this.zzUY = SystemClock.elapsedRealtime();
            zza = 31;
        }
        else {
            zza = this.zzUZ.zza(jsonObject, n4);
        }
        if ((zza & 0x1) != 0x0) {
            this.zzUY = SystemClock.elapsedRealtime();
            this.onStatusUpdated();
        }
        if ((zza & 0x2) != 0x0) {
            this.zzUY = SystemClock.elapsedRealtime();
            this.onStatusUpdated();
        }
        if ((zza & 0x4) != 0x0) {
            this.onMetadataUpdated();
        }
        if ((zza & 0x8) != 0x0) {
            this.onQueueStatusUpdated();
        }
        if ((zza & 0x10) != 0x0) {
            this.onPreloadStatusUpdated();
        }
        final Iterator<zzp> iterator = this.zzTo.iterator();
        while (iterator.hasNext()) {
            iterator.next().zzc(n, 0);
        }
    }
    
    private void zzmc() {
        this.zzUY = 0L;
        this.zzUZ = null;
        final Iterator<zzp> iterator = this.zzTo.iterator();
        while (iterator.hasNext()) {
            iterator.next().clear();
        }
    }
    
    public long getApproximateStreamPosition() {
        final MediaInfo mediaInfo = this.getMediaInfo();
        if (mediaInfo == null || this.zzUY == 0L) {
            return 0L;
        }
        final double playbackRate = this.zzUZ.getPlaybackRate();
        final long streamPosition = this.zzUZ.getStreamPosition();
        final int playerState = this.zzUZ.getPlayerState();
        if (playbackRate == 0.0 || playerState != 2) {
            return streamPosition;
        }
        long n = SystemClock.elapsedRealtime() - this.zzUY;
        if (n < 0L) {
            n = 0L;
        }
        if (n == 0L) {
            return streamPosition;
        }
        final long streamDuration = mediaInfo.getStreamDuration();
        long n2 = streamPosition + (long)(n * playbackRate);
        if (streamDuration > 0L && n2 > streamDuration) {
            n2 = streamDuration;
        }
        else if (n2 < 0L) {
            n2 = 0L;
        }
        return n2;
    }
    
    public MediaInfo getMediaInfo() {
        if (this.zzUZ == null) {
            return null;
        }
        return this.zzUZ.getMediaInfo();
    }
    
    public MediaStatus getMediaStatus() {
        return this.zzUZ;
    }
    
    public long getStreamDuration() {
        final MediaInfo mediaInfo = this.getMediaInfo();
        if (mediaInfo != null) {
            return mediaInfo.getStreamDuration();
        }
        return 0L;
    }
    
    protected void onMetadataUpdated() {
    }
    
    protected void onPreloadStatusUpdated() {
    }
    
    protected void onQueueStatusUpdated() {
    }
    
    protected void onStatusUpdated() {
    }
    
    public long zza(final zzo zzo) throws IOException {
        final JSONObject jsonObject = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVh.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject.put("requestId", zzlK);
                jsonObject.put("type", (Object)"GET_STATUS");
                if (this.zzUZ != null) {
                    jsonObject.put("mediaSessionId", this.zzUZ.zzls());
                }
                this.zza(jsonObject.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final double n, final JSONObject jsonObject) throws IOException, IllegalStateException, IllegalArgumentException {
        if (Double.isInfinite(n) || Double.isNaN(n)) {
            throw new IllegalArgumentException("Volume cannot be " + n);
        }
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVf.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"SET_VOLUME");
                jsonObject2.put("mediaSessionId", this.zzls());
                final JSONObject jsonObject3 = new JSONObject();
                jsonObject3.put("level", n);
                jsonObject2.put("volume", (Object)jsonObject3);
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, int i, final MediaQueueItem[] array, final int n, final Integer n2, final JSONObject jsonObject) throws IOException, IllegalStateException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVl.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"QUEUE_UPDATE");
                jsonObject2.put("mediaSessionId", this.zzls());
                if (i != 0) {
                    jsonObject2.put("currentItemId", i);
                }
                if (n != 0) {
                    jsonObject2.put("jump", n);
                }
                if (array != null && array.length > 0) {
                    final JSONArray jsonArray = new JSONArray();
                    for (i = 0; i < array.length; ++i) {
                        jsonArray.put(i, (Object)array[i].toJson());
                    }
                    jsonObject2.put("items", (Object)jsonArray);
                }
                if (n2 != null) {
                    switch (n2) {
                        case 0: {
                            jsonObject2.put("repeatMode", (Object)"REPEAT_OFF");
                            break;
                        }
                        case 1: {
                            jsonObject2.put("repeatMode", (Object)"REPEAT_ALL");
                            break;
                        }
                        case 2: {
                            jsonObject2.put("repeatMode", (Object)"REPEAT_SINGLE");
                            break;
                        }
                        case 3: {
                            jsonObject2.put("repeatMode", (Object)"REPEAT_ALL_AND_SHUFFLE");
                            break;
                        }
                    }
                }
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final long n, final int n2, final JSONObject jsonObject) throws IOException, IllegalStateException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVe.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"SEEK");
                jsonObject2.put("mediaSessionId", this.zzls());
                jsonObject2.put("currentTime", zzf.zzA(n));
                if (n2 == 1) {
                    jsonObject2.put("resumeState", (Object)"PLAYBACK_START");
                }
                else if (n2 == 2) {
                    jsonObject2.put("resumeState", (Object)"PLAYBACK_PAUSE");
                }
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final MediaInfo mediaInfo, final boolean b, final long n, final long[] array, final JSONObject jsonObject) throws IOException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVa.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"LOAD");
                jsonObject2.put("media", (Object)mediaInfo.toJson());
                jsonObject2.put("autoplay", b);
                jsonObject2.put("currentTime", zzf.zzA(n));
                if (array != null) {
                    final JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < array.length; ++i) {
                        jsonArray.put(i, array[i]);
                    }
                    jsonObject2.put("activeTrackIds", (Object)jsonArray);
                }
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final TextTrackStyle textTrackStyle) throws IOException {
        final JSONObject jsonObject = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVj.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject.put("requestId", zzlK);
                jsonObject.put("type", (Object)"EDIT_TRACKS_INFO");
                if (textTrackStyle != null) {
                    jsonObject.put("textTrackStyle", (Object)textTrackStyle.toJson());
                }
                jsonObject.put("mediaSessionId", this.zzls());
                this.zza(jsonObject.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final JSONObject jsonObject) throws IOException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVb.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"PAUSE");
                jsonObject2.put("mediaSessionId", this.zzls());
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final boolean b, final JSONObject jsonObject) throws IOException, IllegalStateException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVg.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"SET_VOLUME");
                jsonObject2.put("mediaSessionId", this.zzls());
                final JSONObject jsonObject3 = new JSONObject();
                jsonObject3.put("muted", b);
                jsonObject2.put("volume", (Object)jsonObject3);
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final int[] array, final int n, final JSONObject jsonObject) throws IOException, IllegalStateException, IllegalArgumentException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("itemIdsToReorder must not be null or empty.");
        }
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVn.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"QUEUE_REORDER");
                jsonObject2.put("mediaSessionId", this.zzls());
                final JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < array.length; ++i) {
                    jsonArray.put(i, array[i]);
                }
                jsonObject2.put("itemIds", (Object)jsonArray);
                if (n != 0) {
                    jsonObject2.put("insertBefore", n);
                }
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final int[] array, final JSONObject jsonObject) throws IOException, IllegalStateException, IllegalArgumentException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("itemIdsToRemove must not be null or empty.");
        }
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVm.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"QUEUE_REMOVE");
                jsonObject2.put("mediaSessionId", this.zzls());
                final JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < array.length; ++i) {
                    jsonArray.put(i, array[i]);
                }
                jsonObject2.put("itemIds", (Object)jsonArray);
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final long[] array) throws IOException {
        final JSONObject jsonObject = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVi.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject.put("requestId", zzlK);
                jsonObject.put("type", (Object)"EDIT_TRACKS_INFO");
                jsonObject.put("mediaSessionId", this.zzls());
                final JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < array.length; ++i) {
                    jsonArray.put(i, array[i]);
                }
                jsonObject.put("activeTrackIds", (Object)jsonArray);
                this.zza(jsonObject.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zza(final zzo zzo, final MediaQueueItem[] array, final int n, final int n2, final JSONObject jsonObject) throws IOException, IllegalArgumentException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("items must not be null or empty.");
        }
        if (n < 0 || n >= array.length) {
            throw new IllegalArgumentException("Invalid startIndex: " + n);
        }
        while (true) {
            JSONObject jsonObject2 = null;
            Label_0308: {
                Label_0293: {
                    Label_0278: {
                        while (true) {
                            Label_0237: {
                                long zzlK;
                                while (true) {
                                    jsonObject2 = new JSONObject();
                                    zzlK = this.zzlK();
                                    this.zzVa.zza(zzlK, zzo);
                                    this.zzQ(true);
                                    while (true) {
                                        Label_0323: {
                                            try {
                                                jsonObject2.put("requestId", zzlK);
                                                jsonObject2.put("type", (Object)"QUEUE_LOAD");
                                                final JSONArray jsonArray = new JSONArray();
                                                for (int i = 0; i < array.length; ++i) {
                                                    jsonArray.put(i, (Object)array[i].toJson());
                                                }
                                                jsonObject2.put("items", (Object)jsonArray);
                                                switch (n2) {
                                                    case 0: {
                                                        break Label_0237;
                                                    }
                                                    case 1: {
                                                        break Label_0278;
                                                    }
                                                    case 2: {
                                                        break Label_0293;
                                                    }
                                                    case 3: {
                                                        break Label_0308;
                                                    }
                                                    default: {
                                                        break Label_0323;
                                                    }
                                                }
                                                throw new IllegalArgumentException("Invalid repeat mode: " + n2);
                                            }
                                            catch (JSONException ex) {}
                                            break;
                                        }
                                        continue;
                                    }
                                }
                                this.zza(jsonObject2.toString(), zzlK, null);
                                return zzlK;
                            }
                            jsonObject2.put("repeatMode", (Object)"REPEAT_OFF");
                            jsonObject2.put("startIndex", n);
                            if (jsonObject != null) {
                                jsonObject2.put("customData", (Object)jsonObject);
                            }
                            continue;
                        }
                    }
                    jsonObject2.put("repeatMode", (Object)"REPEAT_ALL");
                    continue;
                }
                jsonObject2.put("repeatMode", (Object)"REPEAT_SINGLE");
                continue;
            }
            jsonObject2.put("repeatMode", (Object)"REPEAT_ALL_AND_SHUFFLE");
            continue;
        }
    }
    
    public long zza(final zzo zzo, final MediaQueueItem[] array, final int n, final JSONObject jsonObject) throws IOException, IllegalStateException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("itemsToInsert must not be null or empty.");
        }
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVk.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"QUEUE_INSERT");
                jsonObject2.put("mediaSessionId", this.zzls());
                final JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < array.length; ++i) {
                    jsonArray.put(i, (Object)array[i].toJson());
                }
                jsonObject2.put("items", (Object)jsonArray);
                if (n != 0) {
                    jsonObject2.put("insertBefore", n);
                }
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    public long zzb(final zzo zzo, final JSONObject jsonObject) throws IOException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVd.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"STOP");
                jsonObject2.put("mediaSessionId", this.zzls());
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public void zzb(final long n, final int n2) {
        final Iterator<zzp> iterator = this.zzTo.iterator();
        while (iterator.hasNext()) {
            iterator.next().zzc(n, n2);
        }
    }
    
    @Override
    public final void zzbB(final String s) {
        this.zzUi.zzb("message received: %s", s);
        JSONObject jsonObject;
        String string;
        long optLong;
        try {
            jsonObject = new JSONObject(s);
            string = jsonObject.getString("type");
            optLong = jsonObject.optLong("requestId", -1L);
            if (string.equals("MEDIA_STATUS")) {
                final JSONArray jsonArray = jsonObject.getJSONArray("status");
                if (jsonArray.length() > 0) {
                    this.zza(optLong, jsonArray.getJSONObject(0));
                    return;
                }
                this.zzUZ = null;
                this.onStatusUpdated();
                this.onMetadataUpdated();
                this.onQueueStatusUpdated();
                this.onPreloadStatusUpdated();
                this.zzVh.zzc(optLong, 0);
                return;
            }
        }
        catch (JSONException ex) {
            this.zzUi.zzf("Message is malformed (%s); ignoring: %s", ex.getMessage(), s);
            return;
        }
        if (string.equals("INVALID_PLAYER_STATE")) {
            this.zzUi.zzf("received unexpected error: Invalid Player State.", new Object[0]);
            final JSONObject optJSONObject = jsonObject.optJSONObject("customData");
            final Iterator<zzp> iterator = this.zzTo.iterator();
            while (iterator.hasNext()) {
                iterator.next().zzc(optLong, 2100, optJSONObject);
            }
        }
        else {
            if (string.equals("LOAD_FAILED")) {
                this.zzVa.zzc(optLong, 2100, jsonObject.optJSONObject("customData"));
                return;
            }
            if (string.equals("LOAD_CANCELLED")) {
                this.zzVa.zzc(optLong, 2101, jsonObject.optJSONObject("customData"));
                return;
            }
            if (string.equals("INVALID_REQUEST")) {
                this.zzUi.zzf("received unexpected error: Invalid Request.", new Object[0]);
                final JSONObject optJSONObject2 = jsonObject.optJSONObject("customData");
                final Iterator<zzp> iterator2 = this.zzTo.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().zzc(optLong, 2100, optJSONObject2);
                }
            }
        }
    }
    
    public long zzc(final zzo zzo, final JSONObject jsonObject) throws IOException, IllegalStateException {
        final JSONObject jsonObject2 = new JSONObject();
        final long zzlK = this.zzlK();
        this.zzVc.zza(zzlK, zzo);
        this.zzQ(true);
        while (true) {
            try {
                jsonObject2.put("requestId", zzlK);
                jsonObject2.put("type", (Object)"PLAY");
                jsonObject2.put("mediaSessionId", this.zzls());
                if (jsonObject != null) {
                    jsonObject2.put("customData", (Object)jsonObject);
                }
                this.zza(jsonObject2.toString(), zzlK, null);
                return zzlK;
            }
            catch (JSONException ex) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public void zzlJ() {
        super.zzlJ();
        this.zzmc();
    }
    
    public long zzls() throws IllegalStateException {
        if (this.zzUZ == null) {
            throw new IllegalStateException("No current media session");
        }
        return this.zzUZ.zzls();
    }
    
    @Override
    protected boolean zzz(final long n) {
        final Iterator<zzp> iterator = this.zzTo.iterator();
        while (iterator.hasNext()) {
            iterator.next().zzd(n, 2102);
        }
        while (true) {
            synchronized (zzp.zzVr) {
                final Iterator<zzp> iterator2 = this.zzTo.iterator();
                while (iterator2.hasNext()) {
                    if (iterator2.next().zzme()) {
                        return true;
                    }
                }
                return false;
                return true;
            }
            return false;
        }
    }
}
