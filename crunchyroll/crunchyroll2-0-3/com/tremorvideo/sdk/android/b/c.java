// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.b;

import org.json.JSONArray;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import android.content.Context;
import java.util.Iterator;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.tremorvideo.sdk.android.videoad.r;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.videoad.aw;
import java.util.List;
import java.io.File;

public class c
{
    public String a;
    public String b;
    protected int c;
    private File d;
    private String e;
    private String f;
    private long g;
    private int h;
    private List<aw> i;
    private File j;
    
    public c(JSONObject jsonArray, r.b o) throws Exception {
        this.a = "";
        this.b = "";
        this.c = 1;
        this.e = jsonArray.getString("tms-xml-url");
        if (this.e.contains("http://data.tmsapi.com/v1/")) {
            this.c = 2;
        }
        if (o != null && ((r.b)o).d != null && ((r.b)o).d.length() > 0) {
            this.a = ((r.b)o).d;
        }
        Object o2;
        o = (o2 = new Date());
    Label_0161:
        while (true) {
            if (!jsonArray.has("movie-date")) {
                break Label_0161;
            }
            final String string = jsonArray.getString("movie-date");
            o2 = o;
            if (string == null) {
                break Label_0161;
            }
            o2 = o;
            if (string.length() <= 0) {
                break Label_0161;
            }
            while (true) {
                Date parse;
                Label_0216_Outer:Label_0239_Outer:
                while (true) {
                    Label_0389: {
                    Label_0328:
                        while (true) {
                        Label_0320:
                            while (true) {
                            Label_0312:
                                while (true) {
                                    try {
                                        parse = new SimpleDateFormat("yyyy-MM-dd").parse(string);
                                        if (!parse.after((Date)o)) {
                                            break Label_0389;
                                        }
                                        o = parse;
                                        o2 = o;
                                        if (this.c == 2) {
                                            this.b = new SimpleDateFormat("yyyy-MM-dd").format((Date)o2);
                                            this.f = jsonArray.getString("template-url");
                                            if (!jsonArray.has("template-crc32")) {
                                                break Label_0312;
                                            }
                                            this.g = jsonArray.getLong("template-crc32");
                                            if (!jsonArray.has("auto-skip-seconds")) {
                                                break Label_0320;
                                            }
                                            this.h = jsonArray.getInt("auto-skip-seconds") * 1000;
                                            if (!jsonArray.has("events")) {
                                                this.i = new ArrayList<aw>();
                                                return;
                                            }
                                            break Label_0328;
                                        }
                                    }
                                    catch (Exception ex) {
                                        ac.e("Invalid TMS date: " + string);
                                        o2 = o;
                                        continue Label_0161;
                                    }
                                    this.b = new SimpleDateFormat("yyyyMMdd").format((Date)o2);
                                    continue Label_0216_Outer;
                                }
                                this.g = 0L;
                                continue Label_0239_Outer;
                            }
                            this.h = 0;
                            continue;
                        }
                        jsonArray = (JSONObject)jsonArray.getJSONArray("events");
                        this.i = new ArrayList<aw>(((JSONArray)jsonArray).length());
                        for (int i = 0; i < ((JSONArray)jsonArray).length(); ++i) {
                            this.i.add(new aw(((JSONArray)jsonArray).getJSONObject(i)));
                        }
                        return;
                    }
                    continue;
                }
            }
            break;
        }
    }
    
    private void a(final String s, final String s2) {
        final File file = new File(s2 + s);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }
    
    public int a() {
        return this.h;
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.i) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
    
    public void a(final Context context) {
        FileInputStream fileInputStream = null;
        ZipInputStream zipInputStream = null;
    Label_0311:
        while (true) {
            while (true) {
                String string;
                String name;
                try {
                    fileInputStream = new FileInputStream(this.j);
                    zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
                    this.d = new File(context.getFilesDir() + "/" + "MovieBoard");
                    if (this.d.exists()) {
                        ae.a(this.d);
                    }
                    this.d.mkdir();
                    string = this.d.getAbsolutePath() + "/";
                    while (true) {
                        final ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry == null) {
                            break Label_0311;
                        }
                        name = nextEntry.getName();
                        if (!nextEntry.isDirectory()) {
                            break;
                        }
                        this.a(name, string);
                    }
                }
                catch (IOException ex) {
                    ac.a(ex);
                    return;
                }
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final byte[] array = new byte[1024];
                final File file = new File(string + name);
                if (!file.exists()) {
                    file.createNewFile();
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
    }
    
    public void a(final File j) {
        this.j = j;
    }
    
    public String b() {
        return this.e;
    }
    
    public String c() {
        return this.f;
    }
    
    public Long d() {
        return this.g;
    }
    
    public void e() {
        if ("MovieBoard" != null) {
            ae.a(this.d);
        }
    }
    
    public File f() {
        return this.d;
    }
}
