// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import java.io.Writer;
import java.io.OutputStream;

final class ExceptionUtils
{
    private static String getMessage(final Throwable t) {
        final String localizedMessage = t.getLocalizedMessage();
        if (localizedMessage == null) {
            return null;
        }
        return localizedMessage.replaceAll("(\r\n|\n|\f)", " ");
    }
    
    private static void writeStackTrace(final Throwable p0, final OutputStream p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: new             Ljava/io/PrintWriter;
        //     7: dup            
        //     8: aload_1        
        //     9: invokespecial   java/io/PrintWriter.<init>:(Ljava/io/OutputStream;)V
        //    12: astore_1       
        //    13: aload_0        
        //    14: aload_1        
        //    15: invokestatic    com/crashlytics/android/core/ExceptionUtils.writeStackTrace:(Ljava/lang/Throwable;Ljava/io/Writer;)V
        //    18: aload_1        
        //    19: ldc             "Failed to close stack trace writer."
        //    21: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    24: return         
        //    25: astore_1       
        //    26: aload_3        
        //    27: astore_0       
        //    28: aload_0        
        //    29: astore_2       
        //    30: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    33: ldc             "CrashlyticsCore"
        //    35: ldc             "Failed to create PrintWriter"
        //    37: aload_1        
        //    38: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    43: aload_0        
        //    44: ldc             "Failed to close stack trace writer."
        //    46: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    49: return         
        //    50: astore_0       
        //    51: aload_2        
        //    52: ldc             "Failed to close stack trace writer."
        //    54: invokestatic    io/fabric/sdk/android/services/common/CommonUtils.closeOrLog:(Ljava/io/Closeable;Ljava/lang/String;)V
        //    57: aload_0        
        //    58: athrow         
        //    59: astore_0       
        //    60: aload_1        
        //    61: astore_2       
        //    62: goto            51
        //    65: astore_2       
        //    66: aload_1        
        //    67: astore_0       
        //    68: aload_2        
        //    69: astore_1       
        //    70: goto            28
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      13     25     28     Ljava/lang/Exception;
        //  4      13     50     51     Any
        //  13     18     65     73     Ljava/lang/Exception;
        //  13     18     59     65     Any
        //  30     43     50     51     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0028:
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
    
    private static void writeStackTrace(Throwable cause, final Writer writer) {
        boolean b = true;
    Label_0154:
        while (cause != null) {
            while (true) {
            Label_0020_Outer:
                while (true) {
                    Label_0166: {
                        while (true) {
                            try {
                                final String message = getMessage(cause);
                                if (message != null) {
                                    break Label_0155;
                                }
                                break Label_0166;
                                while (true) {
                                    final StackTraceElement[] stackTrace;
                                    int n = 0;
                                    writer.write("\tat " + stackTrace[n].toString() + "\n");
                                    ++n;
                                    Label_0079: {
                                        break Label_0079;
                                        Label_0130: {
                                            cause = cause.getCause();
                                        }
                                        final boolean b2;
                                        b = b2;
                                        break;
                                        final String s;
                                        writer.write(s + cause.getClass().getName() + ": " + message + "\n");
                                        b2 = false;
                                        stackTrace = cause.getStackTrace();
                                        final int length = stackTrace.length;
                                        n = 0;
                                    }
                                    continue Label_0020_Outer;
                                }
                            }
                            // iftrue(Label_0130:, n >= length)
                            catch (Exception ex) {
                                Fabric.getLogger().e("CrashlyticsCore", "Could not write stack trace", ex);
                            }
                            break Label_0154;
                            if (b) {
                                final String s = "";
                                continue;
                            }
                            final String s = "Caused by: ";
                            continue;
                        }
                    }
                    final String message = "";
                    continue;
                }
            }
        }
    }
    
    public static void writeStackTraceIfNotNull(final Throwable t, final OutputStream outputStream) {
        if (outputStream != null) {
            writeStackTrace(t, outputStream);
        }
    }
}
