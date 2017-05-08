// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.api.Status;
import android.os.RemoteException;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import android.os.IInterface;

public interface zzir extends IInterface
{
    void zza(final GetRecentContextCall.Response p0) throws RemoteException;
    
    void zza(final Status p0) throws RemoteException;
    
    void zza(final Status p0, final ParcelFileDescriptor p1) throws RemoteException;
    
    void zza(final Status p0, final boolean p1) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzir
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
        }
        
        public static zzir zzae(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzir) {
                return (zzir)queryLocalInterface;
            }
            return new zzir.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            final GetRecentContextCall.Response response = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    Status status;
                    if (parcel.readInt() != 0) {
                        status = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status = null;
                    }
                    this.zza(status);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    Status status2;
                    if (parcel.readInt() != 0) {
                        status2 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status2 = null;
                    }
                    ParcelFileDescriptor parcelFileDescriptor;
                    if (parcel.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        parcelFileDescriptor = null;
                    }
                    this.zza(status2, parcelFileDescriptor);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    Status status3;
                    if (parcel.readInt() != 0) {
                        status3 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status3 = null;
                    }
                    this.zza(status3, parcel.readInt() != 0);
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    GetRecentContextCall.Response zzw = response;
                    if (parcel.readInt() != 0) {
                        zzw = GetRecentContextCall.Response.CREATOR.zzw(parcel);
                    }
                    this.zza(zzw);
                    return true;
                }
            }
        }
        
        private static class zza implements zzir
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void zza(final GetRecentContextCall.Response response) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    if (response != null) {
                        obtain.writeInt(1);
                        response.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(4, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final Status status, final ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                            if (status != null) {
                                obtain.writeInt(1);
                                status.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (parcelFileDescriptor != null) {
                                obtain.writeInt(1);
                                parcelFileDescriptor.writeToParcel(obtain, 0);
                                this.zznF.transact(2, obtain, (Parcel)null, 1);
                                return;
                            }
                        }
                        finally {
                            obtain.recycle();
                        }
                        obtain.writeInt(0);
                        continue;
                    }
                }
            }
            
            @Override
            public void zza(final Status status, final boolean b) throws RemoteException {
            Label_0078_Outer:
                while (true) {
                    int n = 1;
                    final Parcel obtain = Parcel.obtain();
                    while (true) {
                        while (true) {
                            Label_0083: {
                                try {
                                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                                    if (status != null) {
                                        obtain.writeInt(1);
                                        status.writeToParcel(obtain, 0);
                                        break Label_0083;
                                    }
                                    obtain.writeInt(0);
                                    break Label_0083;
                                    obtain.writeInt(n);
                                    this.zznF.transact(3, obtain, (Parcel)null, 1);
                                    return;
                                }
                                finally {
                                    obtain.recycle();
                                }
                                n = 0;
                                continue Label_0078_Outer;
                            }
                            if (b) {
                                continue Label_0078_Outer;
                            }
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
}
