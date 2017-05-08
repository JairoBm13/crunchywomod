// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import java.util.Collection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import com.swrve.sdk.SwrveHelper;
import java.util.LinkedHashMap;

public class MemoryCachedLocalStorage implements ILocalStorage
{
    private ILocalStorage cache;
    private Object cacheLock;
    private Object eventLock;
    private ILocalStorage secondaryStorage;
    
    public MemoryCachedLocalStorage(final ILocalStorage cache, final ILocalStorage secondaryStorage) {
        this.eventLock = new Object();
        this.cacheLock = new Object();
        this.cache = cache;
        this.secondaryStorage = secondaryStorage;
    }
    
    @Override
    public void addEvent(final String s) throws Exception {
        synchronized (this.eventLock) {
            this.cache.addEvent(s);
        }
    }
    
    @Override
    public void close() {
        this.cache.close();
        if (this.secondaryStorage != null) {
            this.secondaryStorage.close();
        }
    }
    
    public void flush() throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.cache:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //     4: aload_0        
        //     5: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.secondaryStorage:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //     8: if_acmpeq       79
        //    11: aload_0        
        //    12: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.cache:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //    15: instanceof      Lcom/swrve/sdk/localstorage/IFlushableLocalStorage;
        //    18: ifeq            79
        //    21: aload_0        
        //    22: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.secondaryStorage:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //    25: instanceof      Lcom/swrve/sdk/localstorage/IFastInsertLocalStorage;
        //    28: ifeq            79
        //    31: aload_0        
        //    32: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.cache:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //    35: checkcast       Lcom/swrve/sdk/localstorage/IFlushableLocalStorage;
        //    38: astore_1       
        //    39: aload_0        
        //    40: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.secondaryStorage:Lcom/swrve/sdk/localstorage/ILocalStorage;
        //    43: checkcast       Lcom/swrve/sdk/localstorage/IFastInsertLocalStorage;
        //    46: astore_2       
        //    47: aload_0        
        //    48: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.eventLock:Ljava/lang/Object;
        //    51: astore_3       
        //    52: aload_3        
        //    53: monitorenter   
        //    54: aload_1        
        //    55: aload_2        
        //    56: invokeinterface com/swrve/sdk/localstorage/IFlushableLocalStorage.flushEvents:(Lcom/swrve/sdk/localstorage/IFastInsertLocalStorage;)V
        //    61: aload_3        
        //    62: monitorexit    
        //    63: aload_0        
        //    64: getfield        com/swrve/sdk/localstorage/MemoryCachedLocalStorage.cacheLock:Ljava/lang/Object;
        //    67: astore_3       
        //    68: aload_3        
        //    69: monitorenter   
        //    70: aload_1        
        //    71: aload_2        
        //    72: invokeinterface com/swrve/sdk/localstorage/IFlushableLocalStorage.flushCache:(Lcom/swrve/sdk/localstorage/IFastInsertLocalStorage;)V
        //    77: aload_3        
        //    78: monitorexit    
        //    79: return         
        //    80: astore_1       
        //    81: aload_3        
        //    82: monitorexit    
        //    83: aload_1        
        //    84: athrow         
        //    85: astore_1       
        //    86: aload_3        
        //    87: monitorexit    
        //    88: aload_1        
        //    89: athrow         
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  54     63     80     85     Any
        //  70     79     85     90     Any
        //  81     83     80     85     Any
        //  86     88     85     90     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0079:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public String getCacheEntryForUser(final String s, final String s2) {
        synchronized (this.cacheLock) {
            String s4;
            final String s3 = s4 = this.cache.getCacheEntryForUser(s, s2);
            if (s3 == null) {
                s4 = s3;
                if (this.secondaryStorage != null) {
                    s4 = this.secondaryStorage.getCacheEntryForUser(s, s2);
                }
            }
            return s4;
        }
    }
    
    public LinkedHashMap<ILocalStorage, LinkedHashMap<Long, String>> getCombinedFirstNEvents(final Integer n) {
        synchronized (this.eventLock) {
            final LinkedHashMap<ILocalStorage, LinkedHashMap> linkedHashMap = (LinkedHashMap<ILocalStorage, LinkedHashMap>)new LinkedHashMap<ILocalStorage, LinkedHashMap<Long, String>>();
            int n2 = 0;
            if (this.secondaryStorage != null) {
                final LinkedHashMap<Long, String> firstNEvents = this.secondaryStorage.getFirstNEvents(n);
                final int size = firstNEvents.size();
                if ((n2 = size) > 0) {
                    linkedHashMap.put(this.secondaryStorage, firstNEvents);
                    n2 = size;
                }
            }
            if (n - n2 > 0) {
                final LinkedHashMap<Long, String> firstNEvents2 = this.cache.getFirstNEvents(n - n2);
                if (firstNEvents2.size() > 0) {
                    linkedHashMap.put(this.cache, firstNEvents2);
                }
            }
            return (LinkedHashMap<ILocalStorage, LinkedHashMap<Long, String>>)linkedHashMap;
        }
    }
    
    @Override
    public LinkedHashMap<Long, String> getFirstNEvents(final Integer n) {
        synchronized (this.eventLock) {
            return this.cache.getFirstNEvents(n);
        }
    }
    
    public ILocalStorage getSecondaryStorage() {
        return this.secondaryStorage;
    }
    
    public String getSecureCacheEntryForUser(String hmacWithMD5, final String s, final String s2) throws SecurityException {
        final Object cacheLock = this.cacheLock;
        // monitorenter(cacheLock)
        try {
            final String cacheEntryForUser = this.cache.getCacheEntryForUser(hmacWithMD5, s);
            final String cacheEntryForUser2 = this.cache.getCacheEntryForUser(hmacWithMD5, s + "_SGT");
            String cacheEntryForUser3 = cacheEntryForUser;
            String cacheEntryForUser4 = cacheEntryForUser2;
            if (SwrveHelper.isNullOrEmpty(cacheEntryForUser)) {
                cacheEntryForUser3 = cacheEntryForUser;
                cacheEntryForUser4 = cacheEntryForUser2;
                if (this.secondaryStorage != null) {
                    cacheEntryForUser3 = this.secondaryStorage.getCacheEntryForUser(hmacWithMD5, s);
                    cacheEntryForUser4 = this.secondaryStorage.getCacheEntryForUser(hmacWithMD5, s + "_SGT");
                }
            }
            // monitorexit(cacheLock)
            if (SwrveHelper.isNullOrEmpty(cacheEntryForUser3)) {
                goto Label_0189;
            }
            try {
                hmacWithMD5 = SwrveHelper.createHMACWithMD5(cacheEntryForUser3, s2);
                if (SwrveHelper.isNullOrEmpty(hmacWithMD5) || SwrveHelper.isNullOrEmpty(cacheEntryForUser4) || !cacheEntryForUser4.equals(hmacWithMD5)) {
                    throw new SecurityException("Signature validation failed");
                }
                goto Label_0189;
            }
            catch (NoSuchAlgorithmException ex) {
                Log.i("SwrveSDK", "Computing signature failed because of invalid algorithm");
            }
            catch (InvalidKeyException ex2) {
                Log.i("SwrveSDK", "Computing signature failed because of an invalid key");
                return cacheEntryForUser3;
            }
        }
        finally {}
    }
    
    public String getSharedCacheEntry(final String s) {
        return this.getCacheEntryForUser(s, s);
    }
    
    @Override
    public void removeEventsById(final Collection<Long> collection) {
        synchronized (this.eventLock) {
            this.cache.removeEventsById(collection);
        }
    }
    
    public void setAndFlushSecureSharedEntryForUser(final String ex, final String s, final String s2, String hmacWithMD5) {
        synchronized (this.cacheLock) {
            try {
                hmacWithMD5 = SwrveHelper.createHMACWithMD5(s2, hmacWithMD5);
                this.cache.setSecureCacheEntryForUser((String)ex, s, s2, hmacWithMD5);
                if (this.secondaryStorage != null) {
                    this.secondaryStorage.setSecureCacheEntryForUser((String)ex, s, s2, hmacWithMD5);
                }
            }
            catch (NoSuchAlgorithmException ex2) {
                Log.i("SwrveSDK", "Computing signature failed because of invalid algorithm");
                this.cache.setCacheEntryForUser((String)ex, s, s2);
                if (this.secondaryStorage != null) {
                    this.secondaryStorage.setCacheEntryForUser((String)ex, s, s2);
                }
            }
            catch (InvalidKeyException ex) {
                Log.i("SwrveSDK", "Computing signature failed because of an invalid key");
            }
        }
    }
    
    public void setAndFlushSharedEntry(final String s, final String s2) {
        synchronized (this.cacheLock) {
            this.cache.setCacheEntryForUser(s, s, s2);
            if (this.secondaryStorage != null) {
                this.secondaryStorage.setCacheEntryForUser(s, s, s2);
            }
        }
    }
    
    @Override
    public void setCacheEntryForUser(final String s, final String s2, final String s3) {
        synchronized (this.cacheLock) {
            this.cache.setCacheEntryForUser(s, s2, s3);
        }
    }
    
    public void setSecondaryStorage(final ILocalStorage secondaryStorage) {
        this.secondaryStorage = secondaryStorage;
    }
    
    @Override
    public void setSecureCacheEntryForUser(final String s, final String s2, final String s3, final String s4) {
        synchronized (this.cacheLock) {
            this.cache.setCacheEntryForUser(s, s2, s3);
            this.cache.setCacheEntryForUser(s, s2 + "_SGT", s4);
        }
    }
}
