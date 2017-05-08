// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.activity.event;

import android.content.Intent;

public class OnActivityResultEvent
{
    protected Intent data;
    protected int requestCode;
    protected int resultCode;
    
    public OnActivityResultEvent(final int requestCode, final int resultCode, final Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
    
    public Intent getData() {
        return this.data;
    }
    
    public int getRequestCode() {
        return this.requestCode;
    }
    
    public int getResultCode() {
        return this.resultCode;
    }
}
