// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

public enum SwrveOrientation
{
    Both, 
    Landscape, 
    Portrait;
    
    public static SwrveOrientation parse(final int n) {
        if (n == 1) {
            return SwrveOrientation.Portrait;
        }
        return SwrveOrientation.Landscape;
    }
    
    public static SwrveOrientation parse(final String s) {
        if (s.equalsIgnoreCase("portrait")) {
            return SwrveOrientation.Portrait;
        }
        if (s.equalsIgnoreCase("both")) {
            return SwrveOrientation.Both;
        }
        return SwrveOrientation.Landscape;
    }
}
