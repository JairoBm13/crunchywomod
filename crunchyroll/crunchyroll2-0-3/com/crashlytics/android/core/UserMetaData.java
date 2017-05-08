// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

public class UserMetaData
{
    public static final UserMetaData EMPTY;
    public final String email;
    public final String id;
    public final String name;
    
    static {
        EMPTY = new UserMetaData();
    }
    
    public UserMetaData() {
        this(null, null, null);
    }
    
    public UserMetaData(final String id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public boolean isEmpty() {
        return this.id == null && this.name == null && this.email == null;
    }
}
