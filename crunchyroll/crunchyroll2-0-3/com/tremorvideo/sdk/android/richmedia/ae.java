// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.io.FilterOutputStream;
import java.io.FilterInputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.NameValuePair;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URI;
import com.tremorvideo.sdk.android.videoad.ac;

public class ae
{
    public static int a;
    
    static {
        ae.a = 0;
    }
    
    public static float a(final float n) {
        return 1.0f - (float)Math.cos(n * 3.141592653589793 / 2.0);
    }
    
    public static float a(final int n, final int n2, final float n3) {
        float c;
        if ((n & 0x2) == 0x2) {
            if ((n2 & 0x1) != 0x1) {
                return b(n3);
            }
            c = c(n3);
        }
        else {
            c = n3;
            if ((n2 & 0x1) == 0x1) {
                return a(n3);
            }
        }
        return c;
    }
    
    public static int a(final float n, final float n2, final float n3) {
        return Math.round((1.0f - n3) * n + n2 * n3);
    }
    
    public static int a(final float n, final float n2, final int n3, final int n4, final float n5) {
        return a(n, n2, a(n3, n4, n5));
    }
    
    public static int a(final String s) {
        int n = 5381;
        for (int i = 0; i < s.length(); ++i) {
            n = n + (n << 5) + s.charAt(i);
        }
        return n;
    }
    
    public static String a(final String s, final boolean b, final int n) {
        if (n == 100) {
            return ac.z();
        }
        final Boolean value = false;
        while (true) {
            try {
                final String host = new URI(s).getHost();
                Boolean value2 = value;
                if (host != null) {
                    if (!host.toLowerCase().contains("scanscout.com")) {
                        value2 = value;
                        if (!host.toLowerCase().contains("videohub.tv")) {
                            break Label_0068;
                        }
                    }
                    value2 = true;
                }
                switch (ae.a) {
                    default: {
                        return null;
                    }
                    case 0: {
                        if (b || value2) {
                            return ac.z();
                        }
                        return ac.y();
                    }
                    case 1: {
                        if (b || value2) {
                            return ac.z();
                        }
                        return ac.y();
                    }
                    case 2: {
                        return ac.y();
                    }
                    case 3: {
                        return ac.z();
                    }
                }
            }
            catch (URISyntaxException ex) {
                final Boolean value2 = value;
                continue;
            }
            break;
        }
    }
    
    public static String a(final List<NameValuePair> list) {
        StringBuilder sb;
        while (true) {
            sb = new StringBuilder();
            int n = 1;
            while (true) {
                Label_0115: {
                    try {
                        for (final NameValuePair nameValuePair : list) {
                            if (n == 0) {
                                break Label_0115;
                            }
                            n = 0;
                            sb.append(URLEncoder.encode(nameValuePair.getName(), "UTF-8"));
                            sb.append("=");
                            sb.append(URLEncoder.encode(nameValuePair.getValue(), "UTF-8"));
                        }
                    }
                    catch (UnsupportedEncodingException ex) {
                        ac.e("Utility.getEncondedPairString: " + ex.getMessage());
                    }
                    break;
                }
                sb.append("&");
                continue;
            }
        }
        return sb.toString();
    }
    
    public static String a(final ZipFile zipFile, final String s) {
        String s3;
        try {
            final String s2 = s3 = zipFile.getName();
            if (s2.contains(".zip")) {
                s3 = s2.replace(".zip", "");
            }
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                if (zipEntry.getName().startsWith(s)) {
                    a(zipFile, zipEntry, s3, true);
                }
            }
        }
        catch (Exception ex) {
            ac.a(ex);
            return null;
        }
        return s3;
    }
    
    public static void a(final File file) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                a(listFiles[i]);
            }
        }
        file.delete();
    }
    
    private static void a(ZipFile zipFile, ZipEntry zipEntry, final String s, final boolean b) throws IOException {
        if (zipEntry.isDirectory()) {
            c(new File(s, zipEntry.getName()));
        }
        else {
            final File file = new File(s, zipEntry.getName());
            if (b || !file.exists()) {
                if (!file.getParentFile().exists()) {
                    c(file.getParentFile());
                }
                ac.e("GenericObjec extracting: " + zipEntry);
                zipFile = (ZipFile)new BufferedInputStream(zipFile.getInputStream(zipEntry));
                zipEntry = (ZipEntry)new BufferedOutputStream(new FileOutputStream(file));
                try {
                    final byte[] array = new byte[1024];
                    while (true) {
                        final int read = ((FilterInputStream)zipFile).read(array);
                        if (read == -1) {
                            break;
                        }
                        ((BufferedOutputStream)zipEntry).write(array, 0, read);
                    }
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
                finally {
                    ((FilterOutputStream)zipEntry).close();
                    ((BufferedInputStream)zipFile).close();
                }
            }
        }
    }
    
    public static void a(final HttpGet httpGet, String c) {
        c = c(c);
        if (c != null) {
            httpGet.setHeader("User-Agent", c);
        }
    }
    
    public static void a(final HttpPost httpPost, String c) {
        c = c(c);
        if (c != null) {
            httpPost.setHeader("User-Agent", c);
        }
    }
    
    public static float b(final float n) {
        return (float)(-Math.cos(n * 1.5707963267948966 + 1.5707963267948966));
    }
    
    public static float b(final float n, final float n2, final float n3) {
        return (1.0f - n3) * n + n2 * n3;
    }
    
    public static float b(final float n, final float n2, final int n3, final int n4, final float n5) {
        return b(n, n2, a(n3, n4, n5));
    }
    
    public static String b(File file) {
        final StringBuilder sb = new StringBuilder();
        Label_0076: {
            try {
                file = (File)new BufferedReader(new FileReader(file));
                try {
                    while (true) {
                        final String line = ((BufferedReader)file).readLine();
                        if (line == null) {
                            break Label_0076;
                        }
                        sb.append(line.trim());
                        sb.append(System.getProperty("line.separator", "\n"));
                    }
                }
                finally {
                    ((BufferedReader)file).close();
                }
            }
            catch (Exception ex) {
                ac.a(ex);
            }
            return sb.toString();
        }
        ((BufferedReader)file).close();
        return sb.toString();
    }
    
    public static String b(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/io/File;
        //     3: dup            
        //     4: aload_0        
        //     5: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //     8: astore_0       
        //     9: aload_0        
        //    10: invokevirtual   java/io/File.exists:()Z
        //    13: ifne            18
        //    16: aconst_null    
        //    17: areturn        
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: astore_3       
        //    26: new             Ljava/io/BufferedReader;
        //    29: dup            
        //    30: new             Ljava/io/FileReader;
        //    33: dup            
        //    34: aload_0        
        //    35: invokespecial   java/io/FileReader.<init>:(Ljava/io/File;)V
        //    38: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    41: astore_1       
        //    42: aload_1        
        //    43: astore_0       
        //    44: aload_1        
        //    45: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    48: astore_2       
        //    49: aload_2        
        //    50: ifnull          84
        //    53: aload_1        
        //    54: astore_0       
        //    55: aload_3        
        //    56: aload_2        
        //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    60: pop            
        //    61: goto            42
        //    64: astore_2       
        //    65: aload_1        
        //    66: astore_0       
        //    67: aload_2        
        //    68: invokevirtual   java/lang/Exception.printStackTrace:()V
        //    71: aload_1        
        //    72: ifnull          79
        //    75: aload_1        
        //    76: invokevirtual   java/io/BufferedReader.close:()V
        //    79: aload_3        
        //    80: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    83: areturn        
        //    84: aload_1        
        //    85: ifnull          79
        //    88: aload_1        
        //    89: invokevirtual   java/io/BufferedReader.close:()V
        //    92: goto            79
        //    95: astore_0       
        //    96: aload_0        
        //    97: invokevirtual   java/io/IOException.printStackTrace:()V
        //   100: goto            79
        //   103: astore_0       
        //   104: aload_0        
        //   105: invokevirtual   java/io/IOException.printStackTrace:()V
        //   108: goto            79
        //   111: astore_1       
        //   112: aconst_null    
        //   113: astore_0       
        //   114: aload_0        
        //   115: ifnull          122
        //   118: aload_0        
        //   119: invokevirtual   java/io/BufferedReader.close:()V
        //   122: aload_1        
        //   123: athrow         
        //   124: astore_0       
        //   125: aload_0        
        //   126: invokevirtual   java/io/IOException.printStackTrace:()V
        //   129: goto            122
        //   132: astore_1       
        //   133: goto            114
        //   136: astore_2       
        //   137: aconst_null    
        //   138: astore_1       
        //   139: goto            65
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  26     42     136    142    Ljava/lang/Exception;
        //  26     42     111    114    Any
        //  44     49     64     65     Ljava/lang/Exception;
        //  44     49     132    136    Any
        //  55     61     64     65     Ljava/lang/Exception;
        //  55     61     132    136    Any
        //  67     71     132    136    Any
        //  75     79     103    111    Ljava/io/IOException;
        //  88     92     95     103    Ljava/io/IOException;
        //  118    122    124    132    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0065:
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
    
    public static float c(final float n) {
        return (float)(1.0 - Math.cos(n * 3.141592653589793)) / 2.0f;
    }
    
    public static String c(final String s) {
        return a(s, false, ae.a);
    }
    
    private static void c(final File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Can not create dir " + file);
        }
    }
}
