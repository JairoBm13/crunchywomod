// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import org.json.JSONException;
import org.json.JSONObject;

class DefaultSettingsJsonTransform implements SettingsJsonTransform
{
    private AnalyticsSettingsData buildAnalyticsSessionDataFrom(final JSONObject jsonObject) {
        return new AnalyticsSettingsData(jsonObject.optString("url", "https://e.crashlytics.com/spi/v2/events"), jsonObject.optInt("flush_interval_secs", 600), jsonObject.optInt("max_byte_size_per_file", 8000), jsonObject.optInt("max_file_count_per_send", 1), jsonObject.optInt("max_pending_send_file_count", 100), jsonObject.optBoolean("track_custom_events", true), jsonObject.optBoolean("track_predefined_events", true), jsonObject.optInt("sampling_rate", 1));
    }
    
    private AppSettingsData buildAppDataFrom(final JSONObject jsonObject) throws JSONException {
        final String string = jsonObject.getString("identifier");
        final String string2 = jsonObject.getString("status");
        final String string3 = jsonObject.getString("url");
        final String string4 = jsonObject.getString("reports_url");
        final boolean optBoolean = jsonObject.optBoolean("update_required", false);
        AppIconSettingsData buildIconData = null;
        if (jsonObject.has("icon")) {
            buildIconData = buildIconData;
            if (jsonObject.getJSONObject("icon").has("hash")) {
                buildIconData = this.buildIconDataFrom(jsonObject.getJSONObject("icon"));
            }
        }
        return new AppSettingsData(string, string2, string3, string4, optBoolean, buildIconData);
    }
    
    private BetaSettingsData buildBetaSettingsDataFrom(final JSONObject jsonObject) throws JSONException {
        return new BetaSettingsData(jsonObject.optString("update_endpoint", SettingsJsonConstants.BETA_UPDATE_ENDPOINT_DEFAULT), jsonObject.optInt("update_suspend_duration", 3600));
    }
    
    private FeaturesSettingsData buildFeaturesSessionDataFrom(final JSONObject jsonObject) {
        return new FeaturesSettingsData(jsonObject.optBoolean("prompt_enabled", false), jsonObject.optBoolean("collect_logged_exceptions", true), jsonObject.optBoolean("collect_reports", true), jsonObject.optBoolean("collect_analytics", false));
    }
    
    private AppIconSettingsData buildIconDataFrom(final JSONObject jsonObject) throws JSONException {
        return new AppIconSettingsData(jsonObject.getString("hash"), jsonObject.getInt("width"), jsonObject.getInt("height"));
    }
    
    private PromptSettingsData buildPromptDataFrom(final JSONObject jsonObject) throws JSONException {
        return new PromptSettingsData(jsonObject.optString("title", "Send Crash Report?"), jsonObject.optString("message", "Looks like we crashed! Please help us fix the problem by sending a crash report."), jsonObject.optString("send_button_title", "Send"), jsonObject.optBoolean("show_cancel_button", true), jsonObject.optString("cancel_button_title", "Don't Send"), jsonObject.optBoolean("show_always_send_button", true), jsonObject.optString("always_send_button_title", "Always Send"));
    }
    
    private SessionSettingsData buildSessionDataFrom(final JSONObject jsonObject) throws JSONException {
        return new SessionSettingsData(jsonObject.optInt("log_buffer_size", 64000), jsonObject.optInt("max_chained_exception_depth", 8), jsonObject.optInt("max_custom_exception_events", 64), jsonObject.optInt("max_custom_key_value_pairs", 64), jsonObject.optInt("identifier_mask", 255), jsonObject.optBoolean("send_session_without_crash", false));
    }
    
    private long getExpiresAtFrom(final CurrentTimeProvider currentTimeProvider, final long n, final JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("expires_at")) {
            return jsonObject.getLong("expires_at");
        }
        return currentTimeProvider.getCurrentTimeMillis() + 1000L * n;
    }
    
    @Override
    public SettingsData buildFromJson(final CurrentTimeProvider currentTimeProvider, final JSONObject jsonObject) throws JSONException {
        final int optInt = jsonObject.optInt("settings_version", 0);
        final int optInt2 = jsonObject.optInt("cache_duration", 3600);
        return new SettingsData(this.getExpiresAtFrom(currentTimeProvider, optInt2, jsonObject), this.buildAppDataFrom(jsonObject.getJSONObject("app")), this.buildSessionDataFrom(jsonObject.getJSONObject("session")), this.buildPromptDataFrom(jsonObject.getJSONObject("prompt")), this.buildFeaturesSessionDataFrom(jsonObject.getJSONObject("features")), this.buildAnalyticsSessionDataFrom(jsonObject.getJSONObject("analytics")), this.buildBetaSettingsDataFrom(jsonObject.getJSONObject("beta")), optInt, optInt2);
    }
}
