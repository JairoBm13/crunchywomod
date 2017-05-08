// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog$Builder;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.os.Bundle;
import android.content.Context;

public class ErrorDialogFragments
{
    public static int ERROR_DIALOG_ICON;
    public static Class<?> EVENT_TYPE_ON_CLICK;
    
    static {
        ErrorDialogFragments.ERROR_DIALOG_ICON = 0;
    }
    
    public static Dialog createDialog(final Context context, final Bundle bundle, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
        alertDialog$Builder.setTitle((CharSequence)bundle.getString("de.greenrobot.eventbus.errordialog.title"));
        alertDialog$Builder.setMessage((CharSequence)bundle.getString("de.greenrobot.eventbus.errordialog.message"));
        if (ErrorDialogFragments.ERROR_DIALOG_ICON != 0) {
            alertDialog$Builder.setIcon(ErrorDialogFragments.ERROR_DIALOG_ICON);
        }
        alertDialog$Builder.setPositiveButton(17039370, dialogInterface$OnClickListener);
        return (Dialog)alertDialog$Builder.create();
    }
    
    public static void handleOnClick(final DialogInterface dialogInterface, final int n, final Activity activity, final Bundle bundle) {
        Label_0026: {
            if (ErrorDialogFragments.EVENT_TYPE_ON_CLICK == null) {
                break Label_0026;
            }
            try {
                ErrorDialogManager.factory.config.getEventBus().post(ErrorDialogFragments.EVENT_TYPE_ON_CLICK.newInstance());
                if (bundle.getBoolean("de.greenrobot.eventbus.errordialog.finish_after_dialog", false) && activity != null) {
                    activity.finish();
                }
            }
            catch (Exception ex) {
                throw new RuntimeException("Event cannot be constructed", ex);
            }
        }
    }
    
    @TargetApi(11)
    public static class Honeycomb extends DialogFragment implements DialogInterface$OnClickListener
    {
        public void onClick(final DialogInterface dialogInterface, final int n) {
            ErrorDialogFragments.handleOnClick(dialogInterface, n, this.getActivity(), this.getArguments());
        }
        
        public Dialog onCreateDialog(final Bundle bundle) {
            return ErrorDialogFragments.createDialog((Context)this.getActivity(), this.getArguments(), (DialogInterface$OnClickListener)this);
        }
    }
}
