// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

public enum DeliveryMechanism
{
    APP_STORE(4), 
    DEVELOPER(1), 
    TEST_DISTRIBUTION(3), 
    USER_SIDELOAD(2);
    
    private final int id;
    
    private DeliveryMechanism(final int id) {
        this.id = id;
    }
    
    public static DeliveryMechanism determineFrom(final String s) {
        if ("io.crash.air".equals(s)) {
            return DeliveryMechanism.TEST_DISTRIBUTION;
        }
        if (s != null) {
            return DeliveryMechanism.APP_STORE;
        }
        return DeliveryMechanism.DEVELOPER;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}
