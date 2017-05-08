// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import java.util.Arrays;

public final class NalUnitUtil
{
    public static final float[] ASPECT_RATIO_IDC_VALUES;
    public static final byte[] NAL_START_CODE;
    private static int[] scratchEscapePositions;
    private static final Object scratchEscapePositionsLock;
    
    static {
        NAL_START_CODE = new byte[] { 0, 0, 0, 1 };
        ASPECT_RATIO_IDC_VALUES = new float[] { 1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f };
        scratchEscapePositionsLock = new Object();
        NalUnitUtil.scratchEscapePositions = new int[10];
    }
    
    public static void clearPrefixFlags(final boolean[] array) {
        array[0] = false;
        array[2] = (array[1] = false);
    }
    
    public static int findNalUnit(final byte[] array, int i, final int n, final boolean[] array2) {
        final boolean b = true;
        final int n2 = n - i;
        Assertions.checkState(n2 >= 0);
        if (n2 != 0) {
            if (array2 != null) {
                if (array2[0]) {
                    clearPrefixFlags(array2);
                    return i - 3;
                }
                if (n2 > 1 && array2[1] && array[i] == 1) {
                    clearPrefixFlags(array2);
                    return i - 2;
                }
                if (n2 > 2 && array2[2] && array[i] == 0 && array[i + 1] == 1) {
                    clearPrefixFlags(array2);
                    return i - 1;
                }
            }
            for (i += 2; i < n - 1; i += 3) {
                if ((array[i] & 0xFE) == 0x0) {
                    if (array[i - 2] == 0 && array[i - 1] == 0 && array[i] == 1) {
                        if (array2 != null) {
                            clearPrefixFlags(array2);
                        }
                        return i - 2;
                    }
                    i -= 2;
                }
            }
            if (array2 != null) {
                boolean b2;
                if (n2 > 2) {
                    b2 = (array[n - 3] == 0 && array[n - 2] == 0 && array[n - 1] == 1);
                }
                else if (n2 == 2) {
                    b2 = (array2[2] && array[n - 2] == 0 && array[n - 1] == 1);
                }
                else {
                    b2 = (array2[1] && array[n - 1] == 1);
                }
                array2[0] = b2;
                boolean b3;
                if (n2 > 1) {
                    b3 = (array[n - 2] == 0 && array[n - 1] == 0);
                }
                else {
                    b3 = (array2[2] && array[n - 1] == 0);
                }
                array2[1] = b3;
                array2[2] = (array[n - 1] == 0 && b);
                return n;
            }
        }
        return n;
    }
    
    private static int findNextUnescapeIndex(final byte[] array, int i, final int n) {
        while (i < n - 2) {
            if (array[i] == 0 && array[i + 1] == 0 && array[i + 2] == 3) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static int getH265NalUnitType(final byte[] array, final int n) {
        return (array[n + 3] & 0x7E) >> 1;
    }
    
    public static int getNalUnitType(final byte[] array, final int n) {
        return array[n + 3] & 0x1F;
    }
    
    public static int unescapeStream(final byte[] array, int n) {
        final Object scratchEscapePositionsLock = NalUnitUtil.scratchEscapePositionsLock;
        // monitorenter(scratchEscapePositionsLock)
        int nextUnescapeIndex = 0;
        int n2 = 0;
    Label_0092_Outer:
        while (true) {
            Label_0080: {
                if (nextUnescapeIndex >= n) {
                    break Label_0080;
                }
                try {
                    final int n3 = nextUnescapeIndex = findNextUnescapeIndex(array, nextUnescapeIndex, n);
                    if (n3 < n) {
                        if (NalUnitUtil.scratchEscapePositions.length <= n2) {
                            NalUnitUtil.scratchEscapePositions = Arrays.copyOf(NalUnitUtil.scratchEscapePositions, NalUnitUtil.scratchEscapePositions.length * 2);
                        }
                        NalUnitUtil.scratchEscapePositions[n2] = n3;
                        nextUnescapeIndex = n3 + 3;
                        ++n2;
                        continue Label_0092_Outer;
                    }
                    continue Label_0092_Outer;
                    final int n4 = n - n2;
                    nextUnescapeIndex = 0;
                    int n5 = 0;
                    n = 0;
                    // iftrue(Label_0159:, n >= n2)
                    while (true) {
                        final int n6 = NalUnitUtil.scratchEscapePositions[n] - nextUnescapeIndex;
                        System.arraycopy(array, nextUnescapeIndex, array, n5, n6);
                        final int n7 = n5 + n6;
                        final int n8 = n7 + 1;
                        array[n7] = 0;
                        n5 = n8 + 1;
                        array[n8] = 0;
                        nextUnescapeIndex += n6 + 3;
                        ++n;
                        continue;
                    }
                    Label_0159: {
                        System.arraycopy(array, nextUnescapeIndex, array, n5, n4 - n5);
                    }
                    // monitorexit(scratchEscapePositionsLock)
                    return n4;
                }
                finally {
                    try {
                    }
                    // monitorexit(scratchEscapePositionsLock)
                    finally {}
                }
            }
        }
    }
}
