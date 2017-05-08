// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public final class SlidingPercentile
{
    private static final Comparator<Sample> INDEX_COMPARATOR;
    private static final Comparator<Sample> VALUE_COMPARATOR;
    private int currentSortOrder;
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private final Sample[] recycledSamples;
    private final ArrayList<Sample> samples;
    private int totalWeight;
    
    static {
        INDEX_COMPARATOR = new Comparator<Sample>() {
            @Override
            public int compare(final Sample sample, final Sample sample2) {
                return sample.index - sample2.index;
            }
        };
        VALUE_COMPARATOR = new Comparator<Sample>() {
            @Override
            public int compare(final Sample sample, final Sample sample2) {
                if (sample.value < sample2.value) {
                    return -1;
                }
                if (sample2.value < sample.value) {
                    return 1;
                }
                return 0;
            }
        };
    }
    
    public SlidingPercentile(final int maxWeight) {
        this.maxWeight = maxWeight;
        this.recycledSamples = new Sample[5];
        this.samples = new ArrayList<Sample>();
        this.currentSortOrder = -1;
    }
    
    private void ensureSortedByIndex() {
        if (this.currentSortOrder != 1) {
            Collections.sort(this.samples, SlidingPercentile.INDEX_COMPARATOR);
            this.currentSortOrder = 1;
        }
    }
    
    private void ensureSortedByValue() {
        if (this.currentSortOrder != 0) {
            Collections.sort(this.samples, SlidingPercentile.VALUE_COMPARATOR);
            this.currentSortOrder = 0;
        }
    }
    
    public void addSample(int weight, final float value) {
        this.ensureSortedByIndex();
        Sample sample;
        if (this.recycledSampleCount > 0) {
            final Sample[] recycledSamples = this.recycledSamples;
            final int recycledSampleCount = this.recycledSampleCount - 1;
            this.recycledSampleCount = recycledSampleCount;
            sample = recycledSamples[recycledSampleCount];
        }
        else {
            sample = new Sample();
        }
        sample.index = this.nextSampleIndex++;
        sample.weight = weight;
        sample.value = value;
        this.samples.add(sample);
        this.totalWeight += weight;
        while (this.totalWeight > this.maxWeight) {
            weight = this.totalWeight - this.maxWeight;
            final Sample sample2 = this.samples.get(0);
            if (sample2.weight <= weight) {
                this.totalWeight -= sample2.weight;
                this.samples.remove(0);
                if (this.recycledSampleCount >= 5) {
                    continue;
                }
                final Sample[] recycledSamples2 = this.recycledSamples;
                weight = this.recycledSampleCount++;
                recycledSamples2[weight] = sample2;
            }
            else {
                sample2.weight -= weight;
                this.totalWeight -= weight;
            }
        }
    }
    
    public float getPercentile(final float n) {
        this.ensureSortedByValue();
        final float n2 = this.totalWeight;
        int n3 = 0;
        for (int i = 0; i < this.samples.size(); ++i) {
            final Sample sample = this.samples.get(i);
            n3 += sample.weight;
            if (n3 >= n * n2) {
                return sample.value;
            }
        }
        if (this.samples.isEmpty()) {
            return Float.NaN;
        }
        return this.samples.get(this.samples.size() - 1).value;
    }
    
    private static class Sample
    {
        public int index;
        public float value;
        public int weight;
    }
}
