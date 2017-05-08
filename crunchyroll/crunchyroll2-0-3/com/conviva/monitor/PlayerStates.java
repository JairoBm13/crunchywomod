// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.monitor;

import java.util.HashMap;
import java.util.Map;

public class PlayerStates
{
    public static final String BUFFERING = "BUFFERING";
    public static final String NOTMONITORED = "NOTMONITORED";
    public static final String PAUSED = "PAUSED";
    public static final String PLAYING = "PLAYING";
    public static final String STOPPED = "STOPPED";
    public static final String UNKNOWN = "UNKNOWN";
    public static final int eBuffering = 6;
    public static final int eNotMonitored = 98;
    public static final int ePaused = 12;
    public static final int ePlaying = 3;
    public static final int eStopped = 1;
    public static final int eUnknown = 100;
    public static Map<String, Integer> stateToInt;
    
    static {
        PlayerStates.stateToInt = null;
    }
    
    public static void cleanup() {
        PlayerStates.stateToInt = null;
    }
    
    public static void init() {
        (PlayerStates.stateToInt = new HashMap<String, Integer>()).put("PLAYING", 3);
        PlayerStates.stateToInt.put("STOPPED", 1);
        PlayerStates.stateToInt.put("PAUSED", 12);
        PlayerStates.stateToInt.put("BUFFERING", 6);
        PlayerStates.stateToInt.put("NOTMONITORED", 98);
        PlayerStates.stateToInt.put("UNKNOWN", 100);
    }
}
