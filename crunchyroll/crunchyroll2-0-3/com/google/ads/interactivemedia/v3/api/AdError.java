// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

public class AdError extends Exception
{
    private final AdErrorCode a;
    private final AdErrorType b;
    
    public AdError(final AdErrorType adErrorType, final int n, final String s) {
        this(adErrorType, AdErrorCode.a(n), s);
    }
    
    public AdError(final AdErrorType b, final AdErrorCode a, final String s) {
        super(s);
        this.b = b;
        this.a = a;
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    
    public enum AdErrorCode
    {
        ADS_REQUEST_NETWORK_ERROR(1012), 
        API_ERROR(1102), 
        COMPANION_AD_LOADING_FAILED(603), 
        FAILED_TO_REQUEST_ADS(1005), 
        INTERNAL_ERROR(-1), 
        INVALID_ARGUMENTS(1101), 
        OVERLAY_AD_LOADING_FAILED(502), 
        OVERLAY_AD_PLAYING_FAILED(500), 
        PLAYLIST_MALFORMED_RESPONSE(1004), 
        REQUIRED_LISTENERS_NOT_ADDED(1006), 
        UNKNOWN_AD_RESPONSE(200), 
        UNKNOWN_ERROR(900), 
        VAST_ASSET_NOT_FOUND(1007), 
        VAST_INVALID_URL(303), 
        VAST_LINEAR_ASSET_MISMATCH(403), 
        VAST_LOAD_TIMEOUT(301), 
        VAST_MALFORMED_RESPONSE(100), 
        VAST_MEDIA_LOAD_TIMEOUT(402), 
        VAST_NONLINEAR_ASSET_MISMATCH(503), 
        VAST_TOO_MANY_REDIRECTS(302), 
        VIDEO_PLAY_ERROR(400);
        
        private final int a;
        
        private AdErrorCode(final int a) {
            this.a = a;
        }
        
        static AdErrorCode a(final int n) {
            final AdErrorCode[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final AdErrorCode adErrorCode = values[i];
                if (adErrorCode.a() == n) {
                    return adErrorCode;
                }
            }
            if (1204 == n) {
                return AdErrorCode.INTERNAL_ERROR;
            }
            return AdErrorCode.UNKNOWN_ERROR;
        }
        
        int a() {
            return this.a;
        }
    }
    
    public enum AdErrorType
    {
        LOAD, 
        PLAY;
    }
}
