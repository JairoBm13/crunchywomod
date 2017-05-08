// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.TreeNode;
import java.io.IOException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.models.Media;
import com.secondtv.android.ads.AdSlot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.crunchyroll.android.api.ApiRequest;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.requests.ListAdsRequest;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.crunchyroll.android.api.requests.InfoRequest;
import com.google.common.collect.Lists;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.crunchyroll.android.api.models.EpisodeInfo;

public class LoadEpisodeInfoTask extends BaseTask<EpisodeInfo>
{
    private static final Set<String> fields;
    protected final Long mMediaId;
    protected final Long mSeriesId;
    
    static {
        fields = ImmutableSet.of("media.media_id", "media.stream_data", "media.premium_available", "media.free_available", "media.series_id", "media.collection_id", "media.media_type, media.series_name", "media.name", "media.duration", "media.name", "media.description", "media.episode_number", "media.playhead", "media.fwide_url", "media.screenshot_image", "media.url", "media.bif_url", "media.collection_name", "series.genres");
    }
    
    public LoadEpisodeInfoTask(final Context context, final Long mMediaId, final Long mSeriesId) {
        super(context);
        if (mMediaId == 0L) {
            throw new IllegalArgumentException("mediaId cannot be 0");
        }
        this.mMediaId = mMediaId;
        this.mSeriesId = mSeriesId;
    }
    
    @Override
    public EpisodeInfo call() throws Exception {
        Object o = Lists.newArrayList();
        ((List<InfoRequest>)o).add(new InfoRequest(this.mMediaId, null, null));
        Object o2 = null;
        TreeNode path = null;
        while (true) {
            try {
                o2 = AdvertisingIdClient.getAdvertisingIdInfo((Context)this.getApplication());
                Object id = path;
                if (o2 != null) {
                    id = path;
                    if (!((AdvertisingIdClient.Info)o2).isLimitAdTrackingEnabled()) {
                        id = ((AdvertisingIdClient.Info)o2).getId();
                    }
                }
                ((List<InfoRequest>)o).add((InfoRequest)new ListAdsRequest(this.mMediaId, (String)id));
                if (this.mSeriesId != null) {
                    ((List<InfoRequest>)o).add(new InfoRequest(null, null, this.mSeriesId));
                }
                final ObjectMapper objectMapper = new ObjectMapper();
                final JsonNode path2 = this.getApplication().getApiService().postBatch((List<ApiRequest>)o, LoadEpisodeInfoTask.fields).body.asParser(objectMapper).readValueAsTree().path("data");
                path = path2.path(0).path("body").path("data");
                o = path2.path(1).path("body").path("data");
                final JsonNode path3 = ((JsonNode)o).path("ad_slots");
                final JsonNode path4 = path2.path(0).path("body");
                o2 = null;
                if (this.mSeriesId != null) {
                    o2 = path2.path(2).path("body").path("data");
                }
                if (path4.path("error").asBoolean()) {
                    o2 = path4.path("message").asText();
                    throw ApiErrorException.withErrorCode(ApiErrorException.ApiErrorCode.getErrorStatus(path4.path("code").asText())).message((String)o2).build();
                }
                final TypeReference<List<AdSlot>> typeReference = new TypeReference<List<AdSlot>>() {};
                final EpisodeInfo episodeInfo = new EpisodeInfo();
                episodeInfo.setMedia(objectMapper.readValue(path.traverse(), Media.class));
                episodeInfo.setAdSlots(objectMapper.readValue(path3.traverse(), typeReference));
                if (o2 != null) {
                    episodeInfo.setSeries(objectMapper.readValue(((TreeNode)o2).traverse(), Series.class));
                }
                if (((JsonNode)o).has("max_ad_start_seconds")) {
                    episodeInfo.setMaxAdStartSeconds(((JsonNode)o).path("max_ad_start_seconds").asInt());
                    return episodeInfo;
                }
                episodeInfo.setMaxAdStartSeconds(Integer.MAX_VALUE);
                return episodeInfo;
            }
            catch (GooglePlayServicesNotAvailableException ex) {
                continue;
            }
            catch (GooglePlayServicesRepairableException ex2) {
                continue;
            }
            catch (IOException ex3) {
                continue;
            }
            break;
        }
    }
}
