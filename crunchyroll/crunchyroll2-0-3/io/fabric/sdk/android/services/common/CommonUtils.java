// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import android.os.Build;
import android.provider.Settings$Secure;
import android.os.Debug;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import android.text.TextUtils;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.content.res.Resources;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.ActivityManager$RunningAppProcessInfo;
import java.io.Flushable;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import io.fabric.sdk.android.Fabric;
import java.io.Closeable;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.StatFs;
import android.app.ActivityManager;
import android.app.ActivityManager$MemoryInfo;
import android.content.Context;
import java.io.File;
import java.util.Comparator;

public class CommonUtils
{
    public static final Comparator<File> FILE_MODIFIED_COMPARATOR;
    private static final char[] HEX_VALUES;
    private static Boolean clsTrace;
    private static Boolean loggingEnabled;
    private static long totalRamInBytes;
    
    static {
        CommonUtils.clsTrace = null;
        HEX_VALUES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        CommonUtils.totalRamInBytes = -1L;
        CommonUtils.loggingEnabled = null;
        FILE_MODIFIED_COMPARATOR = new Comparator<File>() {
            @Override
            public int compare(final File file, final File file2) {
                return (int)(file.lastModified() - file2.lastModified());
            }
        };
    }
    
    public static long calculateFreeRamInBytes(final Context context) {
        final ActivityManager$MemoryInfo activityManager$MemoryInfo = new ActivityManager$MemoryInfo();
        ((ActivityManager)context.getSystemService("activity")).getMemoryInfo(activityManager$MemoryInfo);
        return activityManager$MemoryInfo.availMem;
    }
    
    public static long calculateUsedDiskSpaceInBytes(final String s) {
        final StatFs statFs = new StatFs(s);
        final long n = statFs.getBlockSize();
        return n * statFs.getBlockCount() - n * statFs.getAvailableBlocks();
    }
    
    public static boolean canTryConnection(final Context context) {
        if (checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
            final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkPermission(final Context context, final String s) {
        return context.checkCallingOrSelfPermission(s) == 0;
    }
    
    public static void closeOrLog(final Closeable closeable, final String s) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {
            Fabric.getLogger().e("Fabric", s, ex);
        }
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    static long convertMemInfoToBytes(final String s, final String s2, final int n) {
        return Long.parseLong(s.split(s2)[0].trim()) * n;
    }
    
    public static void copyStream(final InputStream inputStream, final OutputStream outputStream, final byte[] array) throws IOException {
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    @SuppressLint({ "GetInstance" })
    public static Cipher createCipher(final int n, final String s) throws InvalidKeyException {
        if (s.length() < 32) {
            throw new InvalidKeyException("Key must be at least 32 bytes.");
        }
        final SecretKeySpec secretKeySpec = new SecretKeySpec(s.getBytes(), 0, 32, "AES/ECB/PKCS7Padding");
        try {
            final Cipher instance = Cipher.getInstance("AES/ECB/PKCS7Padding");
            instance.init(n, secretKeySpec);
            return instance;
        }
        catch (GeneralSecurityException ex) {
            Fabric.getLogger().e("Fabric", "Could not create Cipher for AES/ECB/PKCS7Padding - should never happen.", ex);
            throw new RuntimeException(ex);
        }
    }
    
    public static String createInstanceIdFrom(final String... array) {
        if (array != null && array.length != 0) {
            final ArrayList<String> list = (ArrayList<String>)new ArrayList<Comparable>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                if (s != null) {
                    list.add(s.replace("-", "").toLowerCase(Locale.US));
                }
            }
            Collections.sort((List<Comparable>)list);
            final StringBuilder sb = new StringBuilder();
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
            }
            final String string = sb.toString();
            if (string.length() > 0) {
                return sha1(string);
            }
        }
        return null;
    }
    
    public static String extractFieldFromSystemFile(final File p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore          4
        //     5: aload_0        
        //     6: invokevirtual   java/io/File.exists:()Z
        //     9: ifeq            90
        //    12: aconst_null    
        //    13: astore_2       
        //    14: aconst_null    
        //    15: astore          5
        //    17: new             Ljava/io/BufferedReader;
        //    20: dup            
        //    21: new             Ljava/io/FileReader;
        //    24: dup            
        //    25: aload_0        
        //    26: invokespecial   java/io/FileReader.<init>:(Ljava/io/File;)V
        //    29: sipush          1024
        //    32: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    35: astore_3       
        //    36: aload_3        
        //    37: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    40: astore          5
        //    42: aload           4
        //    44: astore_2       
        //    45: aload           5
        //    47: ifnull          83
        //    50: ldc_w           "\\s*:\\s*"
        //    53: invokestatic    java/util/regex/Pattern.compile:(Ljava/lang/String;)Ljava/util/regex/Pattern;
        //    56: aload           5
        //    58: iconst_2       
        //    59: invokevirtual   java/util/regex/Pattern.split:(Ljava/lang/CharSequence;I)[Ljava/lang/String;
        //    62: astore_2       
        //    63: aload_2        
        //    64: arraylength    
        //    65: iconst_1       
        //    66: if_icmple       36
        //    69: aload_2        
        //    70: iconst_0       
        //    71: aaload         
        //    72: aload_1        
        //    73: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    76: ifeq            36
        //    79: aload_2        
        //    80: iconst_1       
        //    81: aaload         
        //    82: astore_2       
        //    83: aload_3        
        //    84: ldc_w           "Failed to close system file reader."
        //    87: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    90: aload_2        
        //    91: areturn        
        //    92: astore_3       
        //    93: aload           5
        //    95: astore_1       
        //    96: aload_1        
        //    97: astore_2       
        //    98: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   101: ldc             "Fabric"
        //   103: new             Ljava/lang/StringBuilder;
        //   106: dup            
        //   107: invokespecial   java/lang/StringBuilder.<init>:()V
        //   110: ldc_w           "Error parsing "
        //   113: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   116: aload_0        
        //   117: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   120: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   123: aload_3        
        //   124: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   129: aload_1        
        //   130: ldc_w           "Failed to close system file reader."
        //   133: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   136: aconst_null    
        //   137: areturn        
        //   138: astore_0       
        //   139: aload_2        
        //   140: ldc_w           "Failed to close system file reader."
        //   143: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   146: aload_0        
        //   147: athrow         
        //   148: astore_0       
        //   149: aload_3        
        //   150: astore_2       
        //   151: goto            139
        //   154: astore_2       
        //   155: aload_3        
        //   156: astore_1       
        //   157: aload_2        
        //   158: astore_3       
        //   159: goto            96
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     36     92     96     Ljava/lang/Exception;
        //  17     36     138    139    Any
        //  36     42     154    162    Ljava/lang/Exception;
        //  36     42     148    154    Any
        //  50     79     154    162    Ljava/lang/Exception;
        //  50     79     148    154    Any
        //  98     129    138    139    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0036:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void flushOrLog(final Flushable flushable, final String s) {
        if (flushable == null) {
            return;
        }
        try {
            flushable.flush();
        }
        catch (IOException ex) {
            Fabric.getLogger().e("Fabric", s, ex);
        }
    }
    
    public static String getAppIconHashOrNull(final Context context) {
        Closeable closeable = null;
        Closeable openRawResource = null;
        try {
            final Closeable closeable2 = closeable = (openRawResource = context.getResources().openRawResource(getAppIconResourceId(context)));
            final String sha1 = sha1((InputStream)closeable2);
            openRawResource = closeable2;
            closeable = closeable2;
            final boolean nullOrEmpty = isNullOrEmpty(sha1);
            openRawResource = (Closeable)sha1;
            if (nullOrEmpty) {
                openRawResource = null;
            }
            return (String)openRawResource;
        }
        catch (Exception ex) {
            closeable = openRawResource;
            Fabric.getLogger().e("Fabric", "Could not calculate hash for app icon.", ex);
            return null;
        }
        finally {
            closeOrLog(closeable, "Failed to close icon input stream.");
        }
    }
    
    public static int getAppIconResourceId(final Context context) {
        return context.getApplicationContext().getApplicationInfo().icon;
    }
    
    public static ActivityManager$RunningAppProcessInfo getAppProcessInfo(final String s, final Context context) {
        final List runningAppProcesses = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses();
        ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo2;
        final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo = activityManager$RunningAppProcessInfo2 = null;
        if (runningAppProcesses != null) {
            final Iterator<ActivityManager$RunningAppProcessInfo> iterator = runningAppProcesses.iterator();
            do {
                activityManager$RunningAppProcessInfo2 = activityManager$RunningAppProcessInfo;
                if (!iterator.hasNext()) {
                    break;
                }
                activityManager$RunningAppProcessInfo2 = iterator.next();
            } while (!activityManager$RunningAppProcessInfo2.processName.equals(s));
        }
        return activityManager$RunningAppProcessInfo2;
    }
    
    public static float getBatteryLevel(final Context context) {
        final Intent registerReceiver = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return registerReceiver.getIntExtra("level", -1) / registerReceiver.getIntExtra("scale", -1);
    }
    
    public static int getBatteryVelocity(final Context context, final boolean b) {
        final float batteryLevel = getBatteryLevel(context);
        if (!b) {
            return 1;
        }
        if (b && batteryLevel >= 99.0) {
            return 3;
        }
        if (b && batteryLevel < 99.0) {
            return 2;
        }
        return 0;
    }
    
    public static boolean getBooleanResourceValue(final Context context, final String s, final boolean b) {
        boolean boolean1 = b;
        if (context != null) {
            final Resources resources = context.getResources();
            boolean1 = b;
            if (resources != null) {
                final int resourcesIdentifier = getResourcesIdentifier(context, s, "bool");
                if (resourcesIdentifier > 0) {
                    boolean1 = resources.getBoolean(resourcesIdentifier);
                }
                else {
                    final int resourcesIdentifier2 = getResourcesIdentifier(context, s, "string");
                    boolean1 = b;
                    if (resourcesIdentifier2 > 0) {
                        return Boolean.parseBoolean(context.getString(resourcesIdentifier2));
                    }
                }
            }
        }
        return boolean1;
    }
    
    public static int getCpuArchitectureInt() {
        return Architecture.getValue().ordinal();
    }
    
    public static int getDeviceState(final Context context) {
        boolean b = false;
        if (isEmulator(context)) {
            b = (false | true);
        }
        int n = b ? 1 : 0;
        if (isRooted(context)) {
            n = ((b ? 1 : 0) | 0x2);
        }
        int n2 = n;
        if (isDebuggerAttached()) {
            n2 = (n | 0x4);
        }
        return n2;
    }
    
    public static boolean getProximitySensorEnabled(final Context context) {
        return !isEmulator(context) && ((SensorManager)context.getSystemService("sensor")).getDefaultSensor(8) != null;
    }
    
    public static String getResourcePackageName(final Context context) {
        final int icon = context.getApplicationContext().getApplicationInfo().icon;
        if (icon > 0) {
            return context.getResources().getResourcePackageName(icon);
        }
        return context.getPackageName();
    }
    
    public static int getResourcesIdentifier(final Context context, final String s, final String s2) {
        return context.getResources().getIdentifier(s, s2, getResourcePackageName(context));
    }
    
    public static SharedPreferences getSharedPrefs(final Context context) {
        return context.getSharedPreferences("com.crashlytics.prefs", 0);
    }
    
    public static String getStringsFileValue(final Context context, final String s) {
        final int resourcesIdentifier = getResourcesIdentifier(context, s, "string");
        if (resourcesIdentifier > 0) {
            return context.getString(resourcesIdentifier);
        }
        return "";
    }
    
    public static long getTotalRamInBytes() {
        synchronized (CommonUtils.class) {
            while (true) {
                Label_0080: {
                    if (CommonUtils.totalRamInBytes != -1L) {
                        break Label_0080;
                    }
                    final long n = 0L;
                    String s = extractFieldFromSystemFile(new File("/proc/meminfo"), "MemTotal");
                    long totalRamInBytes = n;
                    if (TextUtils.isEmpty((CharSequence)s)) {
                        break Label_0076;
                    }
                    s = s.toUpperCase(Locale.US);
                    try {
                        if (s.endsWith("KB")) {
                            totalRamInBytes = convertMemInfoToBytes(s, "KB", 1024);
                        }
                        else if (s.endsWith("MB")) {
                            totalRamInBytes = convertMemInfoToBytes(s, "MB", 1048576);
                        }
                        else if (s.endsWith("GB")) {
                            totalRamInBytes = convertMemInfoToBytes(s, "GB", 1073741824);
                        }
                        else {
                            Fabric.getLogger().d("Fabric", "Unexpected meminfo format while computing RAM: " + s);
                            totalRamInBytes = n;
                        }
                        CommonUtils.totalRamInBytes = totalRamInBytes;
                        return CommonUtils.totalRamInBytes;
                    }
                    catch (NumberFormatException ex) {
                        Fabric.getLogger().e("Fabric", "Unexpected meminfo format while computing RAM: " + s, ex);
                        totalRamInBytes = n;
                        continue;
                    }
                }
                continue;
            }
        }
    }
    
    private static String hash(final InputStream inputStream, final String s) {
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("SHA-1");
            final byte[] array = new byte[1024];
            while (true) {
                final int read = inputStream.read(array);
                if (read == -1) {
                    break;
                }
                instance.update(array, 0, read);
            }
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Could not calculate hash for app icon.", ex);
            return "";
        }
        return hexify(instance.digest());
    }
    
    private static String hash(final String s, final String s2) {
        return hash(s.getBytes(), s2);
    }
    
    private static String hash(final byte[] array, final String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance(s);
            instance.update(array);
            return hexify(instance.digest());
        }
        catch (NoSuchAlgorithmException ex) {
            Fabric.getLogger().e("Fabric", "Could not create hashing algorithm: " + s + ", returning empty string.", ex);
            return "";
        }
    }
    
    public static String hexify(final byte[] array) {
        final char[] array2 = new char[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            array2[i * 2] = CommonUtils.HEX_VALUES[n >>> 4];
            array2[i * 2 + 1] = CommonUtils.HEX_VALUES[n & 0xF];
        }
        return new String(array2);
    }
    
    public static boolean isAppDebuggable(final Context context) {
        return (context.getApplicationInfo().flags & 0x2) != 0x0;
    }
    
    public static boolean isClsTrace(final Context context) {
        if (CommonUtils.clsTrace == null) {
            CommonUtils.clsTrace = getBooleanResourceValue(context, "com.crashlytics.Trace", false);
        }
        return CommonUtils.clsTrace;
    }
    
    public static boolean isDebuggerAttached() {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
    }
    
    public static boolean isEmulator(final Context context) {
        final String string = Settings$Secure.getString(context.getContentResolver(), "android_id");
        return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) || string == null;
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static boolean isRooted(final Context context) {
        final boolean emulator = isEmulator(context);
        final String tags = Build.TAGS;
        if ((emulator || tags == null || !tags.contains("test-keys")) && !new File("/system/app/Superuser.apk").exists()) {
            final File file = new File("/system/xbin/su");
            if (emulator || !file.exists()) {
                return false;
            }
        }
        return true;
    }
    
    public static void logControlled(final Context context, final int n, final String s, final String s2) {
        if (isClsTrace(context)) {
            Fabric.getLogger().log(n, "Fabric", s2);
        }
    }
    
    public static void logControlled(final Context context, final String s) {
        if (isClsTrace(context)) {
            Fabric.getLogger().d("Fabric", s);
        }
    }
    
    public static void logControlledError(final Context context, final String s, final Throwable t) {
        if (isClsTrace(context)) {
            Fabric.getLogger().e("Fabric", s);
        }
    }
    
    public static String resolveBuildId(final Context context) {
        String string = null;
        int n;
        if ((n = getResourcesIdentifier(context, "io.fabric.android.build_id", "string")) == 0) {
            n = getResourcesIdentifier(context, "com.crashlytics.android.build_id", "string");
        }
        if (n != 0) {
            string = context.getResources().getString(n);
            Fabric.getLogger().d("Fabric", "Build ID is: " + string);
        }
        return string;
    }
    
    public static String sha1(final InputStream inputStream) {
        return hash(inputStream, "SHA-1");
    }
    
    public static String sha1(final String s) {
        return hash(s, "SHA-1");
    }
    
    public static String streamToString(final InputStream inputStream) throws IOException {
        final Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        if (useDelimiter.hasNext()) {
            return useDelimiter.next();
        }
        return "";
    }
    
    enum Architecture
    {
        ARM64, 
        ARMV6, 
        ARMV7, 
        ARMV7S, 
        ARM_UNKNOWN, 
        PPC, 
        PPC64, 
        UNKNOWN, 
        X86_32, 
        X86_64;
        
        private static final Map<String, Architecture> matcher;
        
        static {
            (matcher = new HashMap<String, Architecture>(4)).put("armeabi-v7a", Architecture.ARMV7);
            Architecture.matcher.put("armeabi", Architecture.ARMV6);
            Architecture.matcher.put("x86", Architecture.X86_32);
        }
        
        static Architecture getValue() {
            final String cpu_ABI = Build.CPU_ABI;
            Architecture unknown;
            if (TextUtils.isEmpty((CharSequence)cpu_ABI)) {
                Fabric.getLogger().d("Fabric", "Architecture#getValue()::Build.CPU_ABI returned null or empty");
                unknown = Architecture.UNKNOWN;
            }
            else if ((unknown = Architecture.matcher.get(cpu_ABI.toLowerCase(Locale.US))) == null) {
                return Architecture.UNKNOWN;
            }
            return unknown;
        }
    }
}
