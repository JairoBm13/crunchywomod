// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.os.RemoteException;
import android.os.Parcel;
import android.os.IInterface;
import android.content.ComponentName;
import java.util.concurrent.TimeUnit;
import io.fabric.sdk.android.Fabric;
import android.os.IBinder;
import java.util.concurrent.LinkedBlockingQueue;
import android.content.ServiceConnection;
import android.content.Context;

class AdvertisingInfoServiceStrategy implements AdvertisingInfoStrategy
{
    private final Context context;
    
    public AdvertisingInfoServiceStrategy(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    @Override
    public AdvertisingInfo getAdvertisingInfo() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    android/os/Looper.myLooper:()Landroid/os/Looper;
        //     3: invokestatic    android/os/Looper.getMainLooper:()Landroid/os/Looper;
        //     6: if_acmpne       23
        //     9: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    12: ldc             "Fabric"
        //    14: ldc             "AdvertisingInfoServiceStrategy cannot be called on the main thread"
        //    16: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //    21: aconst_null    
        //    22: areturn        
        //    23: aload_0        
        //    24: getfield        io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy.context:Landroid/content/Context;
        //    27: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    30: ldc             "com.android.vending"
        //    32: iconst_0       
        //    33: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //    36: pop            
        //    37: new             Lio/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingConnection;
        //    40: dup            
        //    41: aconst_null    
        //    42: invokespecial   io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingConnection.<init>:(Lio/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$1;)V
        //    45: astore_2       
        //    46: new             Landroid/content/Intent;
        //    49: dup            
        //    50: ldc             "com.google.android.gms.ads.identifier.service.START"
        //    52: invokespecial   android/content/Intent.<init>:(Ljava/lang/String;)V
        //    55: astore_3       
        //    56: aload_3        
        //    57: ldc             "com.google.android.gms"
        //    59: invokevirtual   android/content/Intent.setPackage:(Ljava/lang/String;)Landroid/content/Intent;
        //    62: pop            
        //    63: aload_0        
        //    64: getfield        io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy.context:Landroid/content/Context;
        //    67: aload_3        
        //    68: aload_2        
        //    69: iconst_1       
        //    70: invokevirtual   android/content/Context.bindService:(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z
        //    73: istore_1       
        //    74: iload_1        
        //    75: ifeq            198
        //    78: new             Lio/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingInterface;
        //    81: dup            
        //    82: aload_2        
        //    83: invokevirtual   io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingConnection.getBinder:()Landroid/os/IBinder;
        //    86: invokespecial   io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingInterface.<init>:(Landroid/os/IBinder;)V
        //    89: astore_3       
        //    90: new             Lio/fabric/sdk/android/services/common/AdvertisingInfo;
        //    93: dup            
        //    94: aload_3        
        //    95: invokevirtual   io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingInterface.getId:()Ljava/lang/String;
        //    98: aload_3        
        //    99: invokevirtual   io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy$AdvertisingInterface.isLimitAdTrackingEnabled:()Z
        //   102: invokespecial   io/fabric/sdk/android/services/common/AdvertisingInfo.<init>:(Ljava/lang/String;Z)V
        //   105: astore_3       
        //   106: aload_0        
        //   107: getfield        io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy.context:Landroid/content/Context;
        //   110: aload_2        
        //   111: invokevirtual   android/content/Context.unbindService:(Landroid/content/ServiceConnection;)V
        //   114: aload_3        
        //   115: areturn        
        //   116: astore_2       
        //   117: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   120: ldc             "Fabric"
        //   122: ldc             "Unable to find Google Play Services package name"
        //   124: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   129: aconst_null    
        //   130: areturn        
        //   131: astore_2       
        //   132: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   135: ldc             "Fabric"
        //   137: ldc             "Unable to determine if Google Play Services is available"
        //   139: aload_2        
        //   140: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   145: aconst_null    
        //   146: areturn        
        //   147: astore_3       
        //   148: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   151: ldc             "Fabric"
        //   153: ldc             "Exception in binding to Google Play Service to capture AdvertisingId"
        //   155: aload_3        
        //   156: invokeinterface io/fabric/sdk/android/Logger.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   161: aload_0        
        //   162: getfield        io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy.context:Landroid/content/Context;
        //   165: aload_2        
        //   166: invokevirtual   android/content/Context.unbindService:(Landroid/content/ServiceConnection;)V
        //   169: aconst_null    
        //   170: areturn        
        //   171: astore_2       
        //   172: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   175: ldc             "Fabric"
        //   177: ldc             "Could not bind to Google Play Service to capture AdvertisingId"
        //   179: aload_2        
        //   180: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   185: aconst_null    
        //   186: areturn        
        //   187: astore_3       
        //   188: aload_0        
        //   189: getfield        io/fabric/sdk/android/services/common/AdvertisingInfoServiceStrategy.context:Landroid/content/Context;
        //   192: aload_2        
        //   193: invokevirtual   android/content/Context.unbindService:(Landroid/content/ServiceConnection;)V
        //   196: aload_3        
        //   197: athrow         
        //   198: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   201: ldc             "Fabric"
        //   203: ldc             "Could not bind to Google Play Service to capture AdvertisingId"
        //   205: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   210: aconst_null    
        //   211: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  23     37     116    131    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  23     37     131    147    Ljava/lang/Exception;
        //  63     74     171    187    Ljava/lang/Throwable;
        //  78     106    147    171    Ljava/lang/Exception;
        //  78     106    187    198    Any
        //  106    114    171    187    Ljava/lang/Throwable;
        //  148    161    187    198    Any
        //  161    169    171    187    Ljava/lang/Throwable;
        //  188    198    171    187    Ljava/lang/Throwable;
        //  198    210    171    187    Ljava/lang/Throwable;
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
    
    private static final class AdvertisingConnection implements ServiceConnection
    {
        private final LinkedBlockingQueue<IBinder> queue;
        private boolean retrieved;
        
        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue<IBinder>(1);
        }
        
        public IBinder getBinder() {
            if (this.retrieved) {
                Fabric.getLogger().e("Fabric", "getBinder already called");
            }
            this.retrieved = true;
            try {
                return this.queue.poll(200L, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex) {
                return null;
            }
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            try {
                this.queue.put(binder);
            }
            catch (InterruptedException ex) {}
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            this.queue.clear();
        }
    }
    
    private static final class AdvertisingInterface implements IInterface
    {
        private final IBinder binder;
        
        public AdvertisingInterface(final IBinder binder) {
            this.binder = binder;
        }
        
        public IBinder asBinder() {
            return this.binder;
        }
        
        public String getId() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            }
            catch (Exception ex) {
                Fabric.getLogger().d("Fabric", "Could not get parcel from Google Play Service to capture AdvertisingId");
                return null;
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
        
        public boolean isLimitAdTrackingEnabled() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(1);
                this.binder.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readInt() != 0;
            }
            catch (Exception ex) {
                Fabric.getLogger().d("Fabric", "Could not get parcel from Google Play Service to capture Advertising limitAdTracking");
                return false;
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }
}
