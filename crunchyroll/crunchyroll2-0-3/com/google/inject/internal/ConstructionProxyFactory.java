// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

interface ConstructionProxyFactory<T>
{
    ConstructionProxy<T> create() throws ErrorsException;
}
