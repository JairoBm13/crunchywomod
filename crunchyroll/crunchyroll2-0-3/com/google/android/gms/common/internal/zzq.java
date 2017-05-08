// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzq extends IInterface
{
    void zzb(final ResolveAccountResponse p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzq
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.common.internal.IResolveAccountCallbacks");
        }
        
        public static zzq zzaH(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IResolveAccountCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzq) {
                return (zzq)queryLocalInterface;
            }
            return new zzq.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.common.internal.IResolveAccountCallbacks");
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.common.internal.IResolveAccountCallbacks");
                    ResolveAccountResponse resolveAccountResponse;
                    if (parcel.readInt() != 0) {
                        resolveAccountResponse = (ResolveAccountResponse)ResolveAccountResponse.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        resolveAccountResponse = null;
                    }
                    this.zzb(resolveAccountResponse);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzq
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void zzb(final ResolveAccountResponse resolveAccountResponse) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IResolveAccountCallbacks");
                    if (resolveAccountResponse != null) {
                        obtain.writeInt(1);
                        resolveAccountResponse.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(2, obtain, obtain2, 0);
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
