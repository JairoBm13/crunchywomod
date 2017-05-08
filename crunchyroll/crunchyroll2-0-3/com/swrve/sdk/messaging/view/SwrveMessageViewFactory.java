// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.view.WindowManager;
import com.swrve.sdk.messaging.SwrveMessageFormat;
import android.util.Log;
import com.swrve.sdk.messaging.ISwrveCustomButtonListener;
import com.swrve.sdk.messaging.ISwrveInstallButtonListener;
import com.swrve.sdk.messaging.SwrveOrientation;
import com.swrve.sdk.messaging.SwrveMessage;
import android.content.Context;

public class SwrveMessageViewFactory
{
    private static SwrveMessageViewFactory instance;
    
    public static SwrveMessageViewFactory getInstance() {
        if (SwrveMessageViewFactory.instance == null) {
            SwrveMessageViewFactory.instance = new SwrveMessageViewFactory();
        }
        return SwrveMessageViewFactory.instance;
    }
    
    public SwrveMessageView buildLayout(final Context context, final SwrveMessage swrveMessage, SwrveOrientation format, final int n, final ISwrveInstallButtonListener swrveInstallButtonListener, final ISwrveCustomButtonListener swrveCustomButtonListener, final boolean b, final int n2) throws SwrveMessageViewBuildException {
        final boolean b2 = false;
        if (swrveMessage != null) {
            while (true) {
                while (true) {
                    Label_0257: {
                        try {
                            if (swrveMessage.getFormats().size() > 0) {
                                Log.i("SwrveMessagingSDK", "Creating layout for message " + swrveMessage.getId() + " with orientation " + ((Enum)format).toString());
                                final SwrveMessageFormat swrveMessageFormat = (SwrveMessageFormat)(format = swrveMessage.getFormat((SwrveOrientation)format));
                                boolean b3 = b2;
                                if (swrveMessageFormat == null) {
                                    format = swrveMessageFormat;
                                    b3 = b2;
                                    if (!b) {
                                        format = swrveMessage.getFormats().get(0);
                                        b3 = true;
                                    }
                                }
                                if (format != null) {
                                    if (!b3) {
                                        break Label_0257;
                                    }
                                    final int n3 = -90;
                                    int n4 = n3;
                                    Label_0194: {
                                        if (!b3) {
                                            break Label_0194;
                                        }
                                        try {
                                            final int rotation = ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getRotation();
                                            n4 = n3;
                                            if (n != rotation) {
                                                if (n != 1 || rotation != 0) {
                                                    n4 = n3;
                                                    if (n != 3) {
                                                        return new SwrveMessageView(context, swrveMessage, (SwrveMessageFormat)format, swrveInstallButtonListener, swrveCustomButtonListener, b, n4, n2);
                                                    }
                                                    n4 = n3;
                                                    if (rotation != 2) {
                                                        return new SwrveMessageView(context, swrveMessage, (SwrveMessageFormat)format, swrveInstallButtonListener, swrveCustomButtonListener, b, n4, n2);
                                                    }
                                                }
                                                n4 = 90;
                                            }
                                            return new SwrveMessageView(context, swrveMessage, (SwrveMessageFormat)format, swrveInstallButtonListener, swrveCustomButtonListener, b, n4, n2);
                                        }
                                        catch (Exception ex) {
                                            try {
                                                Log.e("SwrveMessagingSDK", "Could not obtain device orientation", (Throwable)ex);
                                                n4 = n3;
                                            }
                                            catch (SwrveMessageViewBuildException ex2) {
                                                throw ex2;
                                            }
                                            catch (Exception ex3) {
                                                Log.e("SwrveMessagingSDK", "Error while building SwrveMessageView view", (Throwable)ex3);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (SwrveMessageViewBuildException ex4) {}
                        catch (Exception ex5) {}
                        break;
                    }
                    final int n3 = 0;
                    continue;
                }
            }
        }
        throw new SwrveMessageViewBuildException("No format with the given orientation was found");
    }
}
