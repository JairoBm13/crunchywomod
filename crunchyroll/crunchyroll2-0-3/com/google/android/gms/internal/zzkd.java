// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzkd extends IInterface
{
    void destroy() throws RemoteException;
    
    void disconnect() throws RemoteException;
    
    void zza(final zzkc p0) throws RemoteException;
    
    void zza(final zzkc p0, final int p1) throws RemoteException;
    
    void zza(final zzkc p0, final zzke p1, final String p2, final String p3) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkd
    {
        public static zzkd zzaA(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkd) {
                return (zzkd)queryLocalInterface;
            }
            return new zzkd.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.destroy();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.disconnect();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.zza(zzkc.zza.zzaz(parcel.readStrongBinder()), zzke.zza.zzaB(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.zza(zzkc.zza.zzaz(parcel.readStrongBinder()), parcel.readInt());
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.zza(zzkc.zza.zzaz(parcel.readStrongBinder()));
                    return true;
                }
            }
        }
        
        private static class zza implements zzkd
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void destroy() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.zznF.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void disconnect() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    this.zznF.transact(3, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzkc zzkc) throws RemoteException {
                IBinder binder = null;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    if (zzkc != null) {
                        binder = zzkc.asBinder();
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznF.transact(6, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzkc zzkc, final int n) throws RemoteException {
                IBinder binder = null;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    if (zzkc != null) {
                        binder = zzkc.asBinder();
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeInt(n);
                    this.zznF.transact(5, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzkc zzkc, final zzke zzke, final String s, final String s2) throws RemoteException {
                final IBinder binder = null;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
                    IBinder binder2;
                    if (zzkc != null) {
                        binder2 = zzkc.asBinder();
                    }
                    else {
                        binder2 = null;
                    }
                    obtain.writeStrongBinder(binder2);
                    IBinder binder3 = binder;
                    if (zzke != null) {
                        binder3 = zzke.asBinder();
                    }
                    obtain.writeStrongBinder(binder3);
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    this.zznF.transact(4, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
