// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva;

public class StreamerError
{
    public static final int ERROR_SCOPE_RESOURCE = 3;
    public static final int ERROR_SCOPE_STREAM = 2;
    public static final int ERROR_SCOPE_STREAM_SEGMENT = 1;
    public static final int SEVERITY_FATAL = 1;
    public static final int SEVERITY_WARNING = 0;
    public static final int eErrorScope_Unknown = 0;
    private String _errorCode;
    private int _index;
    private int _scope;
    private int _severity;
    private StreamInfo _stream;
    
    public StreamerError(final String errorCode, final StreamInfo stream, final int index, final int severity, final int scope) {
        this._errorCode = errorCode;
        this._stream = stream;
        this._index = index;
        this._severity = severity;
        this._scope = scope;
    }
    
    public static StreamerError makeStreamError(final String s, final int n, final StreamInfo streamInfo) {
        return new StreamerError(s, streamInfo, -1, n, 2);
    }
    
    public static StreamerError makeStreamSegmentError(final String s, final int n, final StreamInfo streamInfo, final int n2) {
        return new StreamerError(s, streamInfo, n2, n, 1);
    }
    
    public static StreamerError makeUnscopedError(final String s, final int n) {
        return new StreamerError(s, null, -1, n, 0);
    }
    
    public String getErrorCode() {
        return this._errorCode;
    }
    
    public int getIndex() {
        return this._index;
    }
    
    public int getScope() {
        return this._scope;
    }
    
    public int getSeverity() {
        return this._severity;
    }
    
    public StreamInfo getStream() {
        return this._stream;
    }
}
