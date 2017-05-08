// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Versioned;
import java.io.Reader;
import com.fasterxml.jackson.core.Version;
import java.util.regex.Pattern;

public class VersionUtil
{
    private static final Pattern VERSION_SEPARATOR;
    private final Version _version;
    
    static {
        VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
    }
    
    protected VersionUtil() {
        Version version = null;
        while (true) {
            try {
                version = versionFor(this.getClass());
                Version unknownVersion = version;
                if (version == null) {
                    unknownVersion = Version.unknownVersion();
                }
                this._version = unknownVersion;
            }
            catch (Exception ex) {
                System.err.println("ERROR: Failed to load Version information for bundle (via " + this.getClass().getName() + ").");
                continue;
            }
            break;
        }
    }
    
    private static Version doReadVersion(final Reader p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          4
        //     3: new             Ljava/io/BufferedReader;
        //     6: dup            
        //     7: aload_0        
        //     8: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    11: astore          5
        //    13: aload           5
        //    15: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    18: astore_2       
        //    19: aload_2        
        //    20: ifnull          133
        //    23: aload           5
        //    25: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    28: astore_3       
        //    29: aload_3        
        //    30: astore_1       
        //    31: aload           4
        //    33: astore_0       
        //    34: aload_3        
        //    35: ifnull          46
        //    38: aload           5
        //    40: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    43: astore_0       
        //    44: aload_3        
        //    45: astore_1       
        //    46: aload           5
        //    48: invokevirtual   java/io/BufferedReader.close:()V
        //    51: aload_1        
        //    52: astore_3       
        //    53: aload_1        
        //    54: ifnull          62
        //    57: aload_1        
        //    58: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    61: astore_3       
        //    62: aload_0        
        //    63: astore_1       
        //    64: aload_0        
        //    65: ifnull          73
        //    68: aload_0        
        //    69: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    72: astore_1       
        //    73: aload_2        
        //    74: aload_3        
        //    75: aload_1        
        //    76: invokestatic    com/fasterxml/jackson/core/util/VersionUtil.parseVersion:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/fasterxml/jackson/core/Version;
        //    79: areturn        
        //    80: astore_3       
        //    81: goto            51
        //    84: astore_0       
        //    85: aconst_null    
        //    86: astore_0       
        //    87: aconst_null    
        //    88: astore_2       
        //    89: aload           5
        //    91: invokevirtual   java/io/BufferedReader.close:()V
        //    94: aload_0        
        //    95: astore_1       
        //    96: aconst_null    
        //    97: astore_0       
        //    98: goto            51
        //   101: astore_1       
        //   102: aload_0        
        //   103: astore_1       
        //   104: aconst_null    
        //   105: astore_0       
        //   106: goto            51
        //   109: astore_0       
        //   110: aload           5
        //   112: invokevirtual   java/io/BufferedReader.close:()V
        //   115: aload_0        
        //   116: athrow         
        //   117: astore_1       
        //   118: goto            115
        //   121: astore_0       
        //   122: aconst_null    
        //   123: astore_0       
        //   124: goto            89
        //   127: astore_0       
        //   128: aload_3        
        //   129: astore_0       
        //   130: goto            89
        //   133: aconst_null    
        //   134: astore_1       
        //   135: aload           4
        //   137: astore_0       
        //   138: goto            46
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  13     19     84     89     Ljava/io/IOException;
        //  13     19     109    121    Any
        //  23     29     121    127    Ljava/io/IOException;
        //  23     29     109    121    Any
        //  38     44     127    133    Ljava/io/IOException;
        //  38     44     109    121    Any
        //  46     51     80     84     Ljava/io/IOException;
        //  89     94     101    109    Ljava/io/IOException;
        //  110    115    117    121    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 87, Size: 87
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
    
    public static Version packageVersionFor(Class<?> forName) {
        try {
            forName = Class.forName(forName.getPackage().getName() + "." + "PackageVersion", true, forName.getClassLoader());
            if (forName == null) {
                return null;
            }
            Object instance;
            try {
                instance = forName.newInstance();
                if (!(instance instanceof Versioned)) {
                    throw new IllegalArgumentException("Bad version class " + forName.getName() + ": does not implement " + Versioned.class.getName());
                }
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex2) {
                throw new IllegalArgumentException("Failed to instantiate " + forName.getName() + " to find version information, problem: " + ex2.getMessage(), ex2);
            }
            return ((Versioned)instance).version();
        }
        catch (Exception ex3) {
            return null;
        }
    }
    
    public static Version parseVersion(String trim, final String s, final String s2) {
        final String s3 = null;
        if (trim != null) {
            trim = trim.trim();
            if (trim.length() != 0) {
                final String[] split = VersionUtil.VERSION_SEPARATOR.split(trim);
                final int versionPart = parseVersionPart(split[0]);
                int versionPart2;
                if (split.length > 1) {
                    versionPart2 = parseVersionPart(split[1]);
                }
                else {
                    versionPart2 = 0;
                }
                int versionPart3;
                if (split.length > 2) {
                    versionPart3 = parseVersionPart(split[2]);
                }
                else {
                    versionPart3 = 0;
                }
                trim = s3;
                if (split.length > 3) {
                    trim = split[3];
                }
                return new Version(versionPart, versionPart2, versionPart3, trim, s, s2);
            }
        }
        return null;
    }
    
    protected static int parseVersionPart(String string) {
        int i = 0;
        string = string.toString();
        final int length = string.length();
        int n = 0;
        while (i < length) {
            final char char1 = string.charAt(i);
            if (char1 > '9' || char1 < '0') {
                break;
            }
            n = n * 10 + (char1 - '0');
            ++i;
        }
        return n;
    }
    
    public static final void throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }
    
    public static Version versionFor(final Class<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokestatic    com/fasterxml/jackson/core/util/VersionUtil.packageVersionFor:(Ljava/lang/Class;)Lcom/fasterxml/jackson/core/Version;
        //     4: astore_1       
        //     5: aload_1        
        //     6: ifnull          11
        //     9: aload_1        
        //    10: areturn        
        //    11: aload_0        
        //    12: ldc             "VERSION.txt"
        //    14: invokevirtual   java/lang/Class.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //    17: astore_0       
        //    18: aload_0        
        //    19: ifnonnull       26
        //    22: invokestatic    com/fasterxml/jackson/core/Version.unknownVersion:()Lcom/fasterxml/jackson/core/Version;
        //    25: areturn        
        //    26: new             Ljava/io/InputStreamReader;
        //    29: dup            
        //    30: aload_0        
        //    31: ldc             "UTF-8"
        //    33: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    36: astore_1       
        //    37: aload_1        
        //    38: invokestatic    com/fasterxml/jackson/core/util/VersionUtil.doReadVersion:(Ljava/io/Reader;)Lcom/fasterxml/jackson/core/Version;
        //    41: astore_2       
        //    42: aload_1        
        //    43: invokevirtual   java/io/InputStreamReader.close:()V
        //    46: aload_0        
        //    47: invokevirtual   java/io/InputStream.close:()V
        //    50: aload_2        
        //    51: areturn        
        //    52: astore_0       
        //    53: new             Ljava/lang/RuntimeException;
        //    56: dup            
        //    57: aload_0        
        //    58: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    61: athrow         
        //    62: astore_2       
        //    63: aload_1        
        //    64: invokevirtual   java/io/InputStreamReader.close:()V
        //    67: aload_2        
        //    68: athrow         
        //    69: astore_1       
        //    70: invokestatic    com/fasterxml/jackson/core/Version.unknownVersion:()Lcom/fasterxml/jackson/core/Version;
        //    73: astore_1       
        //    74: aload_0        
        //    75: invokevirtual   java/io/InputStream.close:()V
        //    78: aload_1        
        //    79: areturn        
        //    80: astore_0       
        //    81: new             Ljava/lang/RuntimeException;
        //    84: dup            
        //    85: aload_0        
        //    86: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    89: athrow         
        //    90: astore_1       
        //    91: aload_0        
        //    92: invokevirtual   java/io/InputStream.close:()V
        //    95: aload_1        
        //    96: athrow         
        //    97: astore_0       
        //    98: new             Ljava/lang/RuntimeException;
        //   101: dup            
        //   102: aload_0        
        //   103: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   106: athrow         
        //   107: astore_1       
        //   108: goto            46
        //   111: astore_1       
        //   112: goto            67
        //    Signature:
        //  (Ljava/lang/Class<*>;)Lcom/fasterxml/jackson/core/Version;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                  
        //  -----  -----  -----  -----  --------------------------------------
        //  26     37     69     90     Ljava/io/UnsupportedEncodingException;
        //  26     37     90     107    Any
        //  37     42     62     69     Any
        //  42     46     107    111    Ljava/io/IOException;
        //  42     46     69     90     Ljava/io/UnsupportedEncodingException;
        //  42     46     90     107    Any
        //  46     50     52     62     Ljava/io/IOException;
        //  63     67     111    115    Ljava/io/IOException;
        //  63     67     69     90     Ljava/io/UnsupportedEncodingException;
        //  63     67     90     107    Any
        //  67     69     69     90     Ljava/io/UnsupportedEncodingException;
        //  67     69     90     107    Any
        //  70     74     90     107    Any
        //  74     78     80     90     Ljava/io/IOException;
        //  91     95     97     107    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 69, Size: 69
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3551)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
}
