// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import com.swrve.sdk.common.R;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View$OnClickListener;
import android.support.v4.app.DialogFragment;

public class ValidationDialog extends DialogFragment implements View$OnClickListener
{
    public static ValidationDialog create() {
        return new ValidationDialog();
    }
    
    public void onClick(final View view) {
        this.dismiss();
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().requestFeature(1);
        return onCreateDialog;
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R.layout.cio__validation_dialog, viewGroup);
        inflate.findViewById(R.id.cio__btnDialogOk).setOnClickListener((View$OnClickListener)this);
        return inflate;
    }
}
