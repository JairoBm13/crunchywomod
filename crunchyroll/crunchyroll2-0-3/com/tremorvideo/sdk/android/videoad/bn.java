// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Map;
import android.content.Context;

public class bn extends bf
{
    static String a;
    boolean b;
    Context c;
    n d;
    int e;
    boolean f;
    String g;
    
    static {
        bn.a = "embedPlayer_";
    }
    
    public bn(final a a, final Context c, final n d, final Map<String, Object> map) {
        super(a);
        this.b = false;
        this.e = -1;
        this.f = false;
        this.g = "";
        this.c = c;
        this.d = d;
        this.e = map.get("hashName");
    }
    
    private void a(final String s, final String s2) {
        final File file = new File(s2 + s);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }
    
    public String a(final String s) {
        final File file = new File(s);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            ZipInputStream zipInputStream = null;
            String string = null;
        Label_0352:
            while (true) {
                while (true) {
                    String name = null;
                    Label_0202: {
                        try {
                            fileInputStream = new FileInputStream(file);
                            zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
                            final File file2 = new File(this.c.getFilesDir() + "/" + (bn.a + this.e));
                            if (file2.exists()) {
                                ae.a(file2);
                            }
                            file2.mkdir();
                            string = file2.getAbsolutePath() + "/";
                            while (true) {
                                final ZipEntry nextEntry = zipInputStream.getNextEntry();
                                if (nextEntry == null) {
                                    break Label_0352;
                                }
                                name = nextEntry.getName();
                                if (!nextEntry.isDirectory()) {
                                    break Label_0202;
                                }
                                this.a(name, string);
                            }
                        }
                        catch (IOException ex) {
                            this.f = true;
                            this.g = ex.toString();
                        }
                        return null;
                    }
                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    final byte[] array = new byte[1024];
                    final File file3 = new File(string + name);
                    if (!file3.exists()) {
                        file3.createNewFile();
                    }
                    final FileOutputStream fileOutputStream = new FileOutputStream(string + name);
                    while (true) {
                        final int read = zipInputStream.read(array);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(array, 0, read);
                        byteArrayOutputStream.toByteArray();
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        byteArrayOutputStream.reset();
                    }
                    fileOutputStream.close();
                    zipInputStream.closeEntry();
                    byteArrayOutputStream.close();
                    continue;
                }
            }
            zipInputStream.close();
            fileInputStream.close();
            return string;
        }
        ac.e("Error Processing Embed player: Unable to find archive: " + s);
        this.a(bf.b.c);
        return null;
    }
    
    @Override
    protected void e() {
        this.h();
    }
    
    @Override
    protected void f() {
        this.a(bf.b.e);
    }
    
    @Override
    protected void g() {
        this.h();
    }
    
    public void h() {
        this.f = false;
        this.g = "";
        final r.a h = this.d.h(this.e);
        if (h != null) {
            final String a = h.a();
            if (a != null) {
                final String a2 = this.a(a);
                if (a2 != null) {
                    h.b(a2);
                    ac.e("Finished unzipping Embed player to " + a2);
                }
            }
        }
        else {
            this.f = true;
            this.g = "Error Processing Embed player: Unable to find player info for: " + this.e;
        }
        if (this.f) {
            ac.e(this.g);
            this.a(bf.b.c);
            return;
        }
        this.a(bf.b.b);
    }
    
    @Override
    public String toString() {
        return "JobProcessEmbedPlayer";
    }
}
