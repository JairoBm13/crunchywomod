// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import java.io.Serializable;

public class Version implements Serializable, Comparable<Version>
{
    private static final Version UNKNOWN_VERSION;
    protected final String _artifactId;
    protected final String _groupId;
    protected final int _majorVersion;
    protected final int _minorVersion;
    protected final int _patchLevel;
    protected final String _snapshotInfo;
    
    static {
        UNKNOWN_VERSION = new Version(0, 0, 0, null, null, null);
    }
    
    public Version(final int majorVersion, final int minorVersion, final int patchLevel, String artifactId, final String s, final String s2) {
        this._majorVersion = majorVersion;
        this._minorVersion = minorVersion;
        this._patchLevel = patchLevel;
        this._snapshotInfo = artifactId;
        artifactId = s;
        if (s == null) {
            artifactId = "";
        }
        this._groupId = artifactId;
        if ((artifactId = s2) == null) {
            artifactId = "";
        }
        this._artifactId = artifactId;
    }
    
    public static Version unknownVersion() {
        return Version.UNKNOWN_VERSION;
    }
    
    @Override
    public int compareTo(final Version version) {
        int n;
        if (version == this) {
            n = 0;
        }
        else if ((n = this._groupId.compareTo(version._groupId)) == 0 && (n = this._artifactId.compareTo(version._artifactId)) == 0 && (n = this._majorVersion - version._majorVersion) == 0 && (n = this._minorVersion - version._minorVersion) == 0) {
            return this._patchLevel - version._patchLevel;
        }
        return n;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            final Version version = (Version)o;
            if (version._majorVersion != this._majorVersion || version._minorVersion != this._minorVersion || version._patchLevel != this._patchLevel || !version._artifactId.equals(this._artifactId) || !version._groupId.equals(this._groupId)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this._artifactId.hashCode() ^ this._groupId.hashCode() + this._majorVersion - this._minorVersion + this._patchLevel;
    }
    
    public boolean isSnapshot() {
        return this._snapshotInfo != null && this._snapshotInfo.length() > 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._majorVersion).append('.');
        sb.append(this._minorVersion).append('.');
        sb.append(this._patchLevel);
        if (this.isSnapshot()) {
            sb.append('-').append(this._snapshotInfo);
        }
        return sb.toString();
    }
}
