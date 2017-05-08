// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.cast.LaunchOptions;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzi extends IInterface
{
    void disconnect() throws RemoteException;
    
    void zza(final double p0, final double p1, final boolean p2) throws RemoteException;
    
    void zza(final String p0, final LaunchOptions p1) throws RemoteException;
    
    void zza(final String p0, final String p1, final long p2) throws RemoteException;
    
    void zza(final String p0, final byte[] p1, final long p2) throws RemoteException;
    
    void zza(final boolean p0, final double p1, final boolean p2) throws RemoteException;
    
    void zzbG(final String p0) throws RemoteException;
    
    void zzbH(final String p0) throws RemoteException;
    
    void zzbI(final String p0) throws RemoteException;
    
    void zzf(final String p0, final boolean p1) throws RemoteException;
    
    void zzlN() throws RemoteException;
    
    void zzlY() throws RemoteException;
    
    void zzr(final String p0, final String p1) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzi
    {
        public static zzi zzax(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.cast.internal.ICastDeviceController");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzi) {
                return (zzi)queryLocalInterface;
            }
            return new zzi.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            boolean b = false;
            boolean b2 = false;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.cast.internal.ICastDeviceController");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.disconnect();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    final String string = parcel.readString();
                    if (parcel.readInt() != 0) {
                        b2 = true;
                    }
                    this.zzf(string, b2);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzr(parcel.readString(), parcel.readString());
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzlY();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzbG(parcel.readString());
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzlN();
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zza(parcel.readDouble(), parcel.readDouble(), parcel.readInt() != 0);
                    return true;
                }
                case 8: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    final boolean b3 = parcel.readInt() != 0;
                    final double double1 = parcel.readDouble();
                    if (parcel.readInt() != 0) {
                        b = true;
                    }
                    this.zza(b3, double1, b);
                    return true;
                }
                case 9: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zza(parcel.readString(), parcel.readString(), parcel.readLong());
                    return true;
                }
                case 10: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zza(parcel.readString(), parcel.createByteArray(), parcel.readLong());
                    return true;
                }
                case 11: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzbH(parcel.readString());
                    return true;
                }
                case 12: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zzbI(parcel.readString());
                    return true;
                }
                case 13: {
                    parcel.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    final String string2 = parcel.readString();
                    LaunchOptions launchOptions;
                    if (parcel.readInt() != 0) {
                        launchOptions = (LaunchOptions)LaunchOptions.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        launchOptions = null;
                    }
                    this.zza(string2, launchOptions);
                    return true;
                }
            }
        }
        
        private static class zza implements zzi
        {
            private IBinder zznF;
            
            zza(final IBinder zznF) {
                this.zznF = zznF;
            }
            
            public IBinder asBinder() {
                return this.zznF;
            }
            
            @Override
            public void disconnect() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zznF.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final double n, final double n2, final boolean b) throws RemoteException {
                int n3 = 1;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeDouble(n);
                    obtain.writeDouble(n2);
                    if (!b) {
                        n3 = 0;
                    }
                    obtain.writeInt(n3);
                    this.zznF.transact(7, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final String s, final LaunchOptions launchOptions) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    if (launchOptions != null) {
                        obtain.writeInt(1);
                        launchOptions.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(13, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final String s, final String s2, final long n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    obtain.writeLong(n);
                    this.zznF.transact(9, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final String s, final byte[] array, final long n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    obtain.writeByteArray(array);
                    obtain.writeLong(n);
                    this.zznF.transact(10, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final boolean b, final double n, final boolean b2) throws RemoteException {
                final boolean b3 = true;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    int n2;
                    if (b) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    obtain.writeInt(n2);
                    obtain.writeDouble(n);
                    int n3;
                    if (b2) {
                        n3 = (b3 ? 1 : 0);
                    }
                    else {
                        n3 = 0;
                    }
                    obtain.writeInt(n3);
                    this.zznF.transact(8, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzbG(final String s) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    this.zznF.transact(5, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzbH(final String s) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    this.zznF.transact(11, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzbI(final String s) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    this.zznF.transact(12, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzf(final String s, final boolean b) throws RemoteException {
                int n = 1;
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    if (!b) {
                        n = 0;
                    }
                    obtain.writeInt(n);
                    this.zznF.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzlN() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zznF.transact(6, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzlY() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    this.zznF.transact(4, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzr(final String s, final String s2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    this.zznF.transact(3, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
