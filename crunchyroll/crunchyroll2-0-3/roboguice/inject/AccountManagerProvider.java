// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Inject;
import android.content.Context;
import android.accounts.AccountManager;
import com.google.inject.Provider;

@ContextSingleton
public class AccountManagerProvider implements Provider<AccountManager>
{
    @Inject
    protected Context context;
    
    @Override
    public AccountManager get() {
        return AccountManager.get(this.context);
    }
}
