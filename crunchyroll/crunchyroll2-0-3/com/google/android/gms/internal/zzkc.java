// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.view.Surface;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzkc extends IInterface
{
    void onDisconnected() throws RemoteException;
    
    void onError(final int p0) throws RemoteException;
    
    void zza(final int p0, final int p1, final Surface p2) throws RemoteException;
    
    void zzmg() throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkc
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
        }
        
        public static zzkc zzaz(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkc) {
                return (zzkc)queryLocalInterface;
            }
            return new zzkc.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int int1, final Parcel parcel, final Parcel parcel2, int int2) throws RemoteException {
            switch (int1) {
                default: {
                    return super.onTransact(int1, parcel, parcel2, int2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    int1 = parcel.readInt();
                    int2 = parcel.readInt();
                    Surface surface;
                    if (parcel.readInt() != 0) {
                        surface = (Surface)Surface.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        surface = null;
                    }
                    this.zza(int1, int2, surface);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    this.onError(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    this.onDisconnected();
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    this.zzmg();
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzkc
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void onDisconnected() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    this.zznF.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void onError(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    obtain.writeInt(n);
                    this.zznF.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final int n, final int n2, final Surface surface) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    obtain.writeInt(n);
                    obtain.writeInt(n2);
                    if (surface != null) {
                        obtain.writeInt(1);
                        surface.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzmg() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayCallbacks");
                    this.zznF.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
