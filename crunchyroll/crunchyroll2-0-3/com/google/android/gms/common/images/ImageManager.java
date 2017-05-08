// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.images;

import com.google.android.gms.common.internal.zzb;
import android.os.SystemClock;
import java.util.concurrent.CountDownLatch;
import android.graphics.Bitmap;
import com.google.android.gms.internal.zzku;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import java.util.ArrayList;
import android.os.ResultReceiver;
import java.util.Map;
import com.google.android.gms.internal.zzkj;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.content.Context;
import android.net.Uri;
import java.util.HashSet;

public final class ImageManager
{
    private static final Object zzYN;
    private static HashSet<Uri> zzYO;
    private final Context mContext;
    private final Handler mHandler;
    private final ExecutorService zzYR;
    private final zzb zzYS;
    private final zzkj zzYT;
    private final Map<zza, ImageReceiver> zzYU;
    private final Map<Uri, ImageReceiver> zzYV;
    private final Map<Uri, Long> zzYW;
    
    static {
        zzYN = new Object();
        ImageManager.zzYO = new HashSet<Uri>();
    }
    
    private final class ImageReceiver extends ResultReceiver
    {
        private final Uri mUri;
        private final ArrayList<zza> zzYX;
        final /* synthetic */ ImageManager zzYY;
        
        public void onReceiveResult(final int n, final Bundle bundle) {
            this.zzYY.zzYR.execute(this.zzYY.new zzc(this.mUri, (ParcelFileDescriptor)bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }
    
    public interface OnImageLoadedListener
    {
        void onImageLoaded(final Uri p0, final Drawable p1, final boolean p2);
    }
    
    private static final class zzb extends zzku<zza.zza, Bitmap>
    {
        protected int zza(final zza.zza zza, final Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
        
        protected void zza(final boolean b, final zza.zza zza, final Bitmap bitmap, final Bitmap bitmap2) {
            super.entryRemoved(b, zza, bitmap, bitmap2);
        }
    }
    
    private final class zzc implements Runnable
    {
        private final Uri mUri;
        private final ParcelFileDescriptor zzYZ;
        
        public zzc(final Uri mUri, final ParcelFileDescriptor zzYZ) {
            this.mUri = mUri;
            this.zzYZ = zzYZ;
        }
        
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: ldc             "LoadBitmapFromDiskRunnable can't be executed in the main thread"
            //     2: invokestatic    com/google/android/gms/common/internal/zzb.zzbZ:(Ljava/lang/String;)V
            //     5: iconst_0       
            //     6: istore_1       
            //     7: iconst_0       
            //     8: istore_2       
            //     9: aconst_null    
            //    10: astore_3       
            //    11: aconst_null    
            //    12: astore          4
            //    14: aload_0        
            //    15: getfield        com/google/android/gms/common/images/ImageManager$zzc.zzYZ:Landroid/os/ParcelFileDescriptor;
            //    18: ifnull          41
            //    21: aload_0        
            //    22: getfield        com/google/android/gms/common/images/ImageManager$zzc.zzYZ:Landroid/os/ParcelFileDescriptor;
            //    25: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
            //    28: invokestatic    android/graphics/BitmapFactory.decodeFileDescriptor:(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;
            //    31: astore_3       
            //    32: iload_2        
            //    33: istore_1       
            //    34: aload_0        
            //    35: getfield        com/google/android/gms/common/images/ImageManager$zzc.zzYZ:Landroid/os/ParcelFileDescriptor;
            //    38: invokevirtual   android/os/ParcelFileDescriptor.close:()V
            //    41: new             Ljava/util/concurrent/CountDownLatch;
            //    44: dup            
            //    45: iconst_1       
            //    46: invokespecial   java/util/concurrent/CountDownLatch.<init>:(I)V
            //    49: astore          4
            //    51: aload_0        
            //    52: getfield        com/google/android/gms/common/images/ImageManager$zzc.zzYY:Lcom/google/android/gms/common/images/ImageManager;
            //    55: invokestatic    com/google/android/gms/common/images/ImageManager.zzg:(Lcom/google/android/gms/common/images/ImageManager;)Landroid/os/Handler;
            //    58: new             Lcom/google/android/gms/common/images/ImageManager$zzf;
            //    61: dup            
            //    62: aload_0        
            //    63: getfield        com/google/android/gms/common/images/ImageManager$zzc.zzYY:Lcom/google/android/gms/common/images/ImageManager;
            //    66: aload_0        
            //    67: getfield        com/google/android/gms/common/images/ImageManager$zzc.mUri:Landroid/net/Uri;
            //    70: aload_3        
            //    71: iload_1        
            //    72: aload           4
            //    74: invokespecial   com/google/android/gms/common/images/ImageManager$zzf.<init>:(Lcom/google/android/gms/common/images/ImageManager;Landroid/net/Uri;Landroid/graphics/Bitmap;ZLjava/util/concurrent/CountDownLatch;)V
            //    77: invokevirtual   android/os/Handler.post:(Ljava/lang/Runnable;)Z
            //    80: pop            
            //    81: aload           4
            //    83: invokevirtual   java/util/concurrent/CountDownLatch.await:()V
            //    86: return         
            //    87: astore_3       
            //    88: ldc             "ImageManager"
            //    90: new             Ljava/lang/StringBuilder;
            //    93: dup            
            //    94: invokespecial   java/lang/StringBuilder.<init>:()V
            //    97: ldc             "OOM while loading bitmap for uri: "
            //    99: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   102: aload_0        
            //   103: getfield        com/google/android/gms/common/images/ImageManager$zzc.mUri:Landroid/net/Uri;
            //   106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   109: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   112: aload_3        
            //   113: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   116: pop            
            //   117: iconst_1       
            //   118: istore_1       
            //   119: aload           4
            //   121: astore_3       
            //   122: goto            34
            //   125: astore          4
            //   127: ldc             "ImageManager"
            //   129: ldc             "closed failed"
            //   131: aload           4
            //   133: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   136: pop            
            //   137: goto            41
            //   140: astore_3       
            //   141: ldc             "ImageManager"
            //   143: new             Ljava/lang/StringBuilder;
            //   146: dup            
            //   147: invokespecial   java/lang/StringBuilder.<init>:()V
            //   150: ldc             "Latch interrupted while posting "
            //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   155: aload_0        
            //   156: getfield        com/google/android/gms/common/images/ImageManager$zzc.mUri:Landroid/net/Uri;
            //   159: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   162: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   165: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
            //   168: pop            
            //   169: return         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                            
            //  -----  -----  -----  -----  --------------------------------
            //  21     32     87     125    Ljava/lang/OutOfMemoryError;
            //  34     41     125    140    Ljava/io/IOException;
            //  81     86     140    170    Ljava/lang/InterruptedException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 86, Size: 86
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
    }
    
    private final class zzf implements Runnable
    {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private boolean zzZb;
        private final CountDownLatch zzoD;
        
        public zzf(final Uri mUri, final Bitmap mBitmap, final boolean zzZb, final CountDownLatch zzoD) {
            this.mUri = mUri;
            this.mBitmap = mBitmap;
            this.zzZb = zzZb;
            this.zzoD = zzoD;
        }
        
        private void zza(final ImageReceiver imageReceiver, final boolean b) {
            final ArrayList zza = imageReceiver.zzYX;
            for (int size = zza.size(), i = 0; i < size; ++i) {
                final zza zza2 = zza.get(i);
                if (b) {
                    zza2.zza(ImageManager.this.mContext, this.mBitmap, false);
                }
                else {
                    ImageManager.this.zzYW.put(this.mUri, SystemClock.elapsedRealtime());
                    zza2.zza(ImageManager.this.mContext, ImageManager.this.zzYT, false);
                }
                if (!(zza2 instanceof zza.zzc)) {
                    ImageManager.this.zzYU.remove(zza2);
                }
            }
        }
        
        @Override
        public void run() {
            com.google.android.gms.common.internal.zzb.zzbY("OnBitmapLoadedRunnable must be executed in the main thread");
            final boolean b = this.mBitmap != null;
            if (ImageManager.this.zzYS != null) {
                if (this.zzZb) {
                    ImageManager.this.zzYS.evictAll();
                    System.gc();
                    this.zzZb = false;
                    ImageManager.this.mHandler.post((Runnable)this);
                    return;
                }
                if (b) {
                    ImageManager.this.zzYS.put(new zza.zza(this.mUri), this.mBitmap);
                }
            }
            final ImageReceiver imageReceiver = ImageManager.this.zzYV.remove(this.mUri);
            if (imageReceiver != null) {
                this.zza(imageReceiver, b);
            }
            this.zzoD.countDown();
            synchronized (ImageManager.zzYN) {
                ImageManager.zzYO.remove(this.mUri);
            }
        }
    }
}
