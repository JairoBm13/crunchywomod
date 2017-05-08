// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import com.google.android.exoplayer.upstream.NetworkLock;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import android.os.Handler;
import com.google.android.exoplayer.upstream.Allocator;

public final class DefaultLoadControl implements LoadControl
{
    private final Allocator allocator;
    private int bufferState;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private boolean fillingBuffers;
    private final float highBufferLoad;
    private final long highWatermarkUs;
    private final HashMap<Object, LoaderState> loaderStates;
    private final List<Object> loaders;
    private final float lowBufferLoad;
    private final long lowWatermarkUs;
    private long maxLoadStartPositionUs;
    private boolean streamingPrioritySet;
    private int targetBufferSize;
    
    public DefaultLoadControl(final Allocator allocator) {
        this(allocator, null, null);
    }
    
    public DefaultLoadControl(final Allocator allocator, final Handler handler, final EventListener eventListener) {
        this(allocator, handler, eventListener, 15000, 30000, 0.2f, 0.8f);
    }
    
    public DefaultLoadControl(final Allocator allocator, final Handler eventHandler, final EventListener eventListener, final int n, final int n2, final float lowBufferLoad, final float highBufferLoad) {
        this.allocator = allocator;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.loaders = new ArrayList<Object>();
        this.loaderStates = new HashMap<Object, LoaderState>();
        this.lowWatermarkUs = n * 1000L;
        this.highWatermarkUs = n2 * 1000L;
        this.lowBufferLoad = lowBufferLoad;
        this.highBufferLoad = highBufferLoad;
    }
    
    private int getBufferState(final int n) {
        final float n2 = n / this.targetBufferSize;
        if (n2 > this.highBufferLoad) {
            return 0;
        }
        if (n2 < this.lowBufferLoad) {
            return 2;
        }
        return 1;
    }
    
    private int getLoaderBufferState(long n, final long n2) {
        if (n2 != -1L) {
            n = n2 - n;
            if (n <= this.highWatermarkUs) {
                if (n < this.lowWatermarkUs) {
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
    
    private void notifyLoadingChanged(final boolean b) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    DefaultLoadControl.this.eventListener.onLoadingChanged(b);
                }
            });
        }
    }
    
    private void updateControlState() {
        boolean b = false;
        boolean b2 = false;
        int n = this.bufferState;
        boolean b3;
        for (int i = 0; i < this.loaders.size(); ++i, b = b3) {
            final LoaderState loaderState = this.loaderStates.get(this.loaders.get(i));
            b3 = (b | loaderState.loading);
            boolean b4;
            if (loaderState.nextLoadPositionUs != -1L) {
                b4 = true;
            }
            else {
                b4 = false;
            }
            b2 |= b4;
            n = Math.max(n, loaderState.bufferState);
        }
        this.fillingBuffers = (!this.loaders.isEmpty() && (b || b2) && (n == 2 || (n == 1 && this.fillingBuffers)));
        if (this.fillingBuffers && !this.streamingPrioritySet) {
            NetworkLock.instance.add(0);
            this.notifyLoadingChanged(this.streamingPrioritySet = true);
        }
        else if (!this.fillingBuffers && this.streamingPrioritySet && !b) {
            NetworkLock.instance.remove(0);
            this.notifyLoadingChanged(this.streamingPrioritySet = false);
        }
        this.maxLoadStartPositionUs = -1L;
        if (this.fillingBuffers) {
            for (int j = 0; j < this.loaders.size(); ++j) {
                final long nextLoadPositionUs = this.loaderStates.get(this.loaders.get(j)).nextLoadPositionUs;
                if (nextLoadPositionUs != -1L && (this.maxLoadStartPositionUs == -1L || nextLoadPositionUs < this.maxLoadStartPositionUs)) {
                    this.maxLoadStartPositionUs = nextLoadPositionUs;
                }
            }
        }
    }
    
    @Override
    public Allocator getAllocator() {
        return this.allocator;
    }
    
    @Override
    public void register(final Object o, final int n) {
        this.loaders.add(o);
        this.loaderStates.put(o, new LoaderState(n));
        this.targetBufferSize += n;
    }
    
    @Override
    public void trimAllocator() {
        this.allocator.trim(this.targetBufferSize);
    }
    
    @Override
    public void unregister(final Object o) {
        this.loaders.remove(o);
        this.targetBufferSize -= this.loaderStates.remove(o).bufferSizeContribution;
        this.updateControlState();
    }
    
    @Override
    public boolean update(final Object o, final long n, final long nextLoadPositionUs, final boolean loading) {
        final int loaderBufferState = this.getLoaderBufferState(n, nextLoadPositionUs);
        final LoaderState loaderState = this.loaderStates.get(o);
        boolean b;
        if (loaderState.bufferState != loaderBufferState || loaderState.nextLoadPositionUs != nextLoadPositionUs || loaderState.loading != loading) {
            b = true;
        }
        else {
            b = false;
        }
        if (b) {
            loaderState.bufferState = loaderBufferState;
            loaderState.nextLoadPositionUs = nextLoadPositionUs;
            loaderState.loading = loading;
        }
        final int totalBytesAllocated = this.allocator.getTotalBytesAllocated();
        final int bufferState = this.getBufferState(totalBytesAllocated);
        boolean b2;
        if (this.bufferState != bufferState) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (b2) {
            this.bufferState = bufferState;
        }
        if (b || b2) {
            this.updateControlState();
        }
        return totalBytesAllocated < this.targetBufferSize && nextLoadPositionUs != -1L && nextLoadPositionUs <= this.maxLoadStartPositionUs;
    }
    
    public interface EventListener
    {
        void onLoadingChanged(final boolean p0);
    }
    
    private static class LoaderState
    {
        public final int bufferSizeContribution;
        public int bufferState;
        public boolean loading;
        public long nextLoadPositionUs;
        
        public LoaderState(final int bufferSizeContribution) {
            this.bufferSizeContribution = bufferSizeContribution;
            this.bufferState = 0;
            this.loading = false;
            this.nextLoadPositionUs = -1L;
        }
    }
}
