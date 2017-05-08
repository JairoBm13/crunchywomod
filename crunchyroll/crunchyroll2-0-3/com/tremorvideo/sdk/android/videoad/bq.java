// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.io.IOException;
import java.util.TimeZone;
import java.util.Locale;
import java.util.StringTokenizer;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.io.PrintStream;

public class bq
{
    protected static PrintStream a;
    private static Hashtable d;
    private static int e;
    private static SimpleDateFormat f;
    private final ServerSocket b;
    private Thread c;
    
    static {
        bq.d = new Hashtable();
        final StringTokenizer stringTokenizer = new StringTokenizer("css\t\ttext/css htm\t\ttext/html html\t\ttext/html xml\t\ttext/xml txt\t\ttext/plain asc\t\ttext/plain gif\t\timage/gif jpg\t\timage/jpeg jpeg\t\timage/jpeg png\t\timage/png mp3\t\taudio/mpeg m3u\t\taudio/mpeg-url mp4\t\tvideo/mp4 ogv\t\tvideo/ogg flv\t\tvideo/x-flv mov\t\tvideo/quicktime swf\t\tapplication/x-shockwave-flash js\t\t\tapplication/javascript pdf\t\tapplication/pdf doc\t\tapplication/msword ogg\t\tapplication/x-ogg zip\t\tapplication/octet-stream exe\t\tapplication/octet-stream class\t\tapplication/octet-stream ");
        while (stringTokenizer.hasMoreTokens()) {
            bq.d.put(stringTokenizer.nextToken(), stringTokenizer.nextToken());
        }
        bq.e = 16384;
        bq.a = System.out;
        (bq.f = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US)).setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public void a() {
        try {
            this.b.close();
            this.c.join();
        }
        catch (InterruptedException ex) {}
        catch (IOException ex2) {}
    }
}
