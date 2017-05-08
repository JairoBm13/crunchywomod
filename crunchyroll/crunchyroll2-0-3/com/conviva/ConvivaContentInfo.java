// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva;

import java.util.HashMap;
import java.util.Map;

public class ConvivaContentInfo
{
    public static final String CDN_NAME_AKAMAI = "AKAMAI";
    public static final String CDN_NAME_AMAZON = "AMAZON";
    public static final String CDN_NAME_ATT = "ATT";
    public static final String CDN_NAME_BITGRAVITY = "BITGRAVITY";
    public static final String CDN_NAME_BT = "BT";
    public static final String CDN_NAME_CDNETWORKS = "CDNETWORKS";
    public static final String CDN_NAME_CDNVIDEO = "CDNVIDEO";
    public static final String CDN_NAME_CHINACACHE = "CHINACACHE";
    public static final String CDN_NAME_CHINANETCENTER = "CHINANETCENTER";
    public static final String CDN_NAME_COMCAST = "COMCAST";
    public static final String CDN_NAME_EDGECAST = "EDGECAST";
    public static final String CDN_NAME_FASTLY = "FASTLY";
    public static final String CDN_NAME_HIGHWINDS = "HIGHWINDS";
    public static final String CDN_NAME_INHOUSE = "INHOUSE";
    public static final String CDN_NAME_INTERNAP = "INTERNAP";
    public static final String CDN_NAME_IPONLY = "IPONLY";
    public static final String CDN_NAME_LEVEL3 = "LEVEL3";
    public static final String CDN_NAME_LIMELIGHT = "LIMELIGHT";
    public static final String CDN_NAME_MICROSOFT = "MICROSOFT";
    public static final String CDN_NAME_MIRRORIMAGE = "MIRRORIMAGE";
    public static final String CDN_NAME_NGENIX = "NGENIX";
    public static final String CDN_NAME_NICE = "NICE";
    public static final String CDN_NAME_OCTOSHAPE = "OCTOSHAPE";
    public static final String CDN_NAME_OTHER = "OTHER";
    public static final String CDN_NAME_QBRICK = "QBRICK";
    public static final String CDN_NAME_SONIC = "SONIC";
    public static final String CDN_NAME_SWARMCAST = "SWARMCAST";
    public static final String CDN_NAME_TALKTALK = "TALKTALK";
    public static final String CDN_NAME_TELEFONICA = "TELEFONICA";
    public static final String CDN_NAME_TELENOR = "TELENOR";
    public static final String CDN_NAME_TELIA = "TELIA";
    public static final String CDN_NAME_VELOCIX = "VELOCIX";
    public static final String DEVICE_TYPE_CONSOLE = "Console";
    public static final String DEVICE_TYPE_MOBILE = "Mobile";
    public static final String DEVICE_TYPE_PC = "PC";
    public static final String DEVICE_TYPE_SET_TOP_BOX = "Settop";
    public static final String FRAMEWORK_NAME_BRIGHTCOVE = "Brightcove";
    public static final String FRAMEWORK_NAME_KALTURA = "Kaltura";
    public static final String FRAMEWORK_NAME_OOYALA = "Ooyala";
    public static final String FRAMEWORK_NAME_OSMF = "OSMF";
    public static final String FRAMEWORK_NAME_THE_PLATFORM = "thePlatform";
    public static final String PLUGIN_NAME_KALTURA = "ConvivaKalturaPlugin";
    public String assetName;
    public int defaultReportingBitrateKbps;
    public String defaultReportingCdnName;
    public String defaultReportingResource;
    public String deviceId;
    public String deviceType;
    public String frameworkName;
    public String frameworkVersion;
    public boolean isLive;
    public String playerName;
    public String pluginName;
    public String pluginVersion;
    public String streamUrl;
    public Map<String, String> tags;
    public String viewerId;
    
    public ConvivaContentInfo(final String assetName, final Map<String, String> tags) {
        this.assetName = null;
        this.defaultReportingBitrateKbps = -1;
        this.defaultReportingCdnName = "OTHER";
        this.defaultReportingResource = null;
        this.frameworkName = null;
        this.frameworkVersion = null;
        this.pluginName = null;
        this.pluginVersion = null;
        this.viewerId = null;
        this.deviceId = null;
        this.deviceType = null;
        this.playerName = null;
        this.streamUrl = null;
        this.isLive = false;
        this.assetName = assetName;
        this.tags = tags;
        if (tags == null) {
            new HashMap();
        }
    }
}
