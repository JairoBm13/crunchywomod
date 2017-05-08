// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import android.os.IInterface;

public interface zzr extends IInterface
{
    zzd zza(final zzd p0, final int p1, final int p2) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzr
    {
        public static zzr zzaI(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzr) {
                return (zzr)queryLocalInterface;
            }
            return new zzr.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.common.internal.ISignInButtonCreator");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                    final zzd zza = this.zza(zzd.zza.zzbg(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    IBinder binder;
                    if (zza != null) {
                        binder = zza.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    parcel2.writeStrongBinder(binder);
                    return true;
                }
            }
        }
        
        private static class zza implements zzr
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public zzd zza(zzd zzbg, final int n, final int n2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                    IBinder binder;
                    if (zzbg != null) {
                        binder = zzbg.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeInt(n);
                    obtain.writeInt(n2);
                    this.zznF.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    zzbg = zzd.zza.zzbg(obtain2.readStrongBinder());
                    return zzbg;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
