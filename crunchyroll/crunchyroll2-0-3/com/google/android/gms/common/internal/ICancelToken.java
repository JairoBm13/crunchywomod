// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.RemoteException;
import android.os.IInterface;

public interface ICancelToken extends IInterface
{
    void cancel() throws RemoteException;
}
