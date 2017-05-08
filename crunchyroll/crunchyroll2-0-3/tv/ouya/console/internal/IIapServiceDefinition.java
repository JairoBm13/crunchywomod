// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.internal;

import android.os.Parcelable$Creator;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import tv.ouya.console.api.Purchasable;
import java.util.List;
import android.os.RemoteException;
import android.os.IInterface;

public interface IIapServiceDefinition extends IInterface
{
    void requestGamerUuid(final String p0, final IStringListener p1) throws RemoteException;
    
    void requestProductList(final String p0, final List<Purchasable> p1, final IProductListListener p2) throws RemoteException;
    
    void requestPurchase(final String p0, final Purchasable p1, final IStringListener p2) throws RemoteException;
    
    void requestReceipts(final String p0, final IStringListener p1) throws RemoteException;
    
    void setTestMode() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IIapServiceDefinition
    {
        public static IIapServiceDefinition asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("tv.ouya.console.internal.IIapServiceDefinition");
            if (queryLocalInterface != null && queryLocalInterface instanceof IIapServiceDefinition) {
                return (IIapServiceDefinition)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("tv.ouya.console.internal.IIapServiceDefinition");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("tv.ouya.console.internal.IIapServiceDefinition");
                    this.setTestMode();
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("tv.ouya.console.internal.IIapServiceDefinition");
                    this.requestProductList(parcel.readString(), parcel.createTypedArrayList((Parcelable$Creator)Purchasable.CREATOR), IProductListListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("tv.ouya.console.internal.IIapServiceDefinition");
                    final String string = parcel.readString();
                    Purchasable purchasable;
                    if (parcel.readInt() != 0) {
                        purchasable = (Purchasable)Purchasable.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        purchasable = null;
                    }
                    this.requestPurchase(string, purchasable, IStringListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("tv.ouya.console.internal.IIapServiceDefinition");
                    this.requestReceipts(parcel.readString(), IStringListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("tv.ouya.console.internal.IIapServiceDefinition");
                    this.requestGamerUuid(parcel.readString(), IStringListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class Proxy implements IIapServiceDefinition
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void requestGamerUuid(final String s, final IStringListener stringListener) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IIapServiceDefinition");
                    obtain.writeString(s);
                    IBinder binder;
                    if (stringListener != null) {
                        binder = stringListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void requestProductList(final String s, final List<Purchasable> list, final IProductListListener productListListener) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IIapServiceDefinition");
                    obtain.writeString(s);
                    obtain.writeTypedList((List)list);
                    IBinder binder;
                    if (productListListener != null) {
                        binder = productListListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void requestPurchase(final String s, final Purchasable purchasable, final IStringListener stringListener) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("tv.ouya.console.internal.IIapServiceDefinition");
                            obtain.writeString(s);
                            if (purchasable != null) {
                                obtain.writeInt(1);
                                purchasable.writeToParcel(obtain, 0);
                            }
                            else {
                                obtain.writeInt(0);
                            }
                            if (stringListener != null) {
                                final IBinder binder = stringListener.asBinder();
                                obtain.writeStrongBinder(binder);
                                this.mRemote.transact(3, obtain, obtain2, 0);
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
            public void requestReceipts(final String s, final IStringListener stringListener) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IIapServiceDefinition");
                    obtain.writeString(s);
                    IBinder binder;
                    if (stringListener != null) {
                        binder = stringListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setTestMode() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("tv.ouya.console.internal.IIapServiceDefinition");
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
