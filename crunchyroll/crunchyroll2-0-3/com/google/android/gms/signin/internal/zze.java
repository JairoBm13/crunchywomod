// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.common.api.Status;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import android.os.IInterface;

public interface zze extends IInterface
{
    void zza(final ConnectionResult p0, final AuthAccountResult p1) throws RemoteException;
    
    void zzaT(final Status p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zze
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.signin.internal.ISignInCallbacks");
        }
        
        public static zze zzdC(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zze) {
                return (zze)queryLocalInterface;
            }
            return new zze.zza.zza(binder);
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
                    parcel2.writeString("com.google.android.gms.signin.internal.ISignInCallbacks");
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    ConnectionResult connectionResult;
                    if (parcel.readInt() != 0) {
                        connectionResult = (ConnectionResult)ConnectionResult.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        connectionResult = null;
                    }
                    AuthAccountResult authAccountResult;
                    if (parcel.readInt() != 0) {
                        authAccountResult = (AuthAccountResult)AuthAccountResult.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        authAccountResult = null;
                    }
                    this.zza(connectionResult, authAccountResult);
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    Status status;
                    if (parcel.readInt() != 0) {
                        status = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status = null;
                    }
                    this.zzaT(status);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zze
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void zza(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                            if (connectionResult != null) {
                                obtain.writeInt(1);
                                connectionResult.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (authAccountResult != null) {
                                obtain.writeInt(1);
                                authAccountResult.writeToParcel(obtain, 0);
                                this.zznF.transact(3, obtain, obtain2, 0);
                                obtain2.readException();
                                return;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        obtain.writeInt(0);
                        continue;
                    }
                }
            }
            
            @Override
            public void zzaT(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
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
