// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.internal;

import android.os.Parcelable$Creator;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import tv.ouya.console.api.Product;
import java.util.List;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.IInterface;

public interface IProductListListener extends IInterface
{
    void onCancel() throws RemoteException;
    
    void onFailure(final int p0, final String p1, final Bundle p2) throws RemoteException;
    
    void onSuccess(final List<Product> p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IProductListListener
    {
        public static IProductListListener asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("tv.ouya.console.internal.IProductListListener");
            if (queryLocalInterface != null && queryLocalInterface instanceof IProductListListener) {
                return (IProductListListener)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int int1, final Parcel parcel, final Parcel parcel2, final int n) throws RemoteException {
            switch (int1) {
                default: {
                    return super.onTransact(int1, parcel, parcel2, n);
                }
                case 1598968902: {
                    parcel2.writeString("tv.ouya.console.internal.IProductListListener");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("tv.ouya.console.internal.IProductListListener");
                    this.onSuccess(parcel.createTypedArrayList((Parcelable$Creator)Product.CREATOR));
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("tv.ouya.console.internal.IProductListListener");
                    int1 = parcel.readInt();
                    final String string = parcel.readString();
                    Bundle bundle;
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        bundle = null;
                    }
                    this.onFailure(int1, string, bundle);
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("tv.ouya.console.internal.IProductListListener");
                    this.onCancel();
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class Proxy implements IProductListListener
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void onCancel() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IProductListListener");
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void onFailure(final int n, final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IProductListListener");
                    obtain.writeInt(n);
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void onSuccess(final List<Product> list) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IProductListListener");
                    obtain.writeTypedList((List)list);
                    this.mRemote.transact(1, obtain, obtain2, 0);
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
