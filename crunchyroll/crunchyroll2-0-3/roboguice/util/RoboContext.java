// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.util;

import com.google.inject.Key;
import java.util.Map;

public interface RoboContext
{
    Map<Key<?>, Object> getScopedObjectMap();
}
