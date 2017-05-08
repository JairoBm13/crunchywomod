// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.view.View;
import android.content.Context;
import android.app.Activity;
import android.view.WindowManager$LayoutParams;
import com.swrve.sdk.messaging.SwrveMessage;
import android.app.Dialog;

public class SwrveDialog extends Dialog
{
    private boolean dismissed;
    private SwrveMessageView innerView;
    private SwrveMessage message;
    private WindowManager$LayoutParams originalParams;
    
    public SwrveDialog(final Activity ownerActivity, final SwrveMessage message, final SwrveMessageView swrveMessageView, final int n) {
        super((Context)ownerActivity, n);
        this.dismissed = false;
        this.message = message;
        this.innerView = swrveMessageView;
        this.originalParams = ownerActivity.getWindow().getAttributes();
        this.setContentView((View)swrveMessageView);
        this.setOwnerActivity(ownerActivity);
        swrveMessageView.setContainerDialog(this);
    }
    
    private void goneAway() {
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        try {
            this.getWindow().setAttributes(this.originalParams);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
    
    public void dismiss() {
        super.dismiss();
        this.message.getCampaign().messageDismissed();
        this.goneAway();
    }
    
    public SwrveMessage getMessage() {
        return this.message;
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.goneAway();
    }
    
    protected void onStart() {
        super.onStart();
        final WindowManager$LayoutParams attributes = this.getWindow().getAttributes();
        attributes.flags |= 0x400;
        this.getWindow().setAttributes(attributes);
    }
}
