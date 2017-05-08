// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Iterator;
import org.apache.http.message.BasicNameValuePair;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class by
{
    public String a;
    public int b;
    public boolean c;
    public int d;
    public int e;
    
    public by(final String a) {
        this.b = -1;
        this.c = false;
        this.d = 0;
        this.e = 0;
        this.a = a;
        this.b = -1;
        this.c = false;
        this.d = 0;
        this.e = 0;
    }
    
    public static int a(final JSONArray jsonArray) {
        boolean b = false;
        final String string = jsonArray.toString();
        if (string.contains("UT")) {
            b = true;
        }
        int n = b ? 1 : 0;
        if (string.contains("Umd")) {
            n = ((b ? 1 : 0) | 0x2);
        }
        int n2 = n;
        if (string.contains("RwT")) {
            n2 = (n | 0x4);
        }
        int n3 = n2;
        if (string.contains("RmT")) {
            n3 = (n2 | 0x8);
        }
        int n4 = n3;
        if (string.contains("RvN")) {
            n4 = (n3 | 0x10);
        }
        int n5 = n4;
        if (string.contains("AmN")) {
            n5 = (n4 | 0x20);
        }
        int n6 = n5;
        if (string.contains("EiN")) {
            n6 = (n5 | 0x40);
        }
        int n7 = n6;
        if (string.contains("SfF")) {
            n7 = (n6 | 0x80);
        }
        int n8 = n7;
        if (string.contains("Ee")) {
            n8 = (n7 | 0x100);
        }
        return n8;
    }
    
    public static by a(final JSONObject p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore_2       
        //     2: aload_2        
        //     3: astore_1       
        //     4: aload_0        
        //     5: ldc             "url"
        //     7: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    10: ifeq            103
        //    13: aload_0        
        //    14: ldc             "url"
        //    16: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //    19: astore_3       
        //    20: aload_2        
        //    21: astore_1       
        //    22: aload_3        
        //    23: invokestatic    android/webkit/URLUtil.isValidUrl:(Ljava/lang/String;)Z
        //    26: iconst_1       
        //    27: if_icmpne       103
        //    30: new             Lcom/tremorvideo/sdk/android/videoad/by;
        //    33: dup            
        //    34: aload_3        
        //    35: invokespecial   com/tremorvideo/sdk/android/videoad/by.<init>:(Ljava/lang/String;)V
        //    38: astore_2       
        //    39: aload_0        
        //    40: ldc             "load-count"
        //    42: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    45: ifeq            58
        //    48: aload_2        
        //    49: aload_0        
        //    50: ldc             "load-count"
        //    52: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
        //    55: putfield        com/tremorvideo/sdk/android/videoad/by.b:I
        //    58: aload_0        
        //    59: ldc             "internal"
        //    61: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    64: ifeq            77
        //    67: aload_2        
        //    68: aload_0        
        //    69: ldc             "internal"
        //    71: invokevirtual   org/json/JSONObject.getBoolean:(Ljava/lang/String;)Z
        //    74: putfield        com/tremorvideo/sdk/android/videoad/by.c:Z
        //    77: aload_2        
        //    78: astore_1       
        //    79: aload_0        
        //    80: ldc             "log-vars"
        //    82: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
        //    85: ifeq            103
        //    88: aload_2        
        //    89: aload_0        
        //    90: ldc             "log-vars"
        //    92: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
        //    95: invokestatic    com/tremorvideo/sdk/android/videoad/by.a:(Lorg/json/JSONArray;)I
        //    98: putfield        com/tremorvideo/sdk/android/videoad/by.d:I
        //   101: aload_2        
        //   102: astore_1       
        //   103: aload_1        
        //   104: areturn        
        //   105: astore_1       
        //   106: new             Ljava/lang/StringBuilder;
        //   109: dup            
        //   110: invokespecial   java/lang/StringBuilder.<init>:()V
        //   113: ldc             "Error loading Tracking data: "
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   118: aload_0        
        //   119: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   122: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   125: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   128: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   131: aconst_null    
        //   132: areturn        
        //   133: astore_1       
        //   134: new             Ljava/lang/StringBuilder;
        //   137: dup            
        //   138: invokespecial   java/lang/StringBuilder.<init>:()V
        //   141: ldc             "Error loading load-count for: "
        //   143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   146: aload_0        
        //   147: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   150: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   153: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   156: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   159: goto            58
        //   162: astore_1       
        //   163: new             Ljava/lang/StringBuilder;
        //   166: dup            
        //   167: invokespecial   java/lang/StringBuilder.<init>:()V
        //   170: ldc             "Error loading internal for: "
        //   172: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   175: aload_0        
        //   176: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   179: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   182: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   185: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   188: goto            77
        //   191: astore_1       
        //   192: new             Ljava/lang/StringBuilder;
        //   195: dup            
        //   196: invokespecial   java/lang/StringBuilder.<init>:()V
        //   199: ldc             "Error loading log-vars for: "
        //   201: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   204: aload_0        
        //   205: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   208: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   211: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   214: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   217: aload_2        
        //   218: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  13     20     105    133    Ljava/lang/Exception;
        //  48     58     133    162    Ljava/lang/Exception;
        //  67     77     162    191    Ljava/lang/Exception;
        //  88     101    191    219    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 107, Size: 107
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
    
    private List<NameValuePair> b(final ay.a a) {
        final ArrayList<BasicNameValuePair> list = (ArrayList<BasicNameValuePair>)new ArrayList<NameValuePair>();
        if (this.a(1)) {
            list.add((NameValuePair)new BasicNameValuePair("UT", new SimpleDateFormat("yyMMddHH").format(new Date())));
        }
        if (this.a(2)) {
            if (a.g != null) {
                list.add((NameValuePair)new BasicNameValuePair("Umd", "" + a.g.a + "," + a.g.b + "," + a.g.c + "," + a.g.d));
            }
            else {
                list.add((NameValuePair)new BasicNameValuePair("Umd", "0,0," + ac.m() + "," + ac.n()));
            }
        }
        if (this.a(4)) {
            list.add((NameValuePair)new BasicNameValuePair("RwT", "" + (a.d + a.e - a.a.b)));
        }
        if (this.a(8)) {
            list.add((NameValuePair)new BasicNameValuePair("RmT", "" + a.e));
        }
        if (this.a(16)) {
            list.add((NameValuePair)new BasicNameValuePair("RvN", "" + a.f));
        }
        if (this.a(32)) {
            list.add((NameValuePair)new BasicNameValuePair("AmN", "" + a.f));
        }
        if (this.a(64)) {
            list.add((NameValuePair)new BasicNameValuePair("EiN", "" + a.f));
        }
        if (this.a(128)) {
            if (this.e == 0) {
                list.add((NameValuePair)new BasicNameValuePair("SfF", "true"));
            }
            else {
                list.add((NameValuePair)new BasicNameValuePair("SfF", "false"));
            }
        }
        if (this.a(256)) {
            list.add((NameValuePair)new BasicNameValuePair("Ee", "1"));
        }
        return (List<NameValuePair>)list;
    }
    
    public void a(final ay.a a) {
        if (this.b > 0 && this.e >= this.b) {
            ac.e("Load count for pixel reached: " + this.e + "/" + this.b);
            return;
        }
        List<NameValuePair> list = null;
        while (true) {
            if (!this.c) {
                break Label_0364;
            }
            final List<NameValuePair> b = this.b(a);
            if (b != null && b.size() > 0) {
                ac.e("LogVars: ");
                for (final NameValuePair nameValuePair : b) {
                    ac.e(nameValuePair.getName() + "=" + nameValuePair.getValue());
                }
            }
            Label_0447: {
                if (a.h == null || a.h.size() <= 0) {
                    break Label_0447;
                }
                if ((list = b) == null) {
                    list = new ArrayList<NameValuePair>();
                }
                try {
                    final Iterator<NameValuePair> iterator2 = a.h.iterator();
                    String string = "";
                    while (iterator2.hasNext()) {
                        final NameValuePair nameValuePair2 = iterator2.next();
                        String string2 = string;
                        if (string.length() > 0) {
                            string2 = string + "&";
                        }
                        string = string2 + nameValuePair2.getName() + "=" + nameValuePair2.getValue();
                    }
                    ac.e("Additional Data: " + string);
                    list.add((NameValuePair)new BasicNameValuePair("ssEd", string));
                    final int n = -1;
                    String h = "";
                    int k = n;
                    if (a != null) {
                        h = h;
                        k = n;
                        if (a.c != null) {
                            k = a.c.k();
                            h = a.c.h();
                        }
                    }
                    z.a(this.a, list, this.c, k, h);
                    ++this.e;
                    return;
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
            }
            list = b;
            continue;
        }
    }
    
    public boolean a(final int n) {
        return (this.d & n) > 0;
    }
}
