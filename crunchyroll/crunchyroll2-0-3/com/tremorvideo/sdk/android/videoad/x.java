// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.io.FileOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

public class x
{
    private static final String[] a;
    private static final String[] b;
    private static List<n> e;
    private File c;
    private List<a> d;
    
    static {
        a = new String[] { ".theme", ".ad", ".cookies", ".savedata" };
        b = new String[] { ".dat", ".views" };
        x.e = new ArrayList<n>();
    }
    
    public x(final File p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: aload_0        
        //     5: aload_1        
        //     6: putfield        com/tremorvideo/sdk/android/videoad/x.c:Ljava/io/File;
        //     9: aload_0        
        //    10: new             Ljava/util/ArrayList;
        //    13: dup            
        //    14: invokespecial   java/util/ArrayList.<init>:()V
        //    17: putfield        com/tremorvideo/sdk/android/videoad/x.d:Ljava/util/List;
        //    20: aload_0        
        //    21: invokevirtual   com/tremorvideo/sdk/android/videoad/x.a:()V
        //    24: aload_1        
        //    25: invokevirtual   java/io/File.list:()[Ljava/lang/String;
        //    28: astore_1       
        //    29: ldc             "Loading Cache..."
        //    31: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //    34: aload_1        
        //    35: arraylength    
        //    36: istore_3       
        //    37: iconst_0       
        //    38: istore_2       
        //    39: iload_2        
        //    40: iload_3        
        //    41: if_icmpge       182
        //    44: aload_1        
        //    45: iload_2        
        //    46: aaload         
        //    47: astore          5
        //    49: aload           5
        //    51: ldc             "tremor-cache-1105352-"
        //    53: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    56: ifeq            136
        //    59: aload           5
        //    61: ldc             ".ad"
        //    63: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    66: ifeq            136
        //    69: new             Ljava/lang/StringBuilder;
        //    72: dup            
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: ldc             "Loading cache data: "
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: aload           5
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    86: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    89: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //    92: new             Lcom/tremorvideo/sdk/android/videoad/x$a;
        //    95: dup            
        //    96: aload_0        
        //    97: invokespecial   com/tremorvideo/sdk/android/videoad/x$a.<init>:(Lcom/tremorvideo/sdk/android/videoad/x;)V
        //   100: astore          4
        //   102: new             Ljava/io/File;
        //   105: dup            
        //   106: aload_0        
        //   107: getfield        com/tremorvideo/sdk/android/videoad/x.c:Ljava/io/File;
        //   110: aload           5
        //   112: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   115: astore          5
        //   117: aload           4
        //   119: aload           5
        //   121: invokevirtual   com/tremorvideo/sdk/android/videoad/x$a.a:(Ljava/io/File;)V
        //   124: aload_0        
        //   125: getfield        com/tremorvideo/sdk/android/videoad/x.d:Ljava/util/List;
        //   128: aload           4
        //   130: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   135: pop            
        //   136: iload_2        
        //   137: iconst_1       
        //   138: iadd           
        //   139: istore_2       
        //   140: goto            39
        //   143: astore          4
        //   145: new             Ljava/lang/StringBuilder;
        //   148: dup            
        //   149: invokespecial   java/lang/StringBuilder.<init>:()V
        //   152: ldc             "Error loading cache file: "
        //   154: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   157: aload           5
        //   159: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   162: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   165: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   168: aload           4
        //   170: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   173: aload           5
        //   175: invokevirtual   java/io/File.delete:()Z
        //   178: pop            
        //   179: goto            136
        //   182: new             Ljava/lang/StringBuilder;
        //   185: dup            
        //   186: invokespecial   java/lang/StringBuilder.<init>:()V
        //   189: ldc             "Cache Loaded: "
        //   191: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   194: aload_0        
        //   195: invokevirtual   com/tremorvideo/sdk/android/videoad/x.c:()J
        //   198: ldc2_w          1024
        //   201: ldiv           
        //   202: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   205: ldc             "KB"
        //   207: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   210: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   213: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
        //   216: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  117    136    143    182    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
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
    
    public static File a(final File file, final String s) {
        return new File(file.getAbsolutePath() + File.separator + "tremor-cache-1105352-" + s);
    }
    
    public static void a(final n n) {
        x.e.add(n);
    }
    
    private boolean a(final String s) {
        final boolean b = false;
        final String[] a = x.a;
        final int length = a.length;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= length) {
                break;
            }
            if (s.toLowerCase().endsWith(a[n])) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
    }
    
    public static void b(final n n) {
        x.e.remove(n);
    }
    
    private boolean b(final String s) {
        final boolean b = false;
        final String[] b2 = x.b;
        final int length = b2.length;
        int n = 0;
        boolean b3;
        while (true) {
            b3 = b;
            if (n >= length) {
                break;
            }
            if (s.endsWith(b2[n])) {
                b3 = true;
                break;
            }
            ++n;
        }
        return b3;
    }
    
    private boolean c(String replace) {
        replace = replace.replace(this.c.getAbsolutePath() + File.separator, "").replace("tremor-cache-1105352-", "");
        for (final a a : this.d) {
            for (int i = 0; i < a.d.length; ++i) {
                if (replace.equals(a.d[i])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private String d(final String s) {
        return this.c.getAbsolutePath() + File.separator + "tremor-cache-1105352-" + s;
    }
    
    private File e(final String s) {
        return new File(this.c.getAbsolutePath(), "tremor-cache-1105352-" + s);
    }
    
    private void e(final n n) {
        long n2 = 0L;
        for (int i = 0; i < n.j(); ++i) {
            n2 += n.d(i);
        }
        if (this.c() + n2 > 41943040L) {
            ac.e("Freeing space in cache: Current Size: " + this.c() + " Ad size: " + n2);
            while (this.c() + n2 > 41943040L && this.d.size() > 0) {
                final a a = this.a(this.c() + n2 - 41943040L);
                if (a == null) {
                    break;
                }
                this.a(a, n.k());
            }
        }
    }
    
    private File f(final n n) {
        return new File(this.c.getAbsolutePath(), "tremor-cache-1105352-" + n.i() + ".ad");
    }
    
    private void f() {
        ac.e("Checking for expired files...");
        final ArrayList<a> list = new ArrayList<a>();
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        for (final a a : this.d) {
            if (!this.a(a) && gregorianCalendar.after(n.a(a.b))) {
                list.add(a);
            }
        }
        for (final a a2 : list) {
            this.b(a2);
            this.d.remove(a2);
        }
        ac.e("Done checking for expired files.");
    }
    
    public a a(final long n) {
        final Iterator<a> iterator = this.d.iterator();
        a a = null;
        a a2 = null;
        while (iterator.hasNext()) {
            final a a3 = iterator.next();
            if (!this.a(a3)) {
                a a4 = a;
                if (a3.e >= n) {
                    if (a == null) {
                        a4 = a3;
                    }
                    else {
                        a4 = a;
                        if (a.e > a3.e) {
                            a4 = a3;
                        }
                    }
                }
                a a5;
                if (a2 == null) {
                    a5 = a3;
                }
                else {
                    a5 = a3;
                    if (a2.f <= a3.f) {
                        a5 = a2;
                    }
                }
                a2 = a5;
                a = a4;
            }
        }
        if (a2 != null && a != null && a2.f == a.f) {
            return a;
        }
        return a2;
    }
    
    public void a() {
        if (ac.e()) {
            final String[] list = this.c.list();
            ac.e("Displaing Cache...");
            for (int length = list.length, i = 0; i < length; ++i) {
                final String s = list[i];
                if (s.startsWith("tremor-cache-1105352-")) {
                    ac.e("File: " + s.replace("tremor-cache-1105352-", ""));
                }
            }
        }
    }
    
    public void a(final a a, final String s) {
        for (final a a2 : this.d) {
            if (a2 != a) {
                for (int i = 0; i < a2.d.length; ++i) {
                    if (a2.d[i].equals(s)) {
                        ac.e("Keep shared cache File: " + s);
                        return;
                    }
                }
                continue;
            }
        }
        final File e = this.e(s);
        if (e.exists()) {
            ac.e("Removing Cache File: " + e.getAbsolutePath());
            e.delete();
        }
    }
    
    public void a(final a a, String[] a2) {
        final int n = 0;
        int i = 0;
    Label_0051_Outer:
        while (i < a.d.length) {
        Label_0051:
            while (true) {
                if (a2 != null) {
                    for (int length = a2.length, j = 0; j < length; ++j) {
                        if (a2[j].equals(a.d[i])) {
                            final int n2 = 1;
                            break Label_0051;
                        }
                    }
                }
                Label_0230: {
                    break Label_0230;
                    final int n2;
                    if (n2 == 0) {
                        this.a(a, a.d[i]);
                    }
                    else {
                        ac.e("Keeping future shared cache file: " + a.d[i]);
                    }
                    ++i;
                    continue Label_0051_Outer;
                }
                final int n2 = 0;
                continue Label_0051;
            }
        }
        a2 = x.a;
        for (int length2 = a2.length, k = n; k < length2; ++k) {
            final String d = this.d(a.a + a2[k]);
            final File file = new File(d);
            if (file.exists()) {
                ac.e("Removing Cache File: " + d);
                file.delete();
            }
        }
        this.d.remove(a);
    }
    
    public boolean a(final a a) {
        for (final n n : x.e) {
            if (n.i() != null && a.a.compareTo(n.i()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    public void b() {
        this.d();
        this.f();
        final String[] list = this.c.list();
        for (int length = list.length, i = 0; i < length; ++i) {
            final String s = list[i];
            if (s.startsWith("tremor-cache-1105352-")) {
                ac.e("Cache file: " + s);
            }
        }
    }
    
    public void b(final a a) {
        this.a(a, (String[])null);
    }
    
    public long c() {
        final Iterator<a> iterator = this.d.iterator();
        long n = 0L;
        while (iterator.hasNext()) {
            n += iterator.next().e;
        }
        return n;
    }
    
    public void c(final n n) {
        final File f = this.f(n);
        if (!f.exists()) {
            ac.e("Error Cannot increment views for: " + n.i());
            return;
        }
        try {
            final a a = new a();
            a.a(f);
            ++a.c;
            a.a(f.getAbsolutePath());
            ac.e("Incrementing View Count For: " + n.i() + ": " + a.c);
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public void d() {
        ac.e("Checking for orphaned files...");
        final String[] list = this.c.list();
        for (int length = list.length, i = 0; i < length; ++i) {
            final String s = list[i];
            int n;
            if (s.startsWith("transpera-cache-1105352")) {
                n = 1;
            }
            else {
                if (!s.startsWith("tremor-cache-1105352-") || this.a(s)) {
                    continue;
                }
                if (this.b(s)) {
                    ac.e("Removing legacy file: " + s);
                    n = 1;
                }
                else if (this.c(s)) {
                    ac.e("Removing orphaned file: " + s);
                    n = 1;
                }
                else {
                    n = 0;
                }
            }
            if (n != 0) {
                new File(this.c, s).delete();
            }
        }
        ac.e("Done checking for orphaned files.");
    }
    
    public void d(final n n) {
        final File f = this.f(n);
        if (!f.exists()) {
            ac.e("Adding ad to cache: " + n.i() + "...");
            this.e(n);
            new a(n, this.c).a(f.getAbsolutePath());
        }
        else {
            ac.e("Updating cached ad: " + n.i() + "...");
            ac.e("Cache Usage: " + this.c() + "/" + 41943040L);
            try {
                final a a = new a();
                a.a(f);
                a.a(n);
                a.a(f.getAbsolutePath());
            }
            catch (Exception ex) {
                ac.a(ex);
            }
        }
        ac.e("Cache info: ");
        for (final a a2 : this.d) {
            ac.e(a2.a + " : " + a2.e);
        }
    }
    
    public y e() {
        final y y = new y();
        for (final a a : this.d) {
            y.a(a.a, a.c);
        }
        return y;
    }
    
    private class a
    {
        public String a;
        public String b;
        public int c;
        public String[] d;
        public long e;
        public int f;
        
        public a() {
        }
        
        public a(final n n, File a) {
            final int n2 = 0;
            this.a = n.i();
            this.c = 0;
            this.b = n.h();
            this.f = 0;
            final String[] k = n.k();
            this.d = new String[k.length];
            for (int i = 0; i < this.d.length; ++i) {
                this.d[i] = k[i];
            }
            for (int j = 0; j < k.length; ++j) {
                this.e += n.d(j);
            }
            if (n instanceof s) {
                final String[] d = this.d;
                for (int length = d.length, l = n2; l < length; ++l) {
                    a = x.this.e(d[l]);
                    if (a.exists()) {
                        this.e += (int)a.length();
                    }
                }
            }
        }
        
        public void a(final n n) {
            final int n2 = 0;
            this.b = n.h();
            this.f = 0;
            final String[] k = n.k();
            this.d = new String[k.length];
            for (int i = 0; i < this.d.length; ++i) {
                this.d[i] = k[i];
            }
            this.e = 0L;
            for (int j = 0; j < k.length; ++j) {
                this.e += n.d(j);
            }
            if (n instanceof s) {
                final String[] d = this.d;
                for (int length = d.length, l = n2; l < length; ++l) {
                    final File a = x.this.e(d[l]);
                    if (a.exists()) {
                        this.e += (int)a.length();
                    }
                }
            }
        }
        
        public void a(final File file) throws Exception {
            int i = 0;
            final JSONObject jsonObject = new JSONObject(ac.a(file));
            this.c = jsonObject.getInt("views");
            this.a = jsonObject.getString("base-file");
            this.b = jsonObject.getString("expiration-date");
            this.e = jsonObject.getLong("total-size");
            this.f = jsonObject.getInt("priority");
            if (!jsonObject.has("files")) {
                this.d = new String[0];
            }
            else {
                final JSONArray jsonArray = jsonObject.getJSONArray("files");
                this.d = new String[jsonArray.length()];
                while (i < jsonArray.length()) {
                    this.d[i] = jsonArray.getString(i);
                    ++i;
                }
            }
        }
        
        public void a(final String s) {
            try {
                final JSONObject jsonObject = new JSONObject();
                final JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < this.d.length; ++i) {
                    jsonArray.put((Object)this.d[i]);
                }
                jsonObject.put("files", (Object)jsonArray);
                jsonObject.put("views", this.c);
                jsonObject.put("expiration-date", (Object)this.b);
                jsonObject.put("base-file", (Object)this.a);
                jsonObject.put("total-size", this.e);
                jsonObject.put("priority", this.f);
                final FileOutputStream fileOutputStream = new FileOutputStream(new File(s));
                fileOutputStream.write(jsonObject.toString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (Exception ex) {
                ac.a(ex);
            }
        }
    }
}
