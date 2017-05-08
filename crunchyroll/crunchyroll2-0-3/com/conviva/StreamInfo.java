// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva;

public class StreamInfo
{
    private int _bitrateKbps;
    private String _cdnName;
    private String _resource;
    
    public StreamInfo(final int bitrateKbps, final String cdnName, final String resource) {
        this._bitrateKbps = -1;
        this._resource = null;
        this._cdnName = null;
        this._bitrateKbps = bitrateKbps;
        this._cdnName = cdnName;
        if (this._cdnName == null) {
            this._cdnName = "OTHER";
        }
        this._resource = resource;
    }
    
    public boolean equals(final StreamInfo streamInfo) {
        return this._bitrateKbps == streamInfo.getBitrateKbps() && this._resource == streamInfo.getResource() && this._cdnName == streamInfo.getCdnName();
    }
    
    public int getBitrateKbps() {
        return this._bitrateKbps;
    }
    
    public String getCdnName() {
        return this._cdnName;
    }
    
    public String getResource() {
        return this._resource;
    }
}
