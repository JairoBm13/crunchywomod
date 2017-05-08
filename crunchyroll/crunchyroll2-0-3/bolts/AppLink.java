// 
// Decompiled by Procyon v0.5.30
// 

package bolts;

import java.util.Collections;
import java.util.List;
import android.net.Uri;

public class AppLink
{
    private Uri sourceUrl;
    private List<Target> targets;
    private Uri webUrl;
    
    public AppLink(final Uri sourceUrl, final List<Target> list, final Uri webUrl) {
        this.sourceUrl = sourceUrl;
        List<Target> emptyList = list;
        if (list == null) {
            emptyList = Collections.emptyList();
        }
        this.targets = emptyList;
        this.webUrl = webUrl;
    }
    
    public static class Target
    {
        private final String appName;
        private final String className;
        private final String packageName;
        private final Uri url;
        
        public Target(final String packageName, final String className, final Uri url, final String appName) {
            this.packageName = packageName;
            this.className = className;
            this.url = url;
            this.appName = appName;
        }
    }
}
