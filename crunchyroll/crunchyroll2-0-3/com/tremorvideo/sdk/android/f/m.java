// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import com.tremorvideo.sdk.android.videoad.bx;
import android.webkit.URLUtil;
import android.content.Intent;
import android.os.Environment;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection$OnScanCompletedListener;
import com.tremorvideo.sdk.android.videoad.ac;
import android.provider.MediaStore$Images$Media;
import android.graphics.BitmapFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.net.Uri;
import android.content.Context;

class m extends b
{
    m(final s s) {
        super(s);
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
    
    private void b(final Context context, final Uri uri) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
        alertDialog$Builder.setTitle((CharSequence)"Store Picture");
        alertDialog$Builder.setMessage((CharSequence)"Save this image to your Photo Gallery?");
        alertDialog$Builder.setPositiveButton((CharSequence)"Save Image", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String string = uri.toString();
                                final BasicHttpParams basicHttpParams = new BasicHttpParams();
                                HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 18000);
                                HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 18000);
                                ((HttpParams)basicHttpParams).setBooleanParameter("http.protocol.expect-continue", false);
                                ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_0);
                                final String insertImage = MediaStore$Images$Media.insertImage(context.getContentResolver(), BitmapFactory.decodeStream(new BufferedHttpEntity(new DefaultHttpClient((HttpParams)basicHttpParams).execute((HttpUriRequest)new HttpGet(string)).getEntity()).getContent()), "Image", "Image");
                                if (insertImage != null && insertImage.length() > 0) {
                                    if (ac.r() >= 8) {
                                        MediaScannerConnection.scanFile(context, new String[] { m.this.a(context, Uri.parse(insertImage)) }, (String[])null, (MediaScannerConnection$OnScanCompletedListener)new MediaScannerConnection$OnScanCompletedListener() {
                                            public void onScanCompleted(final String s, final Uri uri) {
                                            }
                                        });
                                        return;
                                    }
                                    context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                                }
                            }
                            catch (Exception ex) {
                                ac.e("TremorLog_error::MRAID::Save Picture " + ex);
                            }
                        }
                    }).start();
                }
                catch (Exception ex) {
                    ac.e("TremorLog_error::MRAID::Save Picture Thread " + ex);
                }
            }
        });
        alertDialog$Builder.setNegativeButton((CharSequence)"Cancel", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                ac.e("TremorLog_error::MRAID::The user canceled the action., action = storePicture");
                dialogInterface.cancel();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    protected void a(final String s) {
        final s a = this.a();
        if (a.k() != null) {
            a.k().a(a);
        }
        ac.e("TremorLog_info::MRAID::open URL=" + s);
        if (URLUtil.isValidUrl(s) || s.startsWith("market://") || s.startsWith("tel://")) {
            this.a().getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
            bx.c();
            return;
        }
        a.a("Attempting to open invalid url: ", s);
    }
    
    protected void b(final String s) {
        final s a = this.a();
        if (a.k() != null) {
            a.k().a(a);
        }
        ac.e("TremorLog_info::MRAID::playVideo URL=" + s);
        if (URLUtil.isValidUrl(s)) {
            final Context context = this.a().getContext();
            final Uri parse = Uri.parse(s);
            final Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.setDataAndType(parse, "video/*");
            context.startActivity(intent);
            bx.c();
            return;
        }
        a.a("Attempting to open invalid video url ", s);
    }
    
    protected void c(final String s) {
        final s a = this.a();
        ac.e("TremorLog_info::MRAID::savePicture URL=" + s);
        if (URLUtil.isValidUrl(s)) {
            this.b(this.a().getContext(), Uri.parse(s));
            return;
        }
        a.a("Attempting to save invalid picture url ", s);
    }
}
