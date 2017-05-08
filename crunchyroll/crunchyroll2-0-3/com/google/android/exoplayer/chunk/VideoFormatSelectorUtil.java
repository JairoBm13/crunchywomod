// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.chunk;

import android.view.WindowManager;
import android.content.Context;
import com.google.android.exoplayer.MediaCodecUtil;
import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;
import com.google.android.exoplayer.util.Util;
import android.graphics.Point;
import android.view.Display;

public final class VideoFormatSelectorUtil
{
    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        if (Util.SDK_INT >= 17) {
            getDisplaySizeV17(display, point);
            return point;
        }
        if (Util.SDK_INT >= 16) {
            getDisplaySizeV16(display, point);
            return point;
        }
        getDisplaySizeV9(display, point);
        return point;
    }
    
    @TargetApi(16)
    private static void getDisplaySizeV16(final Display display, final Point point) {
        display.getSize(point);
    }
    
    @TargetApi(17)
    private static void getDisplaySizeV17(final Display display, final Point point) {
        display.getRealSize(point);
    }
    
    private static void getDisplaySizeV9(final Display display, final Point point) {
        point.x = display.getWidth();
        point.y = display.getHeight();
    }
    
    private static Point getMaxVideoSizeInViewport(final boolean b, final int n, final int n2, final int n3, final int n4) {
        int n5 = 1;
        int n6 = n;
        int n7 = n2;
        if (b) {
            int n8;
            if (n3 > n4) {
                n8 = 1;
            }
            else {
                n8 = 0;
            }
            if (n <= n2) {
                n5 = 0;
            }
            n6 = n;
            n7 = n2;
            if (n8 != n5) {
                n7 = n;
                n6 = n2;
            }
        }
        if (n3 * n7 >= n4 * n6) {
            return new Point(n6, Util.ceilDivide(n6 * n4, n3));
        }
        return new Point(Util.ceilDivide(n7 * n3, n4), n7);
    }
    
    private static boolean isFormatPlayable(final Format format, final String[] array, final boolean b, final int n) {
        return (array == null || Util.contains(array, format.mimeType)) && (!b || (format.width < 1280 && format.height < 720)) && (format.width <= 0 || format.height <= 0 || format.width * format.height <= n);
    }
    
    public static int[] selectVideoFormats(final List<? extends FormatWrapper> list, final String[] array, final boolean b, final boolean b2, int i, final int n) throws MediaCodecUtil.DecoderQueryException {
        int n2 = Integer.MAX_VALUE;
        final ArrayList<Integer> list2 = new ArrayList<Integer>();
        final int maxH264DecodableFrameSize = MediaCodecUtil.maxH264DecodableFrameSize();
        int n3;
        for (int size = list.size(), j = 0; j < size; ++j, n2 = n3) {
            final Format format = ((FormatWrapper)list.get(j)).getFormat();
            n3 = n2;
            if (isFormatPlayable(format, array, b, maxH264DecodableFrameSize)) {
                list2.add(j);
                n3 = n2;
                if (format.width > 0) {
                    n3 = n2;
                    if (format.height > 0) {
                        final Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(b2, i, n, format.width, format.height);
                        final int n4 = format.width * format.height;
                        n3 = n2;
                        if (format.width >= (int)(maxVideoSizeInViewport.x * 0.98f)) {
                            n3 = n2;
                            if (format.height >= (int)(maxVideoSizeInViewport.y * 0.98f) && n4 < (n3 = n2)) {
                                n3 = n4;
                            }
                        }
                    }
                }
            }
        }
        Format format2;
        for (i = list2.size() - 1; i >= 0; --i) {
            format2 = ((FormatWrapper)list.get(list2.get(i))).getFormat();
            if (format2.width > 0 && format2.height > 0 && format2.width * format2.height > n2) {
                list2.remove(i);
            }
        }
        return Util.toArray(list2);
    }
    
    public static int[] selectVideoFormatsForDefaultDisplay(final Context context, final List<? extends FormatWrapper> list, final String[] array, final boolean b) throws MediaCodecUtil.DecoderQueryException {
        final Point displaySize = getDisplaySize(((WindowManager)context.getSystemService("window")).getDefaultDisplay());
        return selectVideoFormats(list, array, b, true, displaySize.x, displaySize.y);
    }
}
