// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Provider;

public class NullProvider<T> implements Provider<T>
{
    @Override
    public T get() {
        return null;
    }
}
