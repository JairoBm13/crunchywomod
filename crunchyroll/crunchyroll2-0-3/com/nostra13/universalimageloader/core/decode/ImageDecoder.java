// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.decode;

import java.io.IOException;
import android.graphics.Bitmap;

public interface ImageDecoder
{
    Bitmap decode(final ImageDecodingInfo p0) throws IOException;
}
