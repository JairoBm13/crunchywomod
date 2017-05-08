// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad.a;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Collections;
import java.util.Comparator;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.List;
import org.xml.sax.XMLReader;
import java.io.Reader;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.xml.sax.ContentHandler;
import javax.xml.parsers.SAXParserFactory;

public class a
{
    a a;
    c b;
    
    public a(final String s) throws Exception {
        String trim = s;
        if (s != null) {
            trim = s.trim();
        }
        final XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(new a());
        xmlReader.parse(new InputSource(new StringReader(trim)));
        this.a = (a)xmlReader.getContentHandler();
        if (!this.a.h) {
            if (this.a.e.size() > 0) {
                this.b = this.a(this.a.e);
            }
            else {
                this.b = this.a(this.a.f);
            }
            if (this.b == null) {
                throw new Exception("No valid media file found.");
            }
            if (this.b.a <= 0) {
                throw new Exception("Invalid width: " + this.b.a);
            }
            if (this.b.b <= 0) {
                throw new Exception("Invalid height: " + this.b.b);
            }
        }
    }
    
    public c a(final List<c> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<c>() {
            final /* synthetic */ int a = Math.max(ac.m(), ac.n());
            final /* synthetic */ int b = Math.min(ac.m(), ac.n());
            
            public int a(final c c, final c c2) {
                final int a = this.a;
                final int a2 = c.a;
                final int b = this.b;
                final int b2 = c.b;
                final int a3 = this.a;
                final int a4 = c2.a;
                final int b3 = this.b;
                final int b4 = c2.b;
                final int max = Math.max(Math.abs(a - a2), Math.abs(b - b2));
                final int max2 = Math.max(Math.abs(a3 - a4), Math.abs(b3 - b4));
                if (max == max2) {
                    return c2.e - c.e;
                }
                return max - max2;
            }
        });
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    public List<com.tremorvideo.sdk.android.videoad.a.b> a() {
        return this.a.c;
    }
    
    public boolean b() {
        return this.a.h;
    }
    
    public String c() {
        return this.b.c;
    }
    
    public String d() {
        return this.a.i;
    }
    
    public int e() {
        return this.b.a;
    }
    
    public int f() {
        return this.b.b;
    }
    
    public String g() {
        return this.a.d;
    }
    
    public int h() {
        return this.a.j;
    }
    
    public boolean i() {
        return this.b != null && this.b.d.equalsIgnoreCase("application/x-mpegURL");
    }
    
    private class a extends DefaultHandler
    {
        b a;
        boolean b;
        List<com.tremorvideo.sdk.android.videoad.a.b> c;
        String d;
        List<c> e;
        List<c> f;
        c g;
        boolean h;
        String i;
        int j;
        StringBuilder k;
        
        private a() {
            this.a = com.tremorvideo.sdk.android.videoad.a.a.b.l;
            this.b = false;
            this.c = new ArrayList<com.tremorvideo.sdk.android.videoad.a.b>();
            this.e = new ArrayList<c>();
            this.f = new ArrayList<c>();
            this.k = new StringBuilder();
        }
        
        private b a(final String s) {
            try {
                return com.tremorvideo.sdk.android.videoad.a.a.b.valueOf(s);
            }
            catch (Exception ex) {
                return com.tremorvideo.sdk.android.videoad.a.a.b.l;
            }
        }
        
        private boolean b(final String s) {
            return !s.toLowerCase().endsWith(".flv");
        }
        
        private boolean c(final String s) {
            return s.equalsIgnoreCase("video/3gp") || s.equalsIgnoreCase("video/3gpp") || s.equalsIgnoreCase("video/mp4") || (!ac.h && s.equalsIgnoreCase("application/x-mpegURL"));
        }
        
        @Override
        public void characters(final char[] array, final int n, final int n2) throws SAXException {
            this.k.append(array, n, n2);
        }
        
        @Override
        public void endElement(String trim, final String s, final String s2) throws SAXException {
            if (this.a(s) == com.tremorvideo.sdk.android.videoad.a.a.b.d) {
                this.b = false;
            }
            else {
                trim = this.k.toString().trim();
                if (trim.length() > 0) {
                    if (this.b) {
                        if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.f) {
                            this.c.get(this.c.size() - 1).b = trim;
                        }
                        else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.g) {
                            this.d = trim;
                        }
                        else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.h) {
                            this.c.add(new com.tremorvideo.sdk.android.videoad.a.b("click", trim));
                        }
                        else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.i) {
                            if (this.c(this.g.d) && this.b(trim)) {
                                this.g.c = trim;
                                if (this.g.d.equalsIgnoreCase("application/x-mpegURL")) {
                                    this.f.add(this.g);
                                }
                                else {
                                    this.e.add(this.g);
                                }
                            }
                            this.g = null;
                        }
                        else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.e) {
                            final String[] split = trim.split(":");
                            this.j = 0;
                            this.j += Math.round(Float.parseFloat(split[0]) * 60.0f * 60.0f * 1000.0f);
                            this.j += Math.round(Float.parseFloat(split[1]) * 60.0f * 1000.0f);
                            this.j += Math.round(Float.parseFloat(split[2]) * 1000.0f);
                        }
                    }
                    else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.c) {
                        this.c.add(new com.tremorvideo.sdk.android.videoad.a.b("impression", trim));
                    }
                    else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.k) {
                        this.i = trim;
                    }
                }
            }
            this.k.setLength(0);
        }
        
        @Override
        public void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
            this.a = this.a(s2);
            if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.a) {
                final float float1 = Float.parseFloat(attributes.getValue("version").split("\\.")[0]);
                if (float1 < 2.0 || float1 >= 3.0) {
                    throw new SAXException("Invalid VAST Version: " + attributes.getValue("version"));
                }
            }
            else if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.d) {
                this.b = true;
            }
            else {
                if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.j) {
                    this.h = true;
                    return;
                }
                if (this.b) {
                    if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.f) {
                        this.c.add(new com.tremorvideo.sdk.android.videoad.a.b(attributes.getValue("event")));
                        return;
                    }
                    if (this.a == com.tremorvideo.sdk.android.videoad.a.a.b.i) {
                        this.g = new c();
                        if (attributes.getValue("width") == null || attributes.getValue("height") == null) {
                            this.g.a = 640;
                            this.g.b = 360;
                        }
                        else {
                            this.g.a = Integer.parseInt(attributes.getValue("width"));
                            this.g.b = Integer.parseInt(attributes.getValue("height"));
                        }
                        if (attributes.getValue("bitrate") == null) {
                            this.g.e = 500;
                        }
                        else {
                            this.g.e = Integer.parseInt(attributes.getValue("bitrate"));
                        }
                        this.g.d = attributes.getValue("type");
                    }
                }
            }
        }
    }
    
    private enum b
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i, 
        j, 
        k, 
        l;
    }
    
    class c
    {
        int a;
        int b;
        String c;
        String d;
        int e;
    }
}
