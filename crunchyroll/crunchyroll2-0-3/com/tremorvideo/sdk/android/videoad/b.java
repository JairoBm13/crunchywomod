// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.annotation.TargetApi;
import android.os.Environment;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection$OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.graphics.Bitmap;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.view.View;
import android.view.View$OnClickListener;
import android.content.Context;
import java.util.Dictionary;
import android.content.Intent;
import android.view.WindowManager;
import android.app.Activity;

public class b extends com.tremorvideo.sdk.android.videoad.a
{
    bw a;
    private ae b;
    private boolean e;
    private boolean f;
    private boolean g;
    
    public b(final com.tremorvideo.sdk.android.videoad.a.a a, final Activity activity, final n n, final boolean f) {
        super(a, activity);
        this.g = false;
        this.f = f;
        this.b = n.p();
        this.e = false;
        this.a = n.q();
    }
    
    private void a(final boolean b) {
        synchronized (this) {
            if (!this.g && !this.e) {
                this.e = true;
                if (b) {
                    int n;
                    if (this.f) {
                        n = this.d.a(this.b.a(aw.b.D));
                    }
                    else {
                        n = this.d.a(this.b.a(aw.b.E));
                    }
                    if (n != -1) {
                        this.d.a(n);
                    }
                }
                this.d.a(this);
            }
        }
    }
    
    private boolean e() {
        return this.b.a(aw.b.M) == null;
    }
    
    private int f() {
        return ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay().getWidth();
    }
    
    private void g() {
        synchronized (this) {
            this.f = false;
            final aw a = this.b.a(aw.b.M);
            final Dictionary<String, String> f = a.f();
            if (f.get("subject") != null || f.get("message") != null) {
                this.d.a(this.d.a(a));
                final Intent intent = new Intent("android.intent.action.SEND");
                if (f.get("subject") != null) {
                    intent.putExtra("android.intent.extra.SUBJECT", (String)f.get("subject"));
                }
                String string = "";
                if (f.get("message") != null) {
                    string = "" + f.get("message");
                }
                intent.putExtra("android.intent.extra.TEXT", string + "\n\n" + this.b.c());
                intent.setType("text/plain");
                this.c.startActivityForResult(Intent.createChooser(intent, (CharSequence)"Share"), 11);
            }
        }
    }
    
    private void h() {
        synchronized (this) {
            if (!this.g) {
                this.f = false;
                this.g = true;
                this.d.a(this.d.a(this.b.a(aw.b.A)));
                final an a = an.a((Context)this.c);
                a.setMessage((CharSequence)"Saving...");
                a.show();
                new a().execute((Object[])new Void[0]);
            }
        }
    }
    
    @Override
    public void a() {
        try {
            if (this.f) {
                final aw a = this.b.a(aw.b.C);
                if (a != null) {
                    this.d.a(this.d.a(a));
                }
            }
            aw.b[] array;
            if (this.b.a(aw.b.M) == null) {
                array = new aw.b[] { aw.b.A };
            }
            else {
                array = new aw.b[] { aw.b.A, aw.b.M };
            }
            final v v = new v((Context)this.c, (View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (view.getTag().equals(aw.b.A.c())) {
                        com.tremorvideo.sdk.android.videoad.b.this.h();
                        return;
                    }
                    if (view.getTag().equals(aw.b.M.c())) {
                        com.tremorvideo.sdk.android.videoad.b.this.g();
                        return;
                    }
                    com.tremorvideo.sdk.android.videoad.b.this.a(true);
                }
            }, array, this.a, true, "Done", this.f());
            final Bitmap a2 = bw.a(this.b.b());
            final ImageView imageView = new ImageView((Context)this.c);
            imageView.setImageBitmap(a2);
            imageView.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
            imageView.setFocusable(false);
            final FrameLayout contentView = new FrameLayout((Context)this.c);
            contentView.addView((View)imageView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -2);
            frameLayout$LayoutParams.gravity = 80;
            contentView.addView(v.d(), (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            this.c.setContentView((View)contentView);
            if (this.b.a() > 0 && this.f) {
                contentView.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (!com.tremorvideo.sdk.android.videoad.b.this.e && com.tremorvideo.sdk.android.videoad.b.this.f) {
                            com.tremorvideo.sdk.android.videoad.b.this.a(true);
                        }
                    }
                }, (long)this.b.a());
            }
        }
        catch (Exception ex) {
            ac.a(ex);
            this.d.a(this);
        }
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            this.a(true);
            return true;
        }
        return super.a(n, keyEvent);
    }
    
    @Override
    public com.tremorvideo.sdk.android.videoad.a.b n() {
        return com.tremorvideo.sdk.android.videoad.a.b.d;
    }
    
    class a extends AsyncTask<Void, Void, Boolean>
    {
        final /* synthetic */ an a;
        
        a(final an a) {
            this.a = a;
        }
        
        private String a(final Context p0, final Uri p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_1        
            //     1: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
            //     4: aload_2        
            //     5: iconst_1       
            //     6: anewarray       Ljava/lang/String;
            //     9: dup            
            //    10: iconst_0       
            //    11: ldc             "_data"
            //    13: aastore        
            //    14: aconst_null    
            //    15: aconst_null    
            //    16: aconst_null    
            //    17: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
            //    20: astore_1       
            //    21: aload_1        
            //    22: ldc             "_data"
            //    24: invokeinterface android/database/Cursor.getColumnIndexOrThrow:(Ljava/lang/String;)I
            //    29: istore_3       
            //    30: aload_1        
            //    31: invokeinterface android/database/Cursor.moveToFirst:()Z
            //    36: pop            
            //    37: aload_1        
            //    38: iload_3        
            //    39: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
            //    44: astore_2       
            //    45: aload_1        
            //    46: ifnull          55
            //    49: aload_1        
            //    50: invokeinterface android/database/Cursor.close:()V
            //    55: aload_2        
            //    56: areturn        
            //    57: astore_2       
            //    58: aconst_null    
            //    59: astore_1       
            //    60: aload_1        
            //    61: ifnull          70
            //    64: aload_1        
            //    65: invokeinterface android/database/Cursor.close:()V
            //    70: aload_2        
            //    71: athrow         
            //    72: astore_2       
            //    73: goto            60
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  0      21     57     60     Any
            //  21     45     72     76     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0055:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
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
        
        @TargetApi(8)
        protected Boolean a(final Void... array) {
            while (true) {
                final boolean b = true;
                boolean b2 = true;
            Label_0099:
                while (true) {
                    while (true) {
                        try {
                            final String a = com.tremorvideo.sdk.android.videoad.b.this.b.a((Context)com.tremorvideo.sdk.android.videoad.b.this.c);
                            if (a != null && a.length() > 0) {
                                while (true) {
                                    try {
                                        if (ac.r() >= 8) {
                                            MediaScannerConnection.scanFile((Context)com.tremorvideo.sdk.android.videoad.b.this.c, new String[] { this.a((Context)com.tremorvideo.sdk.android.videoad.b.this.c, Uri.parse(a)) }, (String[])null, (MediaScannerConnection$OnScanCompletedListener)new MediaScannerConnection$OnScanCompletedListener() {
                                                public void onScanCompleted(final String s, final Uri uri) {
                                                    ac.e("Storage Scan Completed uri = " + uri);
                                                }
                                            });
                                        }
                                        else {
                                            com.tremorvideo.sdk.android.videoad.b.this.c.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                                        }
                                        return b2;
                                    }
                                    catch (Exception ex) {
                                        b2 = b;
                                    }
                                    final Exception ex;
                                    ac.a(ex);
                                    continue Label_0099;
                                }
                            }
                        }
                        catch (Exception ex) {
                            b2 = false;
                            continue;
                        }
                        break;
                    }
                    b2 = false;
                    continue Label_0099;
                }
            }
        }
        
        protected void a(final Boolean b) {
            this.a.dismiss();
            final aa aa = new aa((Context)b.this.c, b.this.a, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                @Override
                public void a(final boolean b) {
                }
            });
            if (b) {
                aa.a("The coupon has been saved to the gallery.");
                aa.setTitle("Save Complete");
            }
            else {
                aa.a("An error occurred while saving.");
                aa.setTitle("Save Error");
            }
            aa.a("Ok", "");
            aa.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                public void onDismiss(final DialogInterface dialogInterface) {
                    com.tremorvideo.sdk.android.videoad.b.this.g = false;
                    if (com.tremorvideo.sdk.android.videoad.b.this.e()) {
                        com.tremorvideo.sdk.android.videoad.b.this.a(false);
                    }
                }
            });
            aa.setCanceledOnTouchOutside(false);
            aa.show();
        }
    }
}
