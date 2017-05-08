// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist;

public class ImageSize
{
    private static final String SEPARATOR = "x";
    private static final int TO_STRING_MAX_LENGHT = 9;
    private final int height;
    private final int width;
    
    public ImageSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public ImageSize(final int n, final int n2, final int n3) {
        if (n3 % 180 == 0) {
            this.width = n;
            this.height = n2;
            return;
        }
        this.width = n2;
        this.height = n;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public ImageSize scale(final float n) {
        return new ImageSize((int)(this.width * n), (int)(this.height * n));
    }
    
    public ImageSize scaleDown(final int n) {
        return new ImageSize(this.width / n, this.height / n);
    }
    
    @Override
    public String toString() {
        return new StringBuilder(9).append(this.width).append("x").append(this.height).toString();
    }
}
