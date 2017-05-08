// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.AuthAccountRequest;
import android.os.RemoteException;
import android.accounts.Account;
import android.os.IInterface;

public interface zzf extends IInterface
{
    void zza(final int p0, final Account p1, final zze p2) throws RemoteException;
    
    void zza(final AuthAccountRequest p0, final zze p1) throws RemoteException;
    
    void zza(final IAccountAccessor p0, final int p1, final boolean p2) throws RemoteException;
    
    void zza(final ResolveAccountRequest p0, final zzq p1) throws RemoteException;
    
    void zza(final CheckServerAuthResult p0) throws RemoteException;
    
    void zzal(final boolean p0) throws RemoteException;
    
    void zziQ(final int p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzf
    {
        public static zzf zzdD(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzf) {
                return (zzf)queryLocalInterface;
            }
            return new zzf.zza.zza(binder);
        }
        
        public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            boolean b = false;
            final CheckServerAuthResult checkServerAuthResult = null;
            final ResolveAccountRequest resolveAccountRequest = null;
            final Account account = null;
            AuthAccountRequest authAccountRequest = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.signin.internal.ISignInService");
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    if (parcel.readInt() != 0) {
                        authAccountRequest = (AuthAccountRequest)AuthAccountRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(authAccountRequest, zze.zza.zzdC(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    CheckServerAuthResult checkServerAuthResult2 = checkServerAuthResult;
                    if (parcel.readInt() != 0) {
                        checkServerAuthResult2 = (CheckServerAuthResult)CheckServerAuthResult.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(checkServerAuthResult2);
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    this.zzal(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    ResolveAccountRequest resolveAccountRequest2 = resolveAccountRequest;
                    if (parcel.readInt() != 0) {
                        resolveAccountRequest2 = (ResolveAccountRequest)ResolveAccountRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(resolveAccountRequest2, zzq.zza.zzaH(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    this.zziQ(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 8: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    n = parcel.readInt();
                    Account account2 = account;
                    if (parcel.readInt() != 0) {
                        account2 = (Account)Account.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(n, account2, zze.zza.zzdC(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 9: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    final IAccountAccessor zzaD = IAccountAccessor.zza.zzaD(parcel.readStrongBinder());
                    n = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        b = true;
                    }
                    this.zza(zzaD, n, b);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzf
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void zza(final int n, final Account account, final zze zze) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                            obtain.writeInt(n);
                            if (account != null) {
                                obtain.writeInt(1);
                                account.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (zze != null) {
                                final IBinder binder = zze.asBinder();
                                obtain.writeStrongBinder(binder);
                                this.zznF.transact(8, obtain, obtain2, 0);
                                obtain2.readException();
                                return;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public void zza(final AuthAccountRequest authAccountRequest, final zze zze) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                            if (authAccountRequest != null) {
                                obtain.writeInt(1);
                                authAccountRequest.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (zze != null) {
                                final IBinder binder = zze.asBinder();
                                obtain.writeStrongBinder(binder);
                                this.zznF.transact(2, obtain, obtain2, 0);
                                obtain2.readException();
                                return;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public void zza(final IAccountAccessor accountAccessor, int n, final boolean b) throws RemoteException {
                final int n2 = 0;
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    IBinder binder;
                    if (accountAccessor != null) {
                        binder = accountAccessor.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeInt(n);
                    n = n2;
                    if (b) {
                        n = 1;
                    }
                    obtain.writeInt(n);
                    this.zznF.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final ResolveAccountRequest resolveAccountRequest, final zzq zzq) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                            if (resolveAccountRequest != null) {
                                obtain.writeInt(1);
                                resolveAccountRequest.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (zzq != null) {
                                final IBinder binder = zzq.asBinder();
                                obtain.writeStrongBinder(binder);
                                this.zznF.transact(5, obtain, obtain2, 0);
                                obtain2.readException();
                                return;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public void zza(final CheckServerAuthResult checkServerAuthResult) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (checkServerAuthResult != null) {
                        obtain.writeInt(1);
                        checkServerAuthResult.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzal(final boolean b) throws RemoteException {
                int n = 0;
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (b) {
                        n = 1;
                    }
                    obtain.writeInt(n);
                    this.zznF.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zziQ(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(n);
                    this.zznF.transact(7, obtain, obtain2, 0);
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
