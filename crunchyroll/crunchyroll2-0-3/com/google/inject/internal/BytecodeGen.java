// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ImmutableMap;
import java.security.AccessController;
import java.security.PrivilegedAction;
import com.google.inject.internal.util.$Nullable;
import com.google.inject.internal.util.$Function;
import com.google.inject.internal.util.$MapMaker;
import java.util.logging.Logger;
import java.util.Map;

public final class BytecodeGen
{
    private static final Map<ClassLoader, ClassLoader> CLASS_LOADER_CACHE;
    private static final boolean CUSTOM_LOADER_ENABLED;
    static final ClassLoader GUICE_CLASS_LOADER;
    static final String GUICE_INTERNAL_PACKAGE;
    static final Logger logger;
    
    static {
        logger = Logger.getLogger(BytecodeGen.class.getName());
        GUICE_CLASS_LOADER = canonicalize(BytecodeGen.class.getClassLoader());
        GUICE_INTERNAL_PACKAGE = BytecodeGen.class.getName().replaceFirst("\\.internal\\..*$", ".internal");
        CUSTOM_LOADER_ENABLED = Boolean.parseBoolean(System.getProperty("guice.custom.loader", "true"));
        if (BytecodeGen.CUSTOM_LOADER_ENABLED) {
            CLASS_LOADER_CACHE = new $MapMaker().weakKeys().weakValues().makeComputingMap(($Function<? super Object, ?>)new $Function<ClassLoader, ClassLoader>() {
                @Override
                public ClassLoader apply(@$Nullable final ClassLoader classLoader) {
                    BytecodeGen.logger.fine("Creating a bridge ClassLoader for " + classLoader);
                    return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
                        @Override
                        public ClassLoader run() {
                            return new BridgeClassLoader(classLoader);
                        }
                    });
                }
            });
            return;
        }
        CLASS_LOADER_CACHE = $ImmutableMap.of();
    }
    
    private static ClassLoader canonicalize(final ClassLoader classLoader) {
        if (classLoader != null) {
            return classLoader;
        }
        return SystemBridgeHolder.SYSTEM_BRIDGE.getParent();
    }
    
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return getClassLoader(clazz, clazz.getClassLoader());
    }
    
    private static ClassLoader getClassLoader(final Class<?> clazz, ClassLoader canonicalize) {
        if (!BytecodeGen.CUSTOM_LOADER_ENABLED) {
            return canonicalize;
        }
        if (clazz.getName().startsWith("java.")) {
            return BytecodeGen.GUICE_CLASS_LOADER;
        }
        canonicalize = canonicalize(canonicalize);
        if (canonicalize == BytecodeGen.GUICE_CLASS_LOADER || canonicalize instanceof BridgeClassLoader) {
            return canonicalize;
        }
        if (Visibility.forType(clazz) != Visibility.PUBLIC) {
            return canonicalize;
        }
        if (canonicalize != SystemBridgeHolder.SYSTEM_BRIDGE.getParent()) {
            return BytecodeGen.CLASS_LOADER_CACHE.get(canonicalize);
        }
        return SystemBridgeHolder.SYSTEM_BRIDGE;
    }
    
    private static class BridgeClassLoader extends ClassLoader
    {
        BridgeClassLoader() {
        }
        
        BridgeClassLoader(final ClassLoader classLoader) {
            super(classLoader);
        }
        
        Class<?> classicLoadClass(final String s, final boolean b) throws ClassNotFoundException {
            return super.loadClass(s, b);
        }
        
        @Override
        protected Class<?> loadClass(final String s, final boolean b) throws ClassNotFoundException {
            if (!s.startsWith("sun.reflect")) {
                if (s.startsWith(BytecodeGen.GUICE_INTERNAL_PACKAGE) || s.startsWith(" ")) {
                    if (BytecodeGen.GUICE_CLASS_LOADER == null) {
                        return SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(s, b);
                    }
                    try {
                        final Class<?> clazz2;
                        final Class<?> clazz = clazz2 = BytecodeGen.GUICE_CLASS_LOADER.loadClass(s);
                        if (b) {
                            this.resolveClass(clazz);
                            return clazz;
                        }
                        return clazz2;
                    }
                    catch (Throwable t) {}
                }
                return this.classicLoadClass(s, b);
            }
            return SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(s, b);
        }
    }
    
    private static class SystemBridgeHolder
    {
        static final BridgeClassLoader SYSTEM_BRIDGE;
        
        static {
            SYSTEM_BRIDGE = new BridgeClassLoader();
        }
    }
    
    public enum Visibility
    {
        PUBLIC {
        }, 
        SAME_PACKAGE {
        };
        
        public static Visibility forType(final Class<?> clazz) {
            if ((clazz.getModifiers() & 0x5) != 0x0) {
                return Visibility.PUBLIC;
            }
            return Visibility.SAME_PACKAGE;
        }
    }
}
