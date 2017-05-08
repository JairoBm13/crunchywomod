// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.os.Looper;
import android.os.Handler;
import com.google.inject.Provider;

public class HandlerProvider implements Provider<Handler>
{
    @Override
    public Handler get() {
        return new Handler(Looper.getMainLooper());
    }
}
