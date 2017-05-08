// 
// Decompiled by Procyon v0.5.30
// 

package bolts;

import android.os.Bundle;
import android.content.Intent;

public final class AppLinks
{
    public static Bundle getAppLinkData(final Intent intent) {
        return intent.getBundleExtra("al_applink_data");
    }
}
