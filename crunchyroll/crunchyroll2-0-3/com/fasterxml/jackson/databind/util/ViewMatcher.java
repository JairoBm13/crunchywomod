// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.io.Serializable;

public abstract class ViewMatcher
{
    public static ViewMatcher construct(final Class<?>[] array) {
        if (array == null) {
            return Empty.instance;
        }
        switch (array.length) {
            default: {
                return new Multi(array);
            }
            case 0: {
                return Empty.instance;
            }
            case 1: {
                return new Single(array[0]);
            }
        }
    }
    
    public abstract boolean isVisibleForView(final Class<?> p0);
    
    private static final class Empty extends ViewMatcher implements Serializable
    {
        static final Empty instance;
        
        static {
            instance = new Empty();
        }
        
        @Override
        public boolean isVisibleForView(final Class<?> clazz) {
            return false;
        }
    }
    
    private static final class Multi extends ViewMatcher implements Serializable
    {
        private final Class<?>[] _views;
        
        public Multi(final Class<?>[] views) {
            this._views = views;
        }
        
        @Override
        public boolean isVisibleForView(final Class<?> clazz) {
            final boolean b = false;
            final int length = this._views.length;
            int n = 0;
            boolean b2;
            while (true) {
                b2 = b;
                if (n >= length) {
                    break;
                }
                final Class<?> clazz2 = this._views[n];
                if (clazz == clazz2 || clazz2.isAssignableFrom(clazz)) {
                    b2 = true;
                    break;
                }
                ++n;
            }
            return b2;
        }
    }
    
    private static final class Single extends ViewMatcher implements Serializable
    {
        private final Class<?> _view;
        
        public Single(final Class<?> view) {
            this._view = view;
        }
        
        @Override
        public boolean isVisibleForView(final Class<?> clazz) {
            return clazz == this._view || this._view.isAssignableFrom(clazz);
        }
    }
}
