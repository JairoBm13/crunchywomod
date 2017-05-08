// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.math.BigDecimal;

public final class f extends Number
{
    private final String a;
    
    public f(final String a) {
        this.a = a;
    }
    
    @Override
    public double doubleValue() {
        return Double.parseDouble(this.a);
    }
    
    @Override
    public float floatValue() {
        return Float.parseFloat(this.a);
    }
    
    @Override
    public int intValue() {
        try {
            return Integer.parseInt(this.a);
        }
        catch (NumberFormatException ex) {
            try {
                return (int)Long.parseLong(this.a);
            }
            catch (NumberFormatException ex2) {
                return new BigDecimal(this.a).intValue();
            }
        }
    }
    
    @Override
    public long longValue() {
        try {
            return Long.parseLong(this.a);
        }
        catch (NumberFormatException ex) {
            return new BigDecimal(this.a).longValue();
        }
    }
    
    @Override
    public String toString() {
        return this.a;
    }
}
