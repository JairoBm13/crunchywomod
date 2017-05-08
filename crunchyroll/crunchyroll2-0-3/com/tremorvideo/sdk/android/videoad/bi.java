// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.Intent;
import java.util.List;
import java.net.SocketTimeoutException;
import com.tremorvideo.sdk.android.logger.TestAppLogger;
import org.json.JSONObject;

public class bi extends bf
{
    JSONObject a;
    String b;
    String c;
    int d;
    boolean e;
    int f;
    String g;
    boolean h;
    
    public bi(final a a, final String g, final String b, final boolean h) {
        super(a);
        this.f = 1000;
        this.e = false;
        this.b = b;
        this.a = null;
        this.g = g;
        this.h = h;
    }
    
    public static void a(final boolean b, final String s) {
        try {
            if (ac.r) {
                final TestAppLogger instance = TestAppLogger.getInstance();
                String s2;
                if (b) {
                    s2 = "streaming=true";
                }
                else {
                    s2 = "streaming=false";
                }
                instance.logRequestAd(s2, "request_url:" + s, "pass");
            }
        }
        catch (Exception ex) {
            ac.e("Error logRequestAd" + ex);
        }
    }
    
    public static void a(final boolean b, final String s, final String s2) {
        try {
            if (ac.r) {
                final TestAppLogger instance = TestAppLogger.getInstance();
                String s3;
                if (b) {
                    s3 = "streaming=true";
                }
                else {
                    s3 = "streaming=false";
                }
                instance.logRequestAd(s3, "request_url:" + s + ", " + s2, "fail");
            }
        }
        catch (Exception ex) {
            ac.e("Error logRequestAd" + ex);
        }
    }
    
    private void j() {
        final boolean b = false;
        if (this.a == null) {
            this.a = z.a(at.a(), ac.v(), new x(ac.x().getFilesDir().getAbsoluteFile()).e(), TremorVideo.a.a, this.g, this.h);
        }
        while (true) {
            this.d = 0;
        Label_0275_Outer:
            while (true) {
                Label_0608: {
                Block_18_Outer:
                    while (true) {
                        Label_0439: {
                            try {
                                final String z = ac.z();
                                ac.e("User Agent: " + z);
                                ac.e("CCH: " + this.g);
                                final String string = this.a.toString();
                                ac.e("DOWNLOAD REQUEST URL=" + this.b);
                                final bb a = bb.a(this.b, z, string, ac.e);
                                a.a();
                                this.c = a.b();
                                if (this.c == null || this.c == "") {
                                    break Label_0608;
                                }
                                final String[] split = new JSONObject(this.c).toString(2).split("\n");
                                for (int length = split.length, i = 0; i < length; ++i) {
                                    ac.a(ac.c.c, split[i]);
                                }
                                final int n = 1;
                                if (this.d != 408) {
                                    break Block_18_Outer;
                                }
                                this.f = Math.min(120000, this.f * 2);
                                if (this.d != 1) {
                                    break;
                                }
                                final bi bi = this;
                                final String s = bi.c;
                                final bi bi2 = this;
                                final String s2 = bi2.g;
                                final boolean b2 = false;
                                final boolean b3 = true;
                                final bs bs = new bs(s, s2, b2, b3);
                                final List<n> list = bs.b();
                                final boolean b4 = list.isEmpty();
                                if (b4) {
                                    final bi bi3 = this;
                                    final boolean b5 = bi3.h;
                                    final bi bi4 = this;
                                    final String s3 = bi4.b;
                                    final String s4 = "AD Empty";
                                    a(b5, s3, s4);
                                    final bi bi5 = this;
                                    final b b6 = bf.b.c;
                                    bi5.a(b6);
                                    return;
                                }
                                break Label_0439;
                            }
                            catch (SocketTimeoutException ex) {
                                this.d = 408;
                                ac.a("Error Downloading Request: ", ex);
                                final int n = b ? 1 : 0;
                                continue Label_0275_Outer;
                            }
                            catch (Exception ex2) {
                                ac.a("Error Downloading Request: ", ex2);
                                final int n = b ? 1 : 0;
                                continue Label_0275_Outer;
                            }
                            try {
                                final bi bi = this;
                                final String s = bi.c;
                                final bi bi2 = this;
                                final String s2 = bi2.g;
                                final boolean b2 = false;
                                final boolean b3 = true;
                                final bs bs = new bs(s, s2, b2, b3);
                                final List<n> list = bs.b();
                                final boolean b4 = list.isEmpty();
                                if (b4) {
                                    final bi bi3 = this;
                                    final boolean b5 = bi3.h;
                                    final bi bi4 = this;
                                    final String s3 = bi4.b;
                                    final String s4 = "AD Empty";
                                    a(b5, s3, s4);
                                    final bi bi5 = this;
                                    final b b6 = bf.b.c;
                                    bi5.a(b6);
                                    return;
                                }
                                a(this.h, this.b);
                                this.i();
                                this.a(bf.b.b);
                                return;
                                // iftrue(Label_0396:, n != 0)
                                while (true) {
                                    this.d = 500;
                                    this.f = Math.min(120000, this.f * 2);
                                    continue Block_18_Outer;
                                    continue;
                                }
                                // iftrue(Label_0431:, this.c.length() != 0)
                                Block_19: {
                                    break Block_19;
                                    Label_0431: {
                                        this.d = 1;
                                    }
                                    continue Block_18_Outer;
                                }
                                this.d = 204;
                                this.f = Math.min(120000, this.f * 2);
                                continue Block_18_Outer;
                            }
                            catch (Exception ex3) {
                                ac.a(ex3);
                                a(this.h, this.b, "Exception Request creation: " + ex3.getMessage());
                                this.a(bf.b.d);
                                return;
                            }
                        }
                        break;
                    }
                    break;
                }
                final int n = 0;
                continue;
            }
        }
        if (this.d == 408) {
            a(this.h, this.b, "SocketTimeoutException ");
            this.a(bf.b.f);
            return;
        }
        ac.e("Request returned: " + this.d);
        a(this.h, this.b, "Error:Request returned: " + this.d);
        this.a(bf.b.c);
    }
    
    @Override
    protected void e() {
        this.j();
    }
    
    @Override
    protected void f() {
    }
    
    @Override
    protected void g() {
        this.j();
    }
    
    public bs h() {
        try {
            return new bs(this.c, this.g, false, true);
        }
        catch (Exception ex) {
            ac.a(ex);
            return null;
        }
    }
    
    public void i() {
        try {
            if (ac.r) {
                final String string = new JSONObject(this.c).getJSONArray("ad").getJSONObject(0).getString("id");
                String substring;
                if ((substring = string) != null) {
                    substring = string;
                    if (string.length() > 0) {
                        final int index = string.indexOf("?");
                        substring = string;
                        if (index != -1) {
                            substring = string.substring(0, index);
                        }
                        final Intent intent = new Intent();
                        intent.setAction("com.tremorvideo.logger.BroadcastCreativeID");
                        intent.putExtra("creativeid", substring);
                        ac.x().sendBroadcast(intent);
                    }
                }
                TestAppLogger.getInstance().logCreative("creative_id:" + substring, "creative_id:" + substring, "info");
            }
        }
        catch (Exception ex) {
            ac.e("Error logCreativeID" + ex);
        }
    }
    
    @Override
    public String toString() {
        return "Download Request";
    }
}
