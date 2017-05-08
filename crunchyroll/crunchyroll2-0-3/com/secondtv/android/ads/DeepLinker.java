// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Parcelable;

public abstract class DeepLinker implements Parcelable
{
    public abstract boolean open(final FragmentActivity p0, final boolean p1, final Uri p2, final boolean p3);
}
