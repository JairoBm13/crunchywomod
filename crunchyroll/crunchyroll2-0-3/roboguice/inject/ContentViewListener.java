// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import java.io.Serializable;
import android.content.Context;
import roboguice.event.Observes;
import roboguice.activity.event.OnCreateEvent;
import com.google.inject.Inject;
import android.app.Activity;

@ContextSingleton
public class ContentViewListener
{
    @Inject
    protected Activity activity;
    
    public void optionallySetContentView(@Observes final OnCreateEvent onCreateEvent) {
        for (Serializable s = this.activity.getClass(); s != Context.class; s = ((Class<Context>)s).getSuperclass()) {
            final ContentView contentView = ((Class<Context>)s).getAnnotation(ContentView.class);
            if (contentView != null) {
                this.activity.setContentView(contentView.value());
                break;
            }
        }
    }
}
