// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

public final class Subtitle
{
    public final boolean autoSelect;
    public final boolean isDefault;
    public final String language;
    public final String name;
    public final String uri;
    
    public Subtitle(final String name, final String uri, final String language, final boolean isDefault, final boolean autoSelect) {
        this.name = name;
        this.uri = uri;
        this.language = language;
        this.autoSelect = autoSelect;
        this.isDefault = isDefault;
    }
}
