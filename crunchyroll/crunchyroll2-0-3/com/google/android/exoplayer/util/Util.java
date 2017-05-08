// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;
import java.lang.reflect.Method;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.Context;
import com.google.android.exoplayer.upstream.DataSpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.os.Build;
import android.os.Build$VERSION;
import java.util.regex.Pattern;

public final class Util
{
    public static final String DEVICE;
    public static final String MANUFACTURER;
    public static final int SDK_INT;
    private static final Pattern XS_DATE_TIME_PATTERN;
    private static final Pattern XS_DURATION_PATTERN;
    
    static {
        SDK_INT = Build$VERSION.SDK_INT;
        DEVICE = Build.DEVICE;
        MANUFACTURER = Build.MANUFACTURER;
        XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d+))?([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");
        XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
    }
    
    public static boolean areEqual(final Object o, final Object o2) {
        if (o == null) {
            return o2 == null;
        }
        return o.equals(o2);
    }
    
    public static <T> int binarySearchFloor(final List<? extends Comparable<? super T>> list, final T t, final boolean b, final boolean b2) {
        final int binarySearch = Collections.binarySearch(list, t);
        int n;
        if (binarySearch < 0) {
            n = -(binarySearch + 2);
        }
        else {
            n = binarySearch;
            if (!b) {
                n = binarySearch - 1;
            }
        }
        int max = n;
        if (b2) {
            max = Math.max(0, n);
        }
        return max;
    }
    
    public static int binarySearchFloor(final long[] array, final long n, final boolean b, final boolean b2) {
        final int binarySearch = Arrays.binarySearch(array, n);
        int n2;
        if (binarySearch < 0) {
            n2 = -(binarySearch + 2);
        }
        else {
            n2 = binarySearch;
            if (!b) {
                n2 = binarySearch - 1;
            }
        }
        int max = n2;
        if (b2) {
            max = Math.max(0, n2);
        }
        return max;
    }
    
    public static int ceilDivide(final int n, final int n2) {
        return (n + n2 - 1) / n2;
    }
    
    public static boolean contains(final Object[] array, final Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (areEqual(array[i], o)) {
                return true;
            }
        }
        return false;
    }
    
    public static int getBottomInt(final long n) {
        return (int)n;
    }
    
    public static int getIntegerCodeForString(final String s) {
        final int length = s.length();
        Assertions.checkArgument(length <= 4);
        int n = 0;
        for (int i = 0; i < length; ++i) {
            n = (n << 8 | s.charAt(i));
        }
        return n;
    }
    
    public static long getLong(final int n, final int n2) {
        return n << 32 | (n2 & 0xFFFFFFFFL);
    }
    
    public static DataSpec getRemainderDataSpec(final DataSpec dataSpec, final int n) {
        long n2 = -1L;
        if (n == 0) {
            return dataSpec;
        }
        if (dataSpec.length != -1L) {
            n2 = dataSpec.length - n;
        }
        return new DataSpec(dataSpec.uri, dataSpec.position + n, n2, dataSpec.key, dataSpec.flags);
    }
    
    public static int getTopInt(final long n) {
        return (int)(n >>> 32);
    }
    
    public static String getUserAgent(final Context context, final String s) {
        try {
            final String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return s + "/" + versionName + " (Linux;Android " + Build$VERSION.RELEASE + ") " + "ExoPlayerLib/" + "1.5.0";
        }
        catch (PackageManager$NameNotFoundException ex) {
            final String versionName = "?";
            return s + "/" + versionName + " (Linux;Android " + Build$VERSION.RELEASE + ") " + "ExoPlayerLib/" + "1.5.0";
        }
    }
    
    public static void maybeTerminateInputStream(final HttpURLConnection httpURLConnection, final long n) {
        if (Util.SDK_INT == 19 || Util.SDK_INT == 20) {
            try {
                final InputStream inputStream = httpURLConnection.getInputStream();
                if (n != -1L) {
                    goto Label_0099;
                }
                if (inputStream.read() != -1) {
                    final String name = inputStream.getClass().getName();
                    if (name.equals("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream") || name.equals("com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream")) {
                        final Method declaredMethod = inputStream.getClass().getSuperclass().getDeclaredMethod("unexpectedEndOfInput", (Class<?>[])new Class[0]);
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(inputStream, new Object[0]);
                    }
                }
            }
            catch (IOException ex) {}
            catch (Exception ex2) {}
        }
    }
    
    public static ExecutorService newSingleThreadExecutor(final String s) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                return new Thread(runnable, s);
            }
        });
    }
    
    public static long scaleLargeTimestamp(final long n, final long n2, final long n3) {
        if (n3 >= n2 && n3 % n2 == 0L) {
            return n / (n3 / n2);
        }
        if (n3 < n2 && n2 % n3 == 0L) {
            return n * (n2 / n3);
        }
        return n * (n2 / n3);
    }
    
    public static int[] toArray(final List<Integer> list) {
        int[] array;
        if (list == null) {
            array = null;
        }
        else {
            final int size = list.size();
            final int[] array2 = new int[size];
            int n = 0;
            while (true) {
                array = array2;
                if (n >= size) {
                    break;
                }
                array2[n] = list.get(n);
                ++n;
            }
        }
        return array;
    }
    
    public static String toLowerInvariant(final String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase(Locale.US);
    }
}
