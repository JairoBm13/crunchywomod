// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

public interface TreeNode
{
    JsonToken asToken();
    
    TreeNode get(final String p0);
    
    JsonParser.NumberType numberType();
    
    JsonParser traverse();
}
