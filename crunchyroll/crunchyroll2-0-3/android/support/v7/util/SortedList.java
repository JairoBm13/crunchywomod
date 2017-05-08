// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.util;

import java.util.Arrays;
import java.lang.reflect.Array;

public class SortedList<T>
{
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mSize;
    private final Class<T> mTClass;
    
    public SortedList(final Class<T> clazz, final Callback<T> callback) {
        this(clazz, callback, 10);
    }
    
    public SortedList(final Class<T> mtClass, final Callback<T> mCallback, final int n) {
        this.mTClass = mtClass;
        this.mData = (Object[])Array.newInstance(mtClass, n);
        this.mCallback = mCallback;
        this.mSize = 0;
    }
    
    private int add(final T t, final boolean b) {
        final int index = this.findIndexOf(t, 1);
        int n;
        if (index == -1) {
            n = 0;
        }
        else if ((n = index) < this.mSize) {
            final Object o = this.mData[index];
            n = index;
            if (this.mCallback.areItemsTheSame(o, t)) {
                if (this.mCallback.areContentsTheSame(o, t)) {
                    this.mData[index] = t;
                    return index;
                }
                this.mData[index] = t;
                this.mCallback.onChanged(index, 1);
                return index;
            }
        }
        this.addToData(n, t);
        if (b) {
            this.mCallback.onInserted(n, 1);
        }
        return n;
    }
    
    private void addToData(final int n, final T t) {
        if (n > this.mSize) {
            throw new IndexOutOfBoundsException("cannot add item to " + n + " because size is " + this.mSize);
        }
        if (this.mSize == this.mData.length) {
            final Object[] mData = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, mData, 0, n);
            mData[n] = t;
            System.arraycopy(this.mData, n, mData, n + 1, this.mSize - n);
            this.mData = mData;
        }
        else {
            System.arraycopy(this.mData, n, this.mData, n + 1, this.mSize - n);
            this.mData[n] = t;
        }
        ++this.mSize;
    }
    
    private int findIndexOf(final T t, final int n) {
        int i = 0;
        int mSize = this.mSize;
        while (i < mSize) {
            final int n2 = (i + mSize) / 2;
            final Object o = this.mData[n2];
            final int compare = this.mCallback.compare(o, t);
            if (compare < 0) {
                i = n2 + 1;
            }
            else {
                if (compare == 0) {
                    if (!this.mCallback.areItemsTheSame(o, t)) {
                        final int linearEqualitySearch = this.linearEqualitySearch(t, n2, i, mSize);
                        if (n != 1) {
                            return linearEqualitySearch;
                        }
                        if (linearEqualitySearch != -1) {
                            return linearEqualitySearch;
                        }
                    }
                    return n2;
                }
                mSize = n2;
            }
        }
        if (n != 1) {
            i = -1;
        }
        return i;
    }
    
    private int linearEqualitySearch(final T t, int i, final int n, final int n2) {
        for (int j = i - 1; j >= n; --j) {
            final Object o = this.mData[j];
            if (this.mCallback.compare(o, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(o, t)) {
                return j;
            }
        }
        Object o2;
        for (++i; i < n2; ++i) {
            o2 = this.mData[i];
            if (this.mCallback.compare(o2, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(o2, t)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean remove(final T t, final boolean b) {
        final int index = this.findIndexOf(t, 2);
        if (index == -1) {
            return false;
        }
        this.removeItemAtIndex(index, b);
        return true;
    }
    
    private void removeItemAtIndex(final int n, final boolean b) {
        System.arraycopy(this.mData, n + 1, this.mData, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (b) {
            this.mCallback.onRemoved(n, 1);
        }
    }
    
    public int add(final T t) {
        return this.add(t, true);
    }
    
    public void beginBatchedUpdates() {
        if (this.mCallback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(this.mCallback);
        }
        this.mCallback = (Callback)this.mBatchedCallback;
    }
    
    public void clear() {
        if (this.mSize == 0) {
            return;
        }
        final int mSize = this.mSize;
        Arrays.fill(this.mData, 0, mSize, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, mSize);
    }
    
    public void endBatchedUpdates() {
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback)this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = (Callback)this.mBatchedCallback.mWrappedCallback;
        }
    }
    
    public T get(final int n) throws IndexOutOfBoundsException {
        if (n >= this.mSize || n < 0) {
            throw new IndexOutOfBoundsException("Asked to get item at " + n + " but size is " + this.mSize);
        }
        return (T)this.mData[n];
    }
    
    public int indexOf(final T t) {
        return this.findIndexOf(t, 4);
    }
    
    public void recalculatePositionOfItemAt(final int n) {
        final T value = this.get(n);
        this.removeItemAtIndex(n, false);
        final int add = this.add(value, false);
        if (n != add) {
            this.mCallback.onMoved(n, add);
        }
    }
    
    public boolean remove(final T t) {
        return this.remove(t, true);
    }
    
    public T removeItemAt(final int n) {
        final T value = this.get(n);
        this.removeItemAtIndex(n, true);
        return value;
    }
    
    public int size() {
        return this.mSize;
    }
    
    public void updateItemAt(final int n, final T t) {
        final T value = this.get(n);
        boolean b;
        if (value == t || !this.mCallback.areContentsTheSame(value, t)) {
            b = true;
        }
        else {
            b = false;
        }
        if (value != t && this.mCallback.compare(value, t) == 0) {
            this.mData[n] = t;
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
        }
        else {
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
            this.removeItemAtIndex(n, false);
            final int add = this.add(t, false);
            if (n != add) {
                this.mCallback.onMoved(n, add);
            }
        }
    }
    
    public static class BatchedCallback<T2> extends Callback<T2>
    {
        static final int TYPE_ADD = 1;
        static final int TYPE_CHANGE = 3;
        static final int TYPE_MOVE = 4;
        static final int TYPE_NONE = 0;
        static final int TYPE_REMOVE = 2;
        int mLastEventCount;
        int mLastEventPosition;
        int mLastEventType;
        private final Callback<T2> mWrappedCallback;
        
        public BatchedCallback(final Callback<T2> mWrappedCallback) {
            this.mLastEventType = 0;
            this.mLastEventPosition = -1;
            this.mLastEventCount = -1;
            this.mWrappedCallback = mWrappedCallback;
        }
        
        @Override
        public boolean areContentsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areContentsTheSame(t2, t3);
        }
        
        @Override
        public boolean areItemsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areItemsTheSame(t2, t3);
        }
        
        @Override
        public int compare(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.compare(t2, t3);
        }
        
        public void dispatchLastEvent() {
            if (this.mLastEventType == 0) {
                return;
            }
            switch (this.mLastEventType) {
                case 1: {
                    this.mWrappedCallback.onInserted(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
                case 2: {
                    this.mWrappedCallback.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
                case 3: {
                    this.mWrappedCallback.onChanged(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
            }
            this.mLastEventType = 0;
        }
        
        @Override
        public void onChanged(final int mLastEventPosition, final int mLastEventCount) {
            if (this.mLastEventType == 3 && mLastEventPosition <= this.mLastEventPosition + this.mLastEventCount && mLastEventPosition + mLastEventCount >= this.mLastEventPosition) {
                final int mLastEventPosition2 = this.mLastEventPosition;
                final int mLastEventCount2 = this.mLastEventCount;
                this.mLastEventPosition = Math.min(mLastEventPosition, this.mLastEventPosition);
                this.mLastEventCount = Math.max(mLastEventPosition2 + mLastEventCount2, mLastEventPosition + mLastEventCount) - this.mLastEventPosition;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = mLastEventPosition;
            this.mLastEventCount = mLastEventCount;
            this.mLastEventType = 3;
        }
        
        @Override
        public void onInserted(final int mLastEventPosition, final int mLastEventCount) {
            if (this.mLastEventType == 1 && mLastEventPosition >= this.mLastEventPosition && mLastEventPosition <= this.mLastEventPosition + this.mLastEventCount) {
                this.mLastEventCount += mLastEventCount;
                this.mLastEventPosition = Math.min(mLastEventPosition, this.mLastEventPosition);
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = mLastEventPosition;
            this.mLastEventCount = mLastEventCount;
            this.mLastEventType = 1;
        }
        
        @Override
        public void onMoved(final int n, final int n2) {
            this.dispatchLastEvent();
            this.mWrappedCallback.onMoved(n, n2);
        }
        
        @Override
        public void onRemoved(final int mLastEventPosition, final int mLastEventCount) {
            if (this.mLastEventType == 2 && this.mLastEventPosition == mLastEventPosition) {
                this.mLastEventCount += mLastEventCount;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = mLastEventPosition;
            this.mLastEventCount = mLastEventCount;
            this.mLastEventType = 2;
        }
    }
    
    public abstract static class Callback<T2>
    {
        public abstract boolean areContentsTheSame(final T2 p0, final T2 p1);
        
        public abstract boolean areItemsTheSame(final T2 p0, final T2 p1);
        
        public abstract int compare(final T2 p0, final T2 p1);
        
        public abstract void onChanged(final int p0, final int p1);
        
        public abstract void onInserted(final int p0, final int p1);
        
        public abstract void onMoved(final int p0, final int p1);
        
        public abstract void onRemoved(final int p0, final int p1);
    }
}
