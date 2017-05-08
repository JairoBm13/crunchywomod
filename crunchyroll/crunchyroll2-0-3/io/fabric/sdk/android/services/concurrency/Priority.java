// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

public enum Priority
{
    HIGH, 
    IMMEDIATE, 
    LOW, 
    NORMAL;
    
    static <Y> int compareTo(final PriorityProvider priorityProvider, final Y y) {
        Priority priority;
        if (y instanceof PriorityProvider) {
            priority = ((PriorityProvider)y).getPriority();
        }
        else {
            priority = Priority.NORMAL;
        }
        return priority.ordinal() - priorityProvider.getPriority().ordinal();
    }
}
