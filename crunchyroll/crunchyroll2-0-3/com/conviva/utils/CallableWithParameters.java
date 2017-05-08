// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

public class CallableWithParameters
{
    public interface With1<T>
    {
        void exec(final T p0);
    }
    
    public interface With1Return1<P1, R2>
    {
        R2 call(final P1 p0);
    }
    
    public interface With2<T1, T2>
    {
        void exec(final T1 p0, final T2 p1);
    }
    
    public interface With3<T1, T2, T3>
    {
        void exec(final T1 p0, final T2 p1, final T3 p2);
    }
    
    public interface With5<T1, T2, T3, T4, T5>
    {
        void exec(final T1 p0, final T2 p1, final T3 p2, final T4 p3, final T5 p4);
    }
}
