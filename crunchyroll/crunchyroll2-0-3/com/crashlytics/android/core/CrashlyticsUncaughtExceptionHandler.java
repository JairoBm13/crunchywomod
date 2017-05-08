// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.concurrent.Callable;
import java.util.Iterator;
import android.app.ActivityManager$RunningAppProcessInfo;
import java.util.List;
import java.util.TreeMap;
import java.util.LinkedList;
import android.os.Environment;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.Set;
import java.util.HashSet;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import java.util.Arrays;
import io.fabric.sdk.android.Fabric;
import java.util.Date;
import java.io.IOException;
import com.crashlytics.android.core.internal.models.SessionEventData;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import java.util.Collections;
import android.content.BroadcastReceiver;
import java.util.concurrent.atomic.AtomicBoolean;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.Map;
import java.io.File;
import java.util.Comparator;
import java.io.FilenameFilter;

class CrashlyticsUncaughtExceptionHandler implements UncaughtExceptionHandler
{
    static final FilenameFilter ANY_SESSION_FILENAME_FILTER;
    static final Comparator<File> LARGEST_FILE_NAME_FIRST;
    private static final Map<String, String> SEND_AT_CRASHTIME_HEADER;
    static final FilenameFilter SESSION_FILE_FILTER;
    private static final Pattern SESSION_FILE_PATTERN;
    static final Comparator<File> SMALLEST_FILE_NAME_FIRST;
    private final CrashlyticsCore crashlyticsCore;
    private final UncaughtExceptionHandler defaultHandler;
    private final AtomicInteger eventCounter;
    private final CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private final File filesDir;
    private final IdManager idManager;
    private final AtomicBoolean isHandlingException;
    private final LogFileManager logFileManager;
    private boolean powerConnected;
    private final BroadcastReceiver powerConnectedReceiver;
    private final BroadcastReceiver powerDisconnectedReceiver;
    private final AtomicBoolean receiversRegistered;
    private final SessionDataWriter sessionDataWriter;
    
    static {
        SESSION_FILE_FILTER = new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return s.length() == ".cls".length() + 35 && s.endsWith(".cls");
            }
        };
        LARGEST_FILE_NAME_FIRST = new Comparator<File>() {
            @Override
            public int compare(final File file, final File file2) {
                return file2.getName().compareTo(file.getName());
            }
        };
        SMALLEST_FILE_NAME_FIRST = new Comparator<File>() {
            @Override
            public int compare(final File file, final File file2) {
                return file.getName().compareTo(file2.getName());
            }
        };
        ANY_SESSION_FILENAME_FILTER = new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(s).matches();
            }
        };
        SESSION_FILE_PATTERN = Pattern.compile("([\\d|A-Z|a-z]{12}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{12}).+");
        SEND_AT_CRASHTIME_HEADER = Collections.singletonMap("X-CRASHLYTICS-SEND-FLAGS", "1");
    }
    
    CrashlyticsUncaughtExceptionHandler(final UncaughtExceptionHandler defaultHandler, final CrashlyticsListener crashlyticsListener, final CrashlyticsExecutorServiceWrapper executorServiceWrapper, final IdManager idManager, final SessionDataWriter sessionDataWriter, final CrashlyticsCore crashlyticsCore) {
        this.eventCounter = new AtomicInteger(0);
        this.receiversRegistered = new AtomicBoolean(false);
        this.defaultHandler = defaultHandler;
        this.executorServiceWrapper = executorServiceWrapper;
        this.idManager = idManager;
        this.crashlyticsCore = crashlyticsCore;
        this.sessionDataWriter = sessionDataWriter;
        this.isHandlingException = new AtomicBoolean(false);
        this.filesDir = crashlyticsCore.getSdkDirectory();
        this.logFileManager = new LogFileManager(crashlyticsCore.getContext(), this.filesDir);
        this.notifyCrashlyticsListenerOfPreviousCrash(crashlyticsListener);
        this.powerConnectedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                CrashlyticsUncaughtExceptionHandler.this.powerConnected = true;
            }
        };
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        this.powerDisconnectedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                CrashlyticsUncaughtExceptionHandler.this.powerConnected = false;
            }
        };
        final IntentFilter intentFilter2 = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        final Context context = crashlyticsCore.getContext();
        context.registerReceiver(this.powerConnectedReceiver, intentFilter);
        context.registerReceiver(this.powerDisconnectedReceiver, intentFilter2);
        this.receiversRegistered.set(true);
    }
    
    private void closeWithoutRenamingOrLog(final ClsFileOutputStream clsFileOutputStream) {
        if (clsFileOutputStream == null) {
            return;
        }
        try {
            clsFileOutputStream.closeInProgressStream();
        }
        catch (IOException ex) {
            Fabric.getLogger().e("CrashlyticsCore", "Error closing session file stream in the presence of an exception", ex);
        }
    }
    
    private void deleteLegacyInvalidCacheDir() {
        final File file = new File(this.crashlyticsCore.getSdkDirectory(), "invalidClsFiles");
        if (file.exists()) {
            if (file.isDirectory()) {
                final File[] listFiles = file.listFiles();
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    listFiles[i].delete();
                }
            }
            file.delete();
        }
    }
    
    private void deleteSessionPartFilesFor(final String s) {
        final File[] listSessionPartFiles = this.listSessionPartFilesFor(s);
        for (int length = listSessionPartFiles.length, i = 0; i < length; ++i) {
            listSessionPartFiles[i].delete();
        }
    }
    
    private void doCloseSessions(final boolean b) throws Exception {
        int i;
        if (b) {
            i = 1;
        }
        else {
            i = 0;
        }
        this.trimOpenSessions(i + 8);
        final File[] listSessionBeginFiles = this.listSessionBeginFiles();
        Arrays.sort(listSessionBeginFiles, CrashlyticsUncaughtExceptionHandler.LARGEST_FILE_NAME_FIRST);
        if (listSessionBeginFiles.length > i) {
            this.writeSessionUser(this.getSessionIdFromSessionFile(listSessionBeginFiles[i]));
            final SessionSettingsData sessionSettingsData = this.crashlyticsCore.getSessionSettingsData();
            if (sessionSettingsData != null) {
                final int maxCustomExceptionEvents = sessionSettingsData.maxCustomExceptionEvents;
                Fabric.getLogger().d("CrashlyticsCore", "Closing open sessions.");
                while (i < listSessionBeginFiles.length) {
                    final File file = listSessionBeginFiles[i];
                    final String sessionIdFromSessionFile = this.getSessionIdFromSessionFile(file);
                    Fabric.getLogger().d("CrashlyticsCore", "Closing session: " + sessionIdFromSessionFile);
                    this.writeSessionPartsToSessionFile(file, sessionIdFromSessionFile, maxCustomExceptionEvents);
                    ++i;
                }
            }
            else {
                Fabric.getLogger().d("CrashlyticsCore", "Unable to close session. Settings are not loaded.");
            }
            return;
        }
        Fabric.getLogger().d("CrashlyticsCore", "No open sessions to be closed.");
    }
    
    private void doOpenSession() throws Exception {
        final Date date = new Date();
        final String string = new CLSUUID(this.idManager).toString();
        Fabric.getLogger().d("CrashlyticsCore", "Opening an new session with ID " + string);
        this.writeBeginSession(string, date);
        this.writeSessionApp(string);
        this.writeSessionOS(string);
        this.writeSessionDevice(string);
        this.logFileManager.setCurrentSession(string);
    }
    
    private File[] ensureFileArrayNotNull(final File[] array) {
        File[] array2 = array;
        if (array == null) {
            array2 = new File[0];
        }
        return array2;
    }
    
    private String getCurrentSessionId() {
        final File[] listSessionBeginFiles = this.listSessionBeginFiles();
        Arrays.sort(listSessionBeginFiles, CrashlyticsUncaughtExceptionHandler.LARGEST_FILE_NAME_FIRST);
        if (listSessionBeginFiles.length > 0) {
            return this.getSessionIdFromSessionFile(listSessionBeginFiles[0]);
        }
        return null;
    }
    
    private String getPreviousSessionId() {
        final File[] listSessionBeginFiles = this.listSessionBeginFiles();
        Arrays.sort(listSessionBeginFiles, CrashlyticsUncaughtExceptionHandler.LARGEST_FILE_NAME_FIRST);
        if (listSessionBeginFiles.length > 1) {
            return this.getSessionIdFromSessionFile(listSessionBeginFiles[1]);
        }
        return null;
    }
    
    private String getSessionIdFromSessionFile(final File file) {
        return file.getName().substring(0, 35);
    }
    
    private UserMetaData getUserMetaData(final String s) {
        if (this.isHandlingException()) {
            return new UserMetaData(this.crashlyticsCore.getUserIdentifier(), this.crashlyticsCore.getUserName(), this.crashlyticsCore.getUserEmail());
        }
        return new MetaDataStore(this.filesDir).readUserData(s);
    }
    
    private void handleUncaughtException(final Date date, final Thread thread, final Throwable t) throws Exception {
        this.writeFatal(date, thread, t);
        this.doCloseSessions();
        this.doOpenSession();
        this.trimSessionFiles();
        if (!this.crashlyticsCore.shouldPromptUserBeforeSendingCrashReports()) {
            this.sendSessionReports();
        }
    }
    
    private File[] listCompleteSessionFiles() {
        return this.listFilesMatching(CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER);
    }
    
    private File[] listFilesMatching(final FilenameFilter filenameFilter) {
        return this.ensureFileArrayNotNull(this.filesDir.listFiles(filenameFilter));
    }
    
    private File[] listSessionPartFilesFor(final String s) {
        return this.listFilesMatching(new SessionPartFileFilter(s));
    }
    
    private void notifyCrashlyticsListenerOfPreviousCrash(final CrashlyticsListener crashlyticsListener) {
        Fabric.getLogger().d("CrashlyticsCore", "Checking for previous crash marker.");
        final File file = new File(this.crashlyticsCore.getSdkDirectory(), "crash_marker");
        if (!file.exists()) {
            return;
        }
        file.delete();
        if (crashlyticsListener == null) {
            return;
        }
        try {
            crashlyticsListener.crashlyticsDidDetectCrashDuringPreviousExecution();
        }
        catch (Exception ex) {
            Fabric.getLogger().e("CrashlyticsCore", "Exception thrown by CrashlyticsListener while notifying of previous crash.", ex);
        }
    }
    
    private void sendSessionReports() {
        final File[] listCompleteSessionFiles = this.listCompleteSessionFiles();
        for (int length = listCompleteSessionFiles.length, i = 0; i < length; ++i) {
            this.executorServiceWrapper.executeAsync(new Runnable() {
                final /* synthetic */ File val$toSend = listCompleteSessionFiles[i];
                
                @Override
                public void run() {
                    if (CommonUtils.canTryConnection(CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getContext())) {
                        Fabric.getLogger().d("CrashlyticsCore", "Attempting to send crash report at time of crash...");
                        final CreateReportSpiCall createReportSpiCall = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getCreateReportSpiCall(Settings.getInstance().awaitSettingsData());
                        if (createReportSpiCall != null) {
                            new ReportUploader(createReportSpiCall).forceUpload(new SessionReport(this.val$toSend, CrashlyticsUncaughtExceptionHandler.SEND_AT_CRASHTIME_HEADER));
                        }
                    }
                }
            });
        }
    }
    
    private void trimOpenSessions(int i) {
        final HashSet<String> set = new HashSet<String>();
        final File[] listSessionBeginFiles = this.listSessionBeginFiles();
        Arrays.sort(listSessionBeginFiles, CrashlyticsUncaughtExceptionHandler.LARGEST_FILE_NAME_FIRST);
        int min;
        for (min = Math.min(i, listSessionBeginFiles.length), i = 0; i < min; ++i) {
            set.add(this.getSessionIdFromSessionFile(listSessionBeginFiles[i]));
        }
        this.logFileManager.discardOldLogFiles(set);
        final File[] listFilesMatching = this.listFilesMatching(new AnySessionPartFileFilter());
        int length;
        File file;
        String name;
        Matcher matcher;
        for (length = listFilesMatching.length, i = 0; i < length; ++i) {
            file = listFilesMatching[i];
            name = file.getName();
            matcher = CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(name);
            matcher.matches();
            if (!set.contains(matcher.group(1))) {
                Fabric.getLogger().d("CrashlyticsCore", "Trimming open session file: " + name);
                file.delete();
            }
        }
    }
    
    private void trimSessionEventFiles(final String s, final int n) {
        Utils.capFileCount(this.filesDir, new FileNameContainsFilter(s + "SessionEvent"), n, CrashlyticsUncaughtExceptionHandler.SMALLEST_FILE_NAME_FIRST);
    }
    
    private void writeBeginSession(final String p0, final Date p1) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          6
        //     3: aconst_null    
        //     4: astore          9
        //     6: aconst_null    
        //     7: astore          5
        //     9: aconst_null    
        //    10: astore          11
        //    12: aconst_null    
        //    13: astore          10
        //    15: aconst_null    
        //    16: astore          8
        //    18: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    21: dup            
        //    22: aload_0        
        //    23: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    26: new             Ljava/lang/StringBuilder;
        //    29: dup            
        //    30: invokespecial   java/lang/StringBuilder.<init>:()V
        //    33: aload_1        
        //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    37: ldc_w           "BeginSession"
        //    40: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    43: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    46: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    49: astore          7
        //    51: aload           11
        //    53: astore          5
        //    55: aload           10
        //    57: astore          6
        //    59: aload           7
        //    61: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //    64: astore          8
        //    66: aload           8
        //    68: astore          5
        //    70: aload           8
        //    72: astore          6
        //    74: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //    77: ldc_w           "Crashlytics Android SDK/%s"
        //    80: iconst_1       
        //    81: anewarray       Ljava/lang/Object;
        //    84: dup            
        //    85: iconst_0       
        //    86: aload_0        
        //    87: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    90: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getVersion:()Ljava/lang/String;
        //    93: aastore        
        //    94: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    97: astore          9
        //    99: aload           8
        //   101: astore          5
        //   103: aload           8
        //   105: astore          6
        //   107: aload_2        
        //   108: invokevirtual   java/util/Date.getTime:()J
        //   111: ldc2_w          1000
        //   114: ldiv           
        //   115: lstore_3       
        //   116: aload           8
        //   118: astore          5
        //   120: aload           8
        //   122: astore          6
        //   124: aload_0        
        //   125: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.sessionDataWriter:Lcom/crashlytics/android/core/SessionDataWriter;
        //   128: aload           8
        //   130: aload_1        
        //   131: aload           9
        //   133: lload_3        
        //   134: invokevirtual   com/crashlytics/android/core/SessionDataWriter.writeBeginSession:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/lang/String;Ljava/lang/String;J)V
        //   137: aload           8
        //   139: ldc_w           "Failed to flush to session begin file."
        //   142: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   145: aload           7
        //   147: ldc_w           "Failed to close begin session file."
        //   150: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   153: return         
        //   154: astore          7
        //   156: aload           9
        //   158: astore_2       
        //   159: aload           8
        //   161: astore_1       
        //   162: aload_1        
        //   163: astore          5
        //   165: aload_2        
        //   166: astore          6
        //   168: aload           7
        //   170: aload_2        
        //   171: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   174: aload_1        
        //   175: astore          5
        //   177: aload_2        
        //   178: astore          6
        //   180: aload           7
        //   182: athrow         
        //   183: astore_1       
        //   184: aload           5
        //   186: ldc_w           "Failed to flush to session begin file."
        //   189: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   192: aload           6
        //   194: ldc_w           "Failed to close begin session file."
        //   197: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   200: aload_1        
        //   201: athrow         
        //   202: astore_1       
        //   203: aload           7
        //   205: astore          6
        //   207: goto            184
        //   210: astore          5
        //   212: aload           7
        //   214: astore_2       
        //   215: aload           6
        //   217: astore_1       
        //   218: aload           5
        //   220: astore          7
        //   222: goto            162
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  18     51     154    162    Ljava/lang/Exception;
        //  18     51     183    184    Any
        //  59     66     210    225    Ljava/lang/Exception;
        //  59     66     202    210    Any
        //  74     99     210    225    Ljava/lang/Exception;
        //  74     99     202    210    Any
        //  107    116    210    225    Ljava/lang/Exception;
        //  107    116    202    210    Any
        //  124    137    210    225    Ljava/lang/Exception;
        //  124    137    202    210    Any
        //  168    174    183    184    Any
        //  180    183    183    184    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0162:
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
    
    private void writeExternalCrashEvent(final SessionEventData p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          6
        //     3: aconst_null    
        //     4: astore          10
        //     6: aconst_null    
        //     7: astore          4
        //     9: aconst_null    
        //    10: astore          5
        //    12: aconst_null    
        //    13: astore          11
        //    15: aconst_null    
        //    16: astore          8
        //    18: aconst_null    
        //    19: astore          7
        //    21: aconst_null    
        //    22: astore          9
        //    24: aload           11
        //    26: astore_2       
        //    27: aload           10
        //    29: astore_3       
        //    30: aload_0        
        //    31: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.getPreviousSessionId:()Ljava/lang/String;
        //    34: astore          12
        //    36: aload           12
        //    38: ifnull          178
        //    41: aload           11
        //    43: astore_2       
        //    44: aload           10
        //    46: astore_3       
        //    47: aload           12
        //    49: invokestatic    com/crashlytics/android/core/CrashlyticsCore.recordFatalExceptionEvent:(Ljava/lang/String;)V
        //    52: aload           11
        //    54: astore_2       
        //    55: aload           10
        //    57: astore_3       
        //    58: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    61: dup            
        //    62: aload_0        
        //    63: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    66: new             Ljava/lang/StringBuilder;
        //    69: dup            
        //    70: invokespecial   java/lang/StringBuilder.<init>:()V
        //    73: aload           12
        //    75: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    78: ldc_w           "SessionCrash"
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    87: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    90: astore          4
        //    92: aload           8
        //    94: astore_2       
        //    95: aload           7
        //    97: astore          5
        //    99: aload           4
        //   101: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //   104: astore_3       
        //   105: aload_3        
        //   106: astore_2       
        //   107: aload_3        
        //   108: astore          5
        //   110: new             Lcom/crashlytics/android/core/MetaDataStore;
        //   113: dup            
        //   114: aload_0        
        //   115: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //   118: invokespecial   com/crashlytics/android/core/MetaDataStore.<init>:(Ljava/io/File;)V
        //   121: aload           12
        //   123: invokevirtual   com/crashlytics/android/core/MetaDataStore.readKeyData:(Ljava/lang/String;)Ljava/util/Map;
        //   126: astore          6
        //   128: aload_3        
        //   129: astore_2       
        //   130: aload_3        
        //   131: astore          5
        //   133: aload_1        
        //   134: new             Lcom/crashlytics/android/core/LogFileManager;
        //   137: dup            
        //   138: aload_0        
        //   139: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //   142: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getContext:()Landroid/content/Context;
        //   145: aload_0        
        //   146: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //   149: aload           12
        //   151: invokespecial   com/crashlytics/android/core/LogFileManager.<init>:(Landroid/content/Context;Ljava/io/File;Ljava/lang/String;)V
        //   154: aload           6
        //   156: aload_3        
        //   157: invokestatic    com/crashlytics/android/core/NativeCrashWriter.writeNativeCrash:(Lcom/crashlytics/android/core/internal/models/SessionEventData;Lcom/crashlytics/android/core/LogFileManager;Ljava/util/Map;Lcom/crashlytics/android/core/CodedOutputStream;)V
        //   160: aload           4
        //   162: astore_1       
        //   163: aload_3        
        //   164: ldc_w           "Failed to flush to session begin file."
        //   167: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   170: aload_1        
        //   171: ldc_w           "Failed to close fatal exception file output stream."
        //   174: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   177: return         
        //   178: aload           11
        //   180: astore_2       
        //   181: aload           10
        //   183: astore_3       
        //   184: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   187: ldc_w           "CrashlyticsCore"
        //   190: ldc_w           "Tried to write a native crash while no session was open."
        //   193: aconst_null    
        //   194: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   199: aload           9
        //   201: astore_3       
        //   202: aload           4
        //   204: astore_1       
        //   205: goto            163
        //   208: astore          4
        //   210: aload           6
        //   212: astore_1       
        //   213: aload           5
        //   215: astore_2       
        //   216: aload_1        
        //   217: astore_3       
        //   218: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   221: ldc_w           "CrashlyticsCore"
        //   224: ldc_w           "An error occurred in the native crash logger"
        //   227: aload           4
        //   229: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   234: aload           5
        //   236: astore_2       
        //   237: aload_1        
        //   238: astore_3       
        //   239: aload           4
        //   241: aload_1        
        //   242: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   245: aload           5
        //   247: ldc_w           "Failed to flush to session begin file."
        //   250: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   253: aload_1        
        //   254: ldc_w           "Failed to close fatal exception file output stream."
        //   257: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   260: return         
        //   261: astore_1       
        //   262: aload_2        
        //   263: ldc_w           "Failed to flush to session begin file."
        //   266: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   269: aload_3        
        //   270: ldc_w           "Failed to close fatal exception file output stream."
        //   273: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   276: aload_1        
        //   277: athrow         
        //   278: astore_1       
        //   279: aload           4
        //   281: astore_3       
        //   282: goto            262
        //   285: astore_2       
        //   286: aload           4
        //   288: astore_1       
        //   289: aload_2        
        //   290: astore          4
        //   292: goto            213
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  30     36     208    213    Ljava/lang/Exception;
        //  30     36     261    262    Any
        //  47     52     208    213    Ljava/lang/Exception;
        //  47     52     261    262    Any
        //  58     92     208    213    Ljava/lang/Exception;
        //  58     92     261    262    Any
        //  99     105    285    295    Ljava/lang/Exception;
        //  99     105    278    285    Any
        //  110    128    285    295    Ljava/lang/Exception;
        //  110    128    278    285    Any
        //  133    160    285    295    Ljava/lang/Exception;
        //  133    160    278    285    Any
        //  184    199    208    213    Ljava/lang/Exception;
        //  184    199    261    262    Any
        //  218    234    261    262    Any
        //  239    245    261    262    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0163:
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
    
    private void writeFatal(final Date p0, final Thread p1, final Throwable p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          8
        //     3: aconst_null    
        //     4: astore          12
        //     6: aconst_null    
        //     7: astore          6
        //     9: aconst_null    
        //    10: astore          7
        //    12: aconst_null    
        //    13: astore          13
        //    15: aconst_null    
        //    16: astore          10
        //    18: aconst_null    
        //    19: astore          9
        //    21: aconst_null    
        //    22: astore          11
        //    24: aload           13
        //    26: astore          4
        //    28: aload           12
        //    30: astore          5
        //    32: new             Ljava/io/File;
        //    35: dup            
        //    36: aload_0        
        //    37: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    40: ldc_w           "crash_marker"
        //    43: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    46: invokevirtual   java/io/File.createNewFile:()Z
        //    49: pop            
        //    50: aload           13
        //    52: astore          4
        //    54: aload           12
        //    56: astore          5
        //    58: aload_0        
        //    59: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.getCurrentSessionId:()Ljava/lang/String;
        //    62: astore          14
        //    64: aload           14
        //    66: ifnull          179
        //    69: aload           13
        //    71: astore          4
        //    73: aload           12
        //    75: astore          5
        //    77: aload           14
        //    79: invokestatic    com/crashlytics/android/core/CrashlyticsCore.recordFatalExceptionEvent:(Ljava/lang/String;)V
        //    82: aload           13
        //    84: astore          4
        //    86: aload           12
        //    88: astore          5
        //    90: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    93: dup            
        //    94: aload_0        
        //    95: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    98: new             Ljava/lang/StringBuilder;
        //   101: dup            
        //   102: invokespecial   java/lang/StringBuilder.<init>:()V
        //   105: aload           14
        //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   110: ldc_w           "SessionCrash"
        //   113: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   116: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   119: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   122: astore          6
        //   124: aload           10
        //   126: astore          4
        //   128: aload           9
        //   130: astore          7
        //   132: aload           6
        //   134: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //   137: astore          5
        //   139: aload           5
        //   141: astore          4
        //   143: aload           5
        //   145: astore          7
        //   147: aload_0        
        //   148: aload           5
        //   150: aload_1        
        //   151: aload_2        
        //   152: aload_3        
        //   153: ldc_w           "crash"
        //   156: iconst_1       
        //   157: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.writeSessionEvent:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/util/Date;Ljava/lang/Thread;Ljava/lang/Throwable;Ljava/lang/String;Z)V
        //   160: aload           6
        //   162: astore_1       
        //   163: aload           5
        //   165: ldc_w           "Failed to flush to session begin file."
        //   168: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   171: aload_1        
        //   172: ldc_w           "Failed to close fatal exception file output stream."
        //   175: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   178: return         
        //   179: aload           13
        //   181: astore          4
        //   183: aload           12
        //   185: astore          5
        //   187: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   190: ldc_w           "CrashlyticsCore"
        //   193: ldc_w           "Tried to write a fatal exception while no session was open."
        //   196: aconst_null    
        //   197: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   202: aload           11
        //   204: astore          5
        //   206: aload           6
        //   208: astore_1       
        //   209: goto            163
        //   212: astore_2       
        //   213: aload           8
        //   215: astore_1       
        //   216: aload           7
        //   218: astore          4
        //   220: aload_1        
        //   221: astore          5
        //   223: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   226: ldc_w           "CrashlyticsCore"
        //   229: ldc_w           "An error occurred in the fatal exception logger"
        //   232: aload_2        
        //   233: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   238: aload           7
        //   240: astore          4
        //   242: aload_1        
        //   243: astore          5
        //   245: aload_2        
        //   246: aload_1        
        //   247: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   250: aload           7
        //   252: ldc_w           "Failed to flush to session begin file."
        //   255: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   258: aload_1        
        //   259: ldc_w           "Failed to close fatal exception file output stream."
        //   262: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   265: return         
        //   266: astore_1       
        //   267: aload           4
        //   269: ldc_w           "Failed to flush to session begin file."
        //   272: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   275: aload           5
        //   277: ldc_w           "Failed to close fatal exception file output stream."
        //   280: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   283: aload_1        
        //   284: athrow         
        //   285: astore_1       
        //   286: aload           6
        //   288: astore          5
        //   290: goto            267
        //   293: astore_2       
        //   294: aload           6
        //   296: astore_1       
        //   297: goto            216
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  32     50     212    216    Ljava/lang/Exception;
        //  32     50     266    267    Any
        //  58     64     212    216    Ljava/lang/Exception;
        //  58     64     266    267    Any
        //  77     82     212    216    Ljava/lang/Exception;
        //  77     82     266    267    Any
        //  90     124    212    216    Ljava/lang/Exception;
        //  90     124    266    267    Any
        //  132    139    293    300    Ljava/lang/Exception;
        //  132    139    285    293    Any
        //  147    160    293    300    Ljava/lang/Exception;
        //  147    160    285    293    Any
        //  187    202    212    216    Ljava/lang/Exception;
        //  187    202    266    267    Any
        //  223    238    266    267    Any
        //  245    250    266    267    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0163:
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
    
    private void writeInitialPartsTo(final CodedOutputStream codedOutputStream, final String s) throws IOException {
        final String[] array = { "SessionUser", "SessionApp", "SessionOS", "SessionDevice" };
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s2 = array[i];
            final File[] listFilesMatching = this.listFilesMatching(new FileNameContainsFilter(s + s2));
            if (listFilesMatching.length == 0) {
                Fabric.getLogger().e("CrashlyticsCore", "Can't find " + s2 + " data for session ID " + s, null);
            }
            else {
                Fabric.getLogger().d("CrashlyticsCore", "Collecting " + s2 + " data for session ID " + s);
                this.writeToCosFromFile(codedOutputStream, listFilesMatching[0]);
            }
        }
    }
    
    private void writeNonFatalEventsTo(final CodedOutputStream codedOutputStream, final File[] array, final String s) {
        Arrays.sort(array, CommonUtils.FILE_MODIFIED_COMPARATOR);
        final int length = array.length;
        int i = 0;
    Label_0070_Outer:
        while (i < length) {
            final File file = array[i];
            while (true) {
                try {
                    Fabric.getLogger().d("CrashlyticsCore", String.format(Locale.US, "Found Non Fatal for session ID %s in %s ", s, file.getName()));
                    this.writeToCosFromFile(codedOutputStream, file);
                    ++i;
                    continue Label_0070_Outer;
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("CrashlyticsCore", "Error writting non-fatal to session.", ex);
                    continue;
                }
                break;
            }
            break;
        }
    }
    
    private void writeSessionApp(final String p0) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          4
        //     3: aconst_null    
        //     4: astore          6
        //     6: aconst_null    
        //     7: astore_3       
        //     8: aconst_null    
        //     9: astore          9
        //    11: aconst_null    
        //    12: astore          8
        //    14: aconst_null    
        //    15: astore          7
        //    17: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    20: dup            
        //    21: aload_0        
        //    22: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    25: new             Ljava/lang/StringBuilder;
        //    28: dup            
        //    29: invokespecial   java/lang/StringBuilder.<init>:()V
        //    32: aload_1        
        //    33: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    36: ldc_w           "SessionApp"
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    48: astore          5
        //    50: aload           9
        //    52: astore_1       
        //    53: aload           8
        //    55: astore_3       
        //    56: aload           5
        //    58: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //    61: astore          4
        //    63: aload           4
        //    65: astore_1       
        //    66: aload           4
        //    68: astore_3       
        //    69: aload_0        
        //    70: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    73: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getPackageName:()Ljava/lang/String;
        //    76: astore          6
        //    78: aload           4
        //    80: astore_1       
        //    81: aload           4
        //    83: astore_3       
        //    84: aload_0        
        //    85: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    88: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getVersionCode:()Ljava/lang/String;
        //    91: astore          7
        //    93: aload           4
        //    95: astore_1       
        //    96: aload           4
        //    98: astore_3       
        //    99: aload_0        
        //   100: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //   103: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getVersionName:()Ljava/lang/String;
        //   106: astore          8
        //   108: aload           4
        //   110: astore_1       
        //   111: aload           4
        //   113: astore_3       
        //   114: aload_0        
        //   115: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.idManager:Lio/fabric/sdk/android/services/common/IdManager;
        //   118: invokevirtual   io/fabric/sdk/android/services/common/IdManager.getAppInstallIdentifier:()Ljava/lang/String;
        //   121: astore          9
        //   123: aload           4
        //   125: astore_1       
        //   126: aload           4
        //   128: astore_3       
        //   129: aload_0        
        //   130: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //   133: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getInstallerPackageName:()Ljava/lang/String;
        //   136: invokestatic    io/fabric/sdk/android/services/common/DeliveryMechanism.determineFrom:(Ljava/lang/String;)Lio/fabric/sdk/android/services/common/DeliveryMechanism;
        //   139: invokevirtual   io/fabric/sdk/android/services/common/DeliveryMechanism.getId:()I
        //   142: istore_2       
        //   143: aload           4
        //   145: astore_1       
        //   146: aload           4
        //   148: astore_3       
        //   149: aload_0        
        //   150: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.sessionDataWriter:Lcom/crashlytics/android/core/SessionDataWriter;
        //   153: aload           4
        //   155: aload           6
        //   157: aload           7
        //   159: aload           8
        //   161: aload           9
        //   163: iload_2        
        //   164: invokevirtual   com/crashlytics/android/core/SessionDataWriter.writeSessionApp:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
        //   167: aload           4
        //   169: ldc_w           "Failed to flush to session app file."
        //   172: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   175: aload           5
        //   177: ldc_w           "Failed to close session app file."
        //   180: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   183: return         
        //   184: astore_1       
        //   185: aload           6
        //   187: astore          5
        //   189: aload_1        
        //   190: astore          6
        //   192: aload           7
        //   194: astore_1       
        //   195: aload_1        
        //   196: astore_3       
        //   197: aload           5
        //   199: astore          4
        //   201: aload           6
        //   203: aload           5
        //   205: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   208: aload_1        
        //   209: astore_3       
        //   210: aload           5
        //   212: astore          4
        //   214: aload           6
        //   216: athrow         
        //   217: astore          5
        //   219: aload_3        
        //   220: astore_1       
        //   221: aload           5
        //   223: astore_3       
        //   224: aload_1        
        //   225: ldc_w           "Failed to flush to session app file."
        //   228: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   231: aload           4
        //   233: ldc_w           "Failed to close session app file."
        //   236: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   239: aload_3        
        //   240: athrow         
        //   241: astore_3       
        //   242: aload           5
        //   244: astore          4
        //   246: goto            224
        //   249: astore          6
        //   251: aload_3        
        //   252: astore_1       
        //   253: goto            195
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     50     184    195    Ljava/lang/Exception;
        //  17     50     217    224    Any
        //  56     63     249    256    Ljava/lang/Exception;
        //  56     63     241    249    Any
        //  69     78     249    256    Ljava/lang/Exception;
        //  69     78     241    249    Any
        //  84     93     249    256    Ljava/lang/Exception;
        //  84     93     241    249    Any
        //  99     108    249    256    Ljava/lang/Exception;
        //  99     108    241    249    Any
        //  114    123    249    256    Ljava/lang/Exception;
        //  114    123    241    249    Any
        //  129    143    249    256    Ljava/lang/Exception;
        //  129    143    241    249    Any
        //  149    167    249    256    Ljava/lang/Exception;
        //  149    167    241    249    Any
        //  201    208    217    224    Any
        //  214    217    217    224    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3035)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    private void writeSessionDevice(final String p0) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          13
        //     3: aconst_null    
        //     4: astore          15
        //     6: aconst_null    
        //     7: astore          12
        //     9: aconst_null    
        //    10: astore          18
        //    12: aconst_null    
        //    13: astore          17
        //    15: aconst_null    
        //    16: astore          16
        //    18: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    21: dup            
        //    22: aload_0        
        //    23: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    26: new             Ljava/lang/StringBuilder;
        //    29: dup            
        //    30: invokespecial   java/lang/StringBuilder.<init>:()V
        //    33: aload_1        
        //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    37: ldc_w           "SessionDevice"
        //    40: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    43: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    46: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    49: astore          14
        //    51: aload           18
        //    53: astore_1       
        //    54: aload           17
        //    56: astore          12
        //    58: aload           14
        //    60: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //    63: astore          13
        //    65: aload           13
        //    67: astore_1       
        //    68: aload           13
        //    70: astore          12
        //    72: aload_0        
        //    73: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    76: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getContext:()Landroid/content/Context;
        //    79: astore          15
        //    81: aload           13
        //    83: astore_1       
        //    84: aload           13
        //    86: astore          12
        //    88: new             Landroid/os/StatFs;
        //    91: dup            
        //    92: invokestatic    android/os/Environment.getDataDirectory:()Ljava/io/File;
        //    95: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    98: invokespecial   android/os/StatFs.<init>:(Ljava/lang/String;)V
        //   101: astore          17
        //   103: aload           13
        //   105: astore_1       
        //   106: aload           13
        //   108: astore          12
        //   110: aload_0        
        //   111: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.idManager:Lio/fabric/sdk/android/services/common/IdManager;
        //   114: invokevirtual   io/fabric/sdk/android/services/common/IdManager.getDeviceUUID:()Ljava/lang/String;
        //   117: astore          16
        //   119: aload           13
        //   121: astore_1       
        //   122: aload           13
        //   124: astore          12
        //   126: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.getCpuArchitectureInt:()I
        //   129: istore_2       
        //   130: aload           13
        //   132: astore_1       
        //   133: aload           13
        //   135: astore          12
        //   137: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //   140: invokevirtual   java/lang/Runtime.availableProcessors:()I
        //   143: istore_3       
        //   144: aload           13
        //   146: astore_1       
        //   147: aload           13
        //   149: astore          12
        //   151: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.getTotalRamInBytes:()J
        //   154: lstore          5
        //   156: aload           13
        //   158: astore_1       
        //   159: aload           13
        //   161: astore          12
        //   163: aload           17
        //   165: invokevirtual   android/os/StatFs.getBlockCount:()I
        //   168: i2l            
        //   169: lstore          7
        //   171: aload           13
        //   173: astore_1       
        //   174: aload           13
        //   176: astore          12
        //   178: aload           17
        //   180: invokevirtual   android/os/StatFs.getBlockSize:()I
        //   183: i2l            
        //   184: lstore          9
        //   186: aload           13
        //   188: astore_1       
        //   189: aload           13
        //   191: astore          12
        //   193: aload           15
        //   195: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.isEmulator:(Landroid/content/Context;)Z
        //   198: istore          11
        //   200: aload           13
        //   202: astore_1       
        //   203: aload           13
        //   205: astore          12
        //   207: aload_0        
        //   208: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.idManager:Lio/fabric/sdk/android/services/common/IdManager;
        //   211: invokevirtual   io/fabric/sdk/android/services/common/IdManager.getDeviceIdentifiers:()Ljava/util/Map;
        //   214: astore          17
        //   216: aload           13
        //   218: astore_1       
        //   219: aload           13
        //   221: astore          12
        //   223: aload           15
        //   225: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.getDeviceState:(Landroid/content/Context;)I
        //   228: istore          4
        //   230: aload           13
        //   232: astore_1       
        //   233: aload           13
        //   235: astore          12
        //   237: aload_0        
        //   238: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.sessionDataWriter:Lcom/crashlytics/android/core/SessionDataWriter;
        //   241: aload           13
        //   243: aload           16
        //   245: iload_2        
        //   246: getstatic       android/os/Build.MODEL:Ljava/lang/String;
        //   249: iload_3        
        //   250: lload           5
        //   252: lload           7
        //   254: lload           9
        //   256: lmul           
        //   257: iload           11
        //   259: aload           17
        //   261: iload           4
        //   263: getstatic       android/os/Build.MANUFACTURER:Ljava/lang/String;
        //   266: getstatic       android/os/Build.PRODUCT:Ljava/lang/String;
        //   269: invokevirtual   com/crashlytics/android/core/SessionDataWriter.writeSessionDevice:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/lang/String;ILjava/lang/String;IJJZLjava/util/Map;ILjava/lang/String;Ljava/lang/String;)V
        //   272: aload           13
        //   274: ldc_w           "Failed to flush session device info."
        //   277: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   280: aload           14
        //   282: ldc_w           "Failed to close session device file."
        //   285: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   288: return         
        //   289: astore_1       
        //   290: aload           15
        //   292: astore          14
        //   294: aload_1        
        //   295: astore          15
        //   297: aload           16
        //   299: astore_1       
        //   300: aload_1        
        //   301: astore          12
        //   303: aload           14
        //   305: astore          13
        //   307: aload           15
        //   309: aload           14
        //   311: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   314: aload_1        
        //   315: astore          12
        //   317: aload           14
        //   319: astore          13
        //   321: aload           15
        //   323: athrow         
        //   324: astore          14
        //   326: aload           12
        //   328: astore_1       
        //   329: aload           14
        //   331: astore          12
        //   333: aload_1        
        //   334: ldc_w           "Failed to flush session device info."
        //   337: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   340: aload           13
        //   342: ldc_w           "Failed to close session device file."
        //   345: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   348: aload           12
        //   350: athrow         
        //   351: astore          12
        //   353: aload           14
        //   355: astore          13
        //   357: goto            333
        //   360: astore          15
        //   362: aload           12
        //   364: astore_1       
        //   365: goto            300
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  18     51     289    300    Ljava/lang/Exception;
        //  18     51     324    333    Any
        //  58     65     360    368    Ljava/lang/Exception;
        //  58     65     351    360    Any
        //  72     81     360    368    Ljava/lang/Exception;
        //  72     81     351    360    Any
        //  88     103    360    368    Ljava/lang/Exception;
        //  88     103    351    360    Any
        //  110    119    360    368    Ljava/lang/Exception;
        //  110    119    351    360    Any
        //  126    130    360    368    Ljava/lang/Exception;
        //  126    130    351    360    Any
        //  137    144    360    368    Ljava/lang/Exception;
        //  137    144    351    360    Any
        //  151    156    360    368    Ljava/lang/Exception;
        //  151    156    351    360    Any
        //  163    171    360    368    Ljava/lang/Exception;
        //  163    171    351    360    Any
        //  178    186    360    368    Ljava/lang/Exception;
        //  178    186    351    360    Any
        //  193    200    360    368    Ljava/lang/Exception;
        //  193    200    351    360    Any
        //  207    216    360    368    Ljava/lang/Exception;
        //  207    216    351    360    Any
        //  223    230    360    368    Ljava/lang/Exception;
        //  223    230    351    360    Any
        //  237    272    360    368    Ljava/lang/Exception;
        //  237    272    351    360    Any
        //  307    314    324    333    Any
        //  321    324    324    333    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3035)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    private void writeSessionEvent(final CodedOutputStream codedOutputStream, final Date date, final Thread thread, final Throwable t, final String s, final boolean b) throws Exception {
        final Context context = this.crashlyticsCore.getContext();
        final long n = date.getTime() / 1000L;
        final float batteryLevel = CommonUtils.getBatteryLevel(context);
        final int batteryVelocity = CommonUtils.getBatteryVelocity(context, this.powerConnected);
        final boolean proximitySensorEnabled = CommonUtils.getProximitySensorEnabled(context);
        final int orientation = context.getResources().getConfiguration().orientation;
        final long totalRamInBytes = CommonUtils.getTotalRamInBytes();
        final long calculateFreeRamInBytes = CommonUtils.calculateFreeRamInBytes(context);
        final long calculateUsedDiskSpaceInBytes = CommonUtils.calculateUsedDiskSpaceInBytes(Environment.getDataDirectory().getPath());
        final ActivityManager$RunningAppProcessInfo appProcessInfo = CommonUtils.getAppProcessInfo(context.getPackageName(), context);
        final LinkedList<StackTraceElement[]> list = new LinkedList<StackTraceElement[]>();
        final StackTraceElement[] stackTrace = t.getStackTrace();
        Thread[] array2;
        if (b) {
            final Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
            final Thread[] array = new Thread[allStackTraces.size()];
            int n2 = 0;
            final Iterator<Map.Entry<Thread, StackTraceElement[]>> iterator = allStackTraces.entrySet().iterator();
            while (true) {
                array2 = array;
                if (!iterator.hasNext()) {
                    break;
                }
                final Map.Entry<Thread, StackTraceElement[]> entry = iterator.next();
                array[n2] = entry.getKey();
                list.add((V)(Object)entry.getValue());
                ++n2;
            }
        }
        else {
            array2 = new Thread[0];
        }
        Map<String, String> map;
        if (!CommonUtils.getBooleanResourceValue(context, "com.crashlytics.CollectCustomKeys", true)) {
            map = new TreeMap<String, String>();
        }
        else {
            final Map<String, String> attributes = this.crashlyticsCore.getAttributes();
            if ((map = attributes) != null) {
                map = attributes;
                if (attributes.size() > 1) {
                    map = new TreeMap<String, String>(attributes);
                }
            }
        }
        this.sessionDataWriter.writeSessionEvent(codedOutputStream, n, thread, t, s, array2, batteryLevel, batteryVelocity, proximitySensorEnabled, orientation, totalRamInBytes - calculateFreeRamInBytes, calculateUsedDiskSpaceInBytes, appProcessInfo, list, stackTrace, this.logFileManager, map);
    }
    
    private void writeSessionOS(final String p0) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          4
        //     3: aconst_null    
        //     4: astore          6
        //     6: aconst_null    
        //     7: astore_3       
        //     8: aconst_null    
        //     9: astore          9
        //    11: aconst_null    
        //    12: astore          8
        //    14: aconst_null    
        //    15: astore          7
        //    17: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    20: dup            
        //    21: aload_0        
        //    22: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    25: new             Ljava/lang/StringBuilder;
        //    28: dup            
        //    29: invokespecial   java/lang/StringBuilder.<init>:()V
        //    32: aload_1        
        //    33: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    36: ldc_w           "SessionOS"
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    48: astore          5
        //    50: aload           9
        //    52: astore_1       
        //    53: aload           8
        //    55: astore_3       
        //    56: aload           5
        //    58: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //    61: astore          4
        //    63: aload           4
        //    65: astore_1       
        //    66: aload           4
        //    68: astore_3       
        //    69: aload_0        
        //    70: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    73: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getContext:()Landroid/content/Context;
        //    76: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.isRooted:(Landroid/content/Context;)Z
        //    79: istore_2       
        //    80: aload           4
        //    82: astore_1       
        //    83: aload           4
        //    85: astore_3       
        //    86: aload_0        
        //    87: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.sessionDataWriter:Lcom/crashlytics/android/core/SessionDataWriter;
        //    90: aload           4
        //    92: iload_2        
        //    93: invokevirtual   com/crashlytics/android/core/SessionDataWriter.writeSessionOS:(Lcom/crashlytics/android/core/CodedOutputStream;Z)V
        //    96: aload           4
        //    98: ldc_w           "Failed to flush to session OS file."
        //   101: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   104: aload           5
        //   106: ldc_w           "Failed to close session OS file."
        //   109: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   112: return         
        //   113: astore_1       
        //   114: aload           6
        //   116: astore          5
        //   118: aload_1        
        //   119: astore          6
        //   121: aload           7
        //   123: astore_1       
        //   124: aload_1        
        //   125: astore_3       
        //   126: aload           5
        //   128: astore          4
        //   130: aload           6
        //   132: aload           5
        //   134: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   137: aload_1        
        //   138: astore_3       
        //   139: aload           5
        //   141: astore          4
        //   143: aload           6
        //   145: athrow         
        //   146: astore          5
        //   148: aload_3        
        //   149: astore_1       
        //   150: aload_1        
        //   151: ldc_w           "Failed to flush to session OS file."
        //   154: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   157: aload           4
        //   159: ldc_w           "Failed to close session OS file."
        //   162: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   165: aload           5
        //   167: athrow         
        //   168: astore_3       
        //   169: aload           5
        //   171: astore          4
        //   173: aload_3        
        //   174: astore          5
        //   176: goto            150
        //   179: astore          6
        //   181: aload_3        
        //   182: astore_1       
        //   183: goto            124
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     50     113    124    Ljava/lang/Exception;
        //  17     50     146    150    Any
        //  56     63     179    186    Ljava/lang/Exception;
        //  56     63     168    179    Any
        //  69     80     179    186    Ljava/lang/Exception;
        //  69     80     168    179    Any
        //  86     96     179    186    Ljava/lang/Exception;
        //  86     96     168    179    Any
        //  130    137    146    150    Any
        //  143    146    146    150    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3035)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    private void writeSessionPartsToSessionFile(final File p0, final String p1, final int p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //     3: ldc_w           "CrashlyticsCore"
        //     6: new             Ljava/lang/StringBuilder;
        //     9: dup            
        //    10: invokespecial   java/lang/StringBuilder.<init>:()V
        //    13: ldc_w           "Collecting session parts for ID "
        //    16: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    19: aload_2        
        //    20: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    23: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    26: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    31: aload_0        
        //    32: new             Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter;
        //    35: dup            
        //    36: new             Ljava/lang/StringBuilder;
        //    39: dup            
        //    40: invokespecial   java/lang/StringBuilder.<init>:()V
        //    43: aload_2        
        //    44: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    47: ldc_w           "SessionCrash"
        //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    53: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    56: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter.<init>:(Ljava/lang/String;)V
        //    59: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.listFilesMatching:(Ljava/io/FilenameFilter;)[Ljava/io/File;
        //    62: astore          14
        //    64: aload           14
        //    66: ifnull          609
        //    69: aload           14
        //    71: arraylength    
        //    72: ifle            609
        //    75: iconst_1       
        //    76: istore          4
        //    78: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    81: ldc_w           "CrashlyticsCore"
        //    84: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //    87: ldc_w           "Session %s has fatal exception: %s"
        //    90: iconst_2       
        //    91: anewarray       Ljava/lang/Object;
        //    94: dup            
        //    95: iconst_0       
        //    96: aload_2        
        //    97: aastore        
        //    98: dup            
        //    99: iconst_1       
        //   100: iload           4
        //   102: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   105: aastore        
        //   106: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   109: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   114: aload_0        
        //   115: new             Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter;
        //   118: dup            
        //   119: new             Ljava/lang/StringBuilder;
        //   122: dup            
        //   123: invokespecial   java/lang/StringBuilder.<init>:()V
        //   126: aload_2        
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: ldc_w           "SessionEvent"
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   139: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter.<init>:(Ljava/lang/String;)V
        //   142: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.listFilesMatching:(Ljava/io/FilenameFilter;)[Ljava/io/File;
        //   145: astore          10
        //   147: aload           10
        //   149: ifnull          615
        //   152: aload           10
        //   154: arraylength    
        //   155: ifle            615
        //   158: iconst_1       
        //   159: istore          5
        //   161: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   164: ldc_w           "CrashlyticsCore"
        //   167: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   170: ldc_w           "Session %s has non-fatal exceptions: %s"
        //   173: iconst_2       
        //   174: anewarray       Ljava/lang/Object;
        //   177: dup            
        //   178: iconst_0       
        //   179: aload_2        
        //   180: aastore        
        //   181: dup            
        //   182: iconst_1       
        //   183: iload           5
        //   185: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   188: aastore        
        //   189: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   192: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   197: iload           4
        //   199: ifne            207
        //   202: iload           5
        //   204: ifeq            760
        //   207: aconst_null    
        //   208: astore          7
        //   210: aconst_null    
        //   211: astore          9
        //   213: aconst_null    
        //   214: astore          6
        //   216: aconst_null    
        //   217: astore          13
        //   219: aconst_null    
        //   220: astore          12
        //   222: aconst_null    
        //   223: astore          11
        //   225: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //   228: dup            
        //   229: aload_0        
        //   230: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //   233: aload_2        
        //   234: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   237: astore          8
        //   239: aload           13
        //   241: astore          6
        //   243: aload           12
        //   245: astore          7
        //   247: aload           8
        //   249: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //   252: astore          9
        //   254: aload           9
        //   256: astore          6
        //   258: aload           9
        //   260: astore          7
        //   262: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   265: ldc_w           "CrashlyticsCore"
        //   268: new             Ljava/lang/StringBuilder;
        //   271: dup            
        //   272: invokespecial   java/lang/StringBuilder.<init>:()V
        //   275: ldc_w           "Collecting SessionStart data for session ID "
        //   278: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   281: aload_2        
        //   282: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   285: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   288: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   293: aload           9
        //   295: astore          6
        //   297: aload           9
        //   299: astore          7
        //   301: aload_0        
        //   302: aload           9
        //   304: aload_1        
        //   305: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.writeToCosFromFile:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/io/File;)V
        //   308: aload           9
        //   310: astore          6
        //   312: aload           9
        //   314: astore          7
        //   316: aload           9
        //   318: iconst_4       
        //   319: new             Ljava/util/Date;
        //   322: dup            
        //   323: invokespecial   java/util/Date.<init>:()V
        //   326: invokevirtual   java/util/Date.getTime:()J
        //   329: ldc2_w          1000
        //   332: ldiv           
        //   333: invokevirtual   com/crashlytics/android/core/CodedOutputStream.writeUInt64:(IJ)V
        //   336: aload           9
        //   338: astore          6
        //   340: aload           9
        //   342: astore          7
        //   344: aload           9
        //   346: iconst_5       
        //   347: iload           4
        //   349: invokevirtual   com/crashlytics/android/core/CodedOutputStream.writeBool:(IZ)V
        //   352: aload           9
        //   354: astore          6
        //   356: aload           9
        //   358: astore          7
        //   360: aload_0        
        //   361: aload           9
        //   363: aload_2        
        //   364: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.writeInitialPartsTo:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/lang/String;)V
        //   367: iload           5
        //   369: ifeq            499
        //   372: aload           10
        //   374: astore_1       
        //   375: aload           9
        //   377: astore          6
        //   379: aload           9
        //   381: astore          7
        //   383: aload           10
        //   385: arraylength    
        //   386: iload_3        
        //   387: if_icmple       483
        //   390: aload           9
        //   392: astore          6
        //   394: aload           9
        //   396: astore          7
        //   398: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   401: ldc_w           "CrashlyticsCore"
        //   404: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   407: ldc_w           "Trimming down to %d logged exceptions."
        //   410: iconst_1       
        //   411: anewarray       Ljava/lang/Object;
        //   414: dup            
        //   415: iconst_0       
        //   416: iload_3        
        //   417: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   420: aastore        
        //   421: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   424: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   429: aload           9
        //   431: astore          6
        //   433: aload           9
        //   435: astore          7
        //   437: aload_0        
        //   438: aload_2        
        //   439: iload_3        
        //   440: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.trimSessionEventFiles:(Ljava/lang/String;I)V
        //   443: aload           9
        //   445: astore          6
        //   447: aload           9
        //   449: astore          7
        //   451: aload_0        
        //   452: new             Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter;
        //   455: dup            
        //   456: new             Ljava/lang/StringBuilder;
        //   459: dup            
        //   460: invokespecial   java/lang/StringBuilder.<init>:()V
        //   463: aload_2        
        //   464: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   467: ldc_w           "SessionEvent"
        //   470: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   473: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   476: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$FileNameContainsFilter.<init>:(Ljava/lang/String;)V
        //   479: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.listFilesMatching:(Ljava/io/FilenameFilter;)[Ljava/io/File;
        //   482: astore_1       
        //   483: aload           9
        //   485: astore          6
        //   487: aload           9
        //   489: astore          7
        //   491: aload_0        
        //   492: aload           9
        //   494: aload_1        
        //   495: aload_2        
        //   496: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.writeNonFatalEventsTo:(Lcom/crashlytics/android/core/CodedOutputStream;[Ljava/io/File;Ljava/lang/String;)V
        //   499: iload           4
        //   501: ifeq            522
        //   504: aload           9
        //   506: astore          6
        //   508: aload           9
        //   510: astore          7
        //   512: aload_0        
        //   513: aload           9
        //   515: aload           14
        //   517: iconst_0       
        //   518: aaload         
        //   519: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.writeToCosFromFile:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/io/File;)V
        //   522: aload           9
        //   524: astore          6
        //   526: aload           9
        //   528: astore          7
        //   530: aload           9
        //   532: bipush          11
        //   534: iconst_1       
        //   535: invokevirtual   com/crashlytics/android/core/CodedOutputStream.writeUInt32:(II)V
        //   538: aload           9
        //   540: astore          6
        //   542: aload           9
        //   544: astore          7
        //   546: aload           9
        //   548: bipush          12
        //   550: iconst_3       
        //   551: invokevirtual   com/crashlytics/android/core/CodedOutputStream.writeEnum:(II)V
        //   554: aload           9
        //   556: ldc_w           "Error flushing session file stream"
        //   559: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   562: iconst_0       
        //   563: ifeq            621
        //   566: aload_0        
        //   567: aload           8
        //   569: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.closeWithoutRenamingOrLog:(Lcom/crashlytics/android/core/ClsFileOutputStream;)V
        //   572: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   575: ldc_w           "CrashlyticsCore"
        //   578: new             Ljava/lang/StringBuilder;
        //   581: dup            
        //   582: invokespecial   java/lang/StringBuilder.<init>:()V
        //   585: ldc_w           "Removing session part files for ID "
        //   588: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   591: aload_2        
        //   592: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   595: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   598: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   603: aload_0        
        //   604: aload_2        
        //   605: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.deleteSessionPartFilesFor:(Ljava/lang/String;)V
        //   608: return         
        //   609: iconst_0       
        //   610: istore          4
        //   612: goto            78
        //   615: iconst_0       
        //   616: istore          5
        //   618: goto            161
        //   621: aload           8
        //   623: ldc_w           "Failed to close CLS file"
        //   626: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   629: goto            572
        //   632: astore_1       
        //   633: aload           9
        //   635: astore          8
        //   637: aload_1        
        //   638: astore          9
        //   640: aload           11
        //   642: astore_1       
        //   643: aload_1        
        //   644: astore          6
        //   646: aload           8
        //   648: astore          7
        //   650: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   653: ldc_w           "CrashlyticsCore"
        //   656: new             Ljava/lang/StringBuilder;
        //   659: dup            
        //   660: invokespecial   java/lang/StringBuilder.<init>:()V
        //   663: ldc_w           "Failed to write session file for session ID: "
        //   666: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   669: aload_2        
        //   670: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   673: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   676: aload           9
        //   678: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   683: aload_1        
        //   684: astore          6
        //   686: aload           8
        //   688: astore          7
        //   690: aload           9
        //   692: aload           8
        //   694: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   697: aload_1        
        //   698: ldc_w           "Error flushing session file stream"
        //   701: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   704: iconst_1       
        //   705: ifeq            717
        //   708: aload_0        
        //   709: aload           8
        //   711: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.closeWithoutRenamingOrLog:(Lcom/crashlytics/android/core/ClsFileOutputStream;)V
        //   714: goto            572
        //   717: aload           8
        //   719: ldc_w           "Failed to close CLS file"
        //   722: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   725: goto            572
        //   728: astore_1       
        //   729: aload           6
        //   731: ldc_w           "Error flushing session file stream"
        //   734: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   737: iconst_0       
        //   738: ifeq            749
        //   741: aload_0        
        //   742: aload           7
        //   744: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.closeWithoutRenamingOrLog:(Lcom/crashlytics/android/core/ClsFileOutputStream;)V
        //   747: aload_1        
        //   748: athrow         
        //   749: aload           7
        //   751: ldc_w           "Failed to close CLS file"
        //   754: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   757: goto            747
        //   760: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   763: ldc_w           "CrashlyticsCore"
        //   766: new             Ljava/lang/StringBuilder;
        //   769: dup            
        //   770: invokespecial   java/lang/StringBuilder.<init>:()V
        //   773: ldc_w           "No events present for session ID "
        //   776: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   779: aload_2        
        //   780: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   783: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   786: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   791: goto            572
        //   794: astore_1       
        //   795: aload           8
        //   797: astore          7
        //   799: goto            729
        //   802: astore          9
        //   804: aload           7
        //   806: astore_1       
        //   807: goto            643
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  225    239    632    643    Ljava/lang/Exception;
        //  225    239    728    729    Any
        //  247    254    802    810    Ljava/lang/Exception;
        //  247    254    794    802    Any
        //  262    293    802    810    Ljava/lang/Exception;
        //  262    293    794    802    Any
        //  301    308    802    810    Ljava/lang/Exception;
        //  301    308    794    802    Any
        //  316    336    802    810    Ljava/lang/Exception;
        //  316    336    794    802    Any
        //  344    352    802    810    Ljava/lang/Exception;
        //  344    352    794    802    Any
        //  360    367    802    810    Ljava/lang/Exception;
        //  360    367    794    802    Any
        //  383    390    802    810    Ljava/lang/Exception;
        //  383    390    794    802    Any
        //  398    429    802    810    Ljava/lang/Exception;
        //  398    429    794    802    Any
        //  437    443    802    810    Ljava/lang/Exception;
        //  437    443    794    802    Any
        //  451    483    802    810    Ljava/lang/Exception;
        //  451    483    794    802    Any
        //  491    499    802    810    Ljava/lang/Exception;
        //  491    499    794    802    Any
        //  512    522    802    810    Ljava/lang/Exception;
        //  512    522    794    802    Any
        //  530    538    802    810    Ljava/lang/Exception;
        //  530    538    794    802    Any
        //  546    554    802    810    Ljava/lang/Exception;
        //  546    554    794    802    Any
        //  650    683    728    729    Any
        //  690    697    728    729    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0483:
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
    
    private void writeSessionUser(final String p0) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          4
        //     3: aconst_null    
        //     4: astore          6
        //     6: aconst_null    
        //     7: astore_3       
        //     8: aconst_null    
        //     9: astore          9
        //    11: aconst_null    
        //    12: astore          8
        //    14: aconst_null    
        //    15: astore          7
        //    17: new             Lcom/crashlytics/android/core/ClsFileOutputStream;
        //    20: dup            
        //    21: aload_0        
        //    22: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.filesDir:Ljava/io/File;
        //    25: new             Ljava/lang/StringBuilder;
        //    28: dup            
        //    29: invokespecial   java/lang/StringBuilder.<init>:()V
        //    32: aload_1        
        //    33: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    36: ldc_w           "SessionUser"
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: invokespecial   com/crashlytics/android/core/ClsFileOutputStream.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    48: astore          5
        //    50: aload           9
        //    52: astore_3       
        //    53: aload           8
        //    55: astore          4
        //    57: aload           5
        //    59: invokestatic    com/crashlytics/android/core/CodedOutputStream.newInstance:(Ljava/io/OutputStream;)Lcom/crashlytics/android/core/CodedOutputStream;
        //    62: astore          6
        //    64: aload           6
        //    66: astore_3       
        //    67: aload           6
        //    69: astore          4
        //    71: aload_0        
        //    72: aload_1        
        //    73: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.getUserMetaData:(Ljava/lang/String;)Lcom/crashlytics/android/core/UserMetaData;
        //    76: astore_1       
        //    77: aload           6
        //    79: astore_3       
        //    80: aload           6
        //    82: astore          4
        //    84: aload_1        
        //    85: invokevirtual   com/crashlytics/android/core/UserMetaData.isEmpty:()Z
        //    88: istore_2       
        //    89: iload_2        
        //    90: ifeq            110
        //    93: aload           6
        //    95: ldc_w           "Failed to flush session user file."
        //    98: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   101: aload           5
        //   103: ldc_w           "Failed to close session user file."
        //   106: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   109: return         
        //   110: aload           6
        //   112: astore_3       
        //   113: aload           6
        //   115: astore          4
        //   117: aload_0        
        //   118: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.sessionDataWriter:Lcom/crashlytics/android/core/SessionDataWriter;
        //   121: aload           6
        //   123: aload_1        
        //   124: getfield        com/crashlytics/android/core/UserMetaData.id:Ljava/lang/String;
        //   127: aload_1        
        //   128: getfield        com/crashlytics/android/core/UserMetaData.name:Ljava/lang/String;
        //   131: aload_1        
        //   132: getfield        com/crashlytics/android/core/UserMetaData.email:Ljava/lang/String;
        //   135: invokevirtual   com/crashlytics/android/core/SessionDataWriter.writeSessionUser:(Lcom/crashlytics/android/core/CodedOutputStream;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   138: aload           6
        //   140: ldc_w           "Failed to flush session user file."
        //   143: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   146: aload           5
        //   148: ldc_w           "Failed to close session user file."
        //   151: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   154: return         
        //   155: astore_1       
        //   156: aload           6
        //   158: astore          5
        //   160: aload_1        
        //   161: astore          6
        //   163: aload           7
        //   165: astore_1       
        //   166: aload_1        
        //   167: astore_3       
        //   168: aload           5
        //   170: astore          4
        //   172: aload           6
        //   174: aload           5
        //   176: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTraceIfNotNull:(Ljava/lang/Throwable;Ljava/io/OutputStream;)V
        //   179: aload_1        
        //   180: astore_3       
        //   181: aload           5
        //   183: astore          4
        //   185: aload           6
        //   187: athrow         
        //   188: astore_1       
        //   189: aload_3        
        //   190: ldc_w           "Failed to flush session user file."
        //   193: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.flushOrLog:(Ljava/io/Flushable;Ljava/lang/String;)V
        //   196: aload           4
        //   198: ldc_w           "Failed to close session user file."
        //   201: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //   204: aload_1        
        //   205: athrow         
        //   206: astore_1       
        //   207: aload           5
        //   209: astore          4
        //   211: goto            189
        //   214: astore          6
        //   216: aload           4
        //   218: astore_1       
        //   219: goto            166
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     50     155    166    Ljava/lang/Exception;
        //  17     50     188    189    Any
        //  57     64     214    222    Ljava/lang/Exception;
        //  57     64     206    214    Any
        //  71     77     214    222    Ljava/lang/Exception;
        //  71     77     206    214    Any
        //  84     89     214    222    Ljava/lang/Exception;
        //  84     89     206    214    Any
        //  117    138    214    222    Ljava/lang/Exception;
        //  117    138    206    214    Any
        //  172    179    188    189    Any
        //  185    188    188    189    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0110:
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
    
    private void writeToCosFromFile(final CodedOutputStream p0, final File p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_2        
        //     1: invokevirtual   java/io/File.exists:()Z
        //     4: ifeq            91
        //     7: aload_2        
        //     8: invokevirtual   java/io/File.length:()J
        //    11: l2i            
        //    12: newarray        B
        //    14: astore          6
        //    16: aconst_null    
        //    17: astore          5
        //    19: new             Ljava/io/FileInputStream;
        //    22: dup            
        //    23: aload_2        
        //    24: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    27: astore_2       
        //    28: iconst_0       
        //    29: istore_3       
        //    30: iload_3        
        //    31: aload           6
        //    33: arraylength    
        //    34: if_icmpge       64
        //    37: aload_2        
        //    38: aload           6
        //    40: iload_3        
        //    41: aload           6
        //    43: arraylength    
        //    44: iload_3        
        //    45: isub           
        //    46: invokevirtual   java/io/FileInputStream.read:([BII)I
        //    49: istore          4
        //    51: iload           4
        //    53: iflt            64
        //    56: iload_3        
        //    57: iload           4
        //    59: iadd           
        //    60: istore_3       
        //    61: goto            30
        //    64: aload_2        
        //    65: ldc_w           "Failed to close file input stream."
        //    68: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    71: aload_1        
        //    72: aload           6
        //    74: invokevirtual   com/crashlytics/android/core/CodedOutputStream.writeRawBytes:([B)V
        //    77: return         
        //    78: astore_2       
        //    79: aload           5
        //    81: astore_1       
        //    82: aload_1        
        //    83: ldc_w           "Failed to close file input stream."
        //    86: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    89: aload_2        
        //    90: athrow         
        //    91: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    94: ldc_w           "CrashlyticsCore"
        //    97: new             Ljava/lang/StringBuilder;
        //   100: dup            
        //   101: invokespecial   java/lang/StringBuilder.<init>:()V
        //   104: ldc_w           "Tried to include a file that doesn't exist: "
        //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   110: aload_2        
        //   111: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   114: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   117: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   120: aconst_null    
        //   121: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   126: return         
        //   127: astore          5
        //   129: aload_2        
        //   130: astore_1       
        //   131: aload           5
        //   133: astore_2       
        //   134: goto            82
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  19     28     78     82     Any
        //  30     51     127    137    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0030:
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
    
    void cacheKeyData(final Map<String, String> map) {
        this.executorServiceWrapper.executeAsync((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsUncaughtExceptionHandler.this.filesDir).writeKeyData(CrashlyticsUncaughtExceptionHandler.this.getCurrentSessionId(), map);
                return null;
            }
        });
    }
    
    void cleanInvalidTempFiles() {
        this.executorServiceWrapper.executeAsync(new Runnable() {
            @Override
            public void run() {
                CrashlyticsUncaughtExceptionHandler.this.doCleanInvalidTempFiles(CrashlyticsUncaughtExceptionHandler.this.listFilesMatching(ClsFileOutputStream.TEMP_FILENAME_FILTER));
            }
        });
    }
    
    void doCleanInvalidTempFiles(final File[] array) {
        this.deleteLegacyInvalidCacheDir();
        for (int length = array.length, i = 0; i < length; ++i) {
            final File file = array[i];
            Fabric.getLogger().d("CrashlyticsCore", "Found invalid session part file: " + file);
            final String sessionIdFromSessionFile = this.getSessionIdFromSessionFile(file);
            final FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(final File file, final String s) {
                    return s.startsWith(sessionIdFromSessionFile);
                }
            };
            Fabric.getLogger().d("CrashlyticsCore", "Deleting all part files for invalid session: " + sessionIdFromSessionFile);
            final File[] listFilesMatching = this.listFilesMatching(filenameFilter);
            for (int length2 = listFilesMatching.length, j = 0; j < length2; ++j) {
                final File file2 = listFilesMatching[j];
                Fabric.getLogger().d("CrashlyticsCore", "Deleting session file: " + file2);
                file2.delete();
            }
        }
    }
    
    void doCloseSessions() throws Exception {
        this.doCloseSessions(false);
    }
    
    boolean finalizeSessions() {
        return this.executorServiceWrapper.executeSyncLoggingException((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    Fabric.getLogger().d("CrashlyticsCore", "Finalizing previously open sessions.");
                    final SessionEventData externalCrashEventData = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getExternalCrashEventData();
                    if (externalCrashEventData != null) {
                        CrashlyticsUncaughtExceptionHandler.this.writeExternalCrashEvent(externalCrashEventData);
                    }
                    CrashlyticsUncaughtExceptionHandler.this.doCloseSessions(true);
                    Fabric.getLogger().d("CrashlyticsCore", "Closed all previously open sessions");
                    return true;
                }
                Fabric.getLogger().d("CrashlyticsCore", "Skipping session finalization because a crash has already occurred.");
                return false;
            }
        });
    }
    
    boolean isHandlingException() {
        return this.isHandlingException.get();
    }
    
    File[] listSessionBeginFiles() {
        return this.listFilesMatching(new FileNameContainsFilter("BeginSession"));
    }
    
    void openSession() {
        this.executorServiceWrapper.executeAsync((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                CrashlyticsUncaughtExceptionHandler.this.doOpenSession();
                return null;
            }
        });
    }
    
    void trimSessionFiles() {
        Utils.capFileCount(this.filesDir, CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER, 4, CrashlyticsUncaughtExceptionHandler.SMALLEST_FILE_NAME_FIRST);
    }
    
    @Override
    public void uncaughtException(final Thread p0, final Throwable p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.isHandlingException:Ljava/util/concurrent/atomic/AtomicBoolean;
        //     6: iconst_1       
        //     7: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.set:(Z)V
        //    10: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    13: ldc_w           "CrashlyticsCore"
        //    16: new             Ljava/lang/StringBuilder;
        //    19: dup            
        //    20: invokespecial   java/lang/StringBuilder.<init>:()V
        //    23: ldc_w           "Crashlytics is handling uncaught exception \""
        //    26: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    29: aload_2        
        //    30: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    33: ldc_w           "\" from thread "
        //    36: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    39: aload_1        
        //    40: invokevirtual   java/lang/Thread.getName:()Ljava/lang/String;
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    49: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    54: aload_0        
        //    55: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.receiversRegistered:Ljava/util/concurrent/atomic/AtomicBoolean;
        //    58: iconst_1       
        //    59: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.getAndSet:(Z)Z
        //    62: ifne            103
        //    65: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    68: ldc_w           "CrashlyticsCore"
        //    71: ldc_w           "Unregistering power receivers."
        //    74: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    79: aload_0        
        //    80: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.crashlyticsCore:Lcom/crashlytics/android/core/CrashlyticsCore;
        //    83: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getContext:()Landroid/content/Context;
        //    86: astore_3       
        //    87: aload_3        
        //    88: aload_0        
        //    89: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.powerConnectedReceiver:Landroid/content/BroadcastReceiver;
        //    92: invokevirtual   android/content/Context.unregisterReceiver:(Landroid/content/BroadcastReceiver;)V
        //    95: aload_3        
        //    96: aload_0        
        //    97: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.powerDisconnectedReceiver:Landroid/content/BroadcastReceiver;
        //   100: invokevirtual   android/content/Context.unregisterReceiver:(Landroid/content/BroadcastReceiver;)V
        //   103: new             Ljava/util/Date;
        //   106: dup            
        //   107: invokespecial   java/util/Date.<init>:()V
        //   110: astore_3       
        //   111: aload_0        
        //   112: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.executorServiceWrapper:Lcom/crashlytics/android/core/CrashlyticsExecutorServiceWrapper;
        //   115: new             Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$7;
        //   118: dup            
        //   119: aload_0        
        //   120: aload_3        
        //   121: aload_1        
        //   122: aload_2        
        //   123: invokespecial   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler$7.<init>:(Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler;Ljava/util/Date;Ljava/lang/Thread;Ljava/lang/Throwable;)V
        //   126: invokevirtual   com/crashlytics/android/core/CrashlyticsExecutorServiceWrapper.executeSyncLoggingException:(Ljava/util/concurrent/Callable;)Ljava/lang/Object;
        //   129: pop            
        //   130: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   133: ldc_w           "CrashlyticsCore"
        //   136: ldc_w           "Crashlytics completed exception processing. Invoking default exception handler."
        //   139: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   144: aload_0        
        //   145: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.defaultHandler:Ljava/lang/Thread$UncaughtExceptionHandler;
        //   148: aload_1        
        //   149: aload_2        
        //   150: invokeinterface java/lang/Thread$UncaughtExceptionHandler.uncaughtException:(Ljava/lang/Thread;Ljava/lang/Throwable;)V
        //   155: aload_0        
        //   156: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.isHandlingException:Ljava/util/concurrent/atomic/AtomicBoolean;
        //   159: iconst_0       
        //   160: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.set:(Z)V
        //   163: aload_0        
        //   164: monitorexit    
        //   165: return         
        //   166: astore_3       
        //   167: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   170: ldc_w           "CrashlyticsCore"
        //   173: ldc_w           "An error occurred in the uncaught exception handler"
        //   176: aload_3        
        //   177: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   182: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   185: ldc_w           "CrashlyticsCore"
        //   188: ldc_w           "Crashlytics completed exception processing. Invoking default exception handler."
        //   191: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   196: aload_0        
        //   197: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.defaultHandler:Ljava/lang/Thread$UncaughtExceptionHandler;
        //   200: aload_1        
        //   201: aload_2        
        //   202: invokeinterface java/lang/Thread$UncaughtExceptionHandler.uncaughtException:(Ljava/lang/Thread;Ljava/lang/Throwable;)V
        //   207: aload_0        
        //   208: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.isHandlingException:Ljava/util/concurrent/atomic/AtomicBoolean;
        //   211: iconst_0       
        //   212: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.set:(Z)V
        //   215: goto            163
        //   218: astore_1       
        //   219: aload_0        
        //   220: monitorexit    
        //   221: aload_1        
        //   222: athrow         
        //   223: astore_3       
        //   224: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   227: ldc_w           "CrashlyticsCore"
        //   230: ldc_w           "Crashlytics completed exception processing. Invoking default exception handler."
        //   233: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   238: aload_0        
        //   239: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.defaultHandler:Ljava/lang/Thread$UncaughtExceptionHandler;
        //   242: aload_1        
        //   243: aload_2        
        //   244: invokeinterface java/lang/Thread$UncaughtExceptionHandler.uncaughtException:(Ljava/lang/Thread;Ljava/lang/Throwable;)V
        //   249: aload_0        
        //   250: getfield        com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.isHandlingException:Ljava/util/concurrent/atomic/AtomicBoolean;
        //   253: iconst_0       
        //   254: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.set:(Z)V
        //   257: aload_3        
        //   258: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      10     218    223    Any
        //  10     103    166    218    Ljava/lang/Exception;
        //  10     103    223    259    Any
        //  103    130    166    218    Ljava/lang/Exception;
        //  103    130    223    259    Any
        //  130    163    218    223    Any
        //  167    182    223    259    Any
        //  182    215    218    223    Any
        //  224    259    218    223    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0103:
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
    
    private static class AnySessionPartFileFilter implements FilenameFilter
    {
        @Override
        public boolean accept(final File file, final String s) {
            return !CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER.accept(file, s) && CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(s).matches();
        }
    }
    
    static class FileNameContainsFilter implements FilenameFilter
    {
        private final String string;
        
        public FileNameContainsFilter(final String string) {
            this.string = string;
        }
        
        @Override
        public boolean accept(final File file, final String s) {
            return s.contains(this.string) && !s.endsWith(".cls_temp");
        }
    }
    
    static class SessionPartFileFilter implements FilenameFilter
    {
        private final String sessionId;
        
        public SessionPartFileFilter(final String sessionId) {
            this.sessionId = sessionId;
        }
        
        @Override
        public boolean accept(final File file, final String s) {
            return !s.equals(this.sessionId + ".cls") && s.contains(this.sessionId) && !s.endsWith(".cls_temp");
        }
    }
}
