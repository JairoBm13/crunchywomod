// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.content.res.Resources;
import com.google.inject.Singleton;

@Singleton
public class StringResourceFactory implements ResourceFactory<String>
{
    protected Resources resources;
    
    public StringResourceFactory(final Resources resources) {
        this.resources = resources;
    }
    
    @Override
    public String get(final int n) {
        return this.resources.getString(n);
    }
}
