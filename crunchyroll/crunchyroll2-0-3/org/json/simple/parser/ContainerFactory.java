// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple.parser;

import java.util.Map;
import java.util.List;

public interface ContainerFactory
{
    List creatArrayContainer();
    
    Map createObjectContainer();
}
