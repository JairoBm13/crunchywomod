// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

public enum SwrveActionType
{
    Custom, 
    Dismiss, 
    Install;
    
    public static SwrveActionType parse(final String s) {
        if (s.equalsIgnoreCase("install")) {
            return SwrveActionType.Install;
        }
        if (s.equalsIgnoreCase("dismiss")) {
            return SwrveActionType.Dismiss;
        }
        return SwrveActionType.Custom;
    }
}
