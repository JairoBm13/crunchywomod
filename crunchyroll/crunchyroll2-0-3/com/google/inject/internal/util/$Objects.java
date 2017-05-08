// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.Arrays;

public final class $Objects
{
    public static boolean equal(@$Nullable final Object o, @$Nullable final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int hashCode(final Object... array) {
        return Arrays.hashCode(array);
    }
}
