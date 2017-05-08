// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.adapter;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.ArrayAdapter;

public class IterableAdapter<T> extends ArrayAdapter<T>
{
    public IterableAdapter(final Context context, final int n) {
        super(context, n);
    }
    
    public IterableAdapter(final Context context, final int n, final int n2) {
        super(context, n, n2);
    }
    
    public IterableAdapter(final Context context, final int n, final int n2, final Iterable<T> iterable) {
        super(context, n, n2, (List)toList(iterable));
    }
    
    public IterableAdapter(final Context context, final int n, final Iterable<T> iterable) {
        super(context, n, (List)toList(iterable));
    }
    
    protected static <T> List<T> toList(final Iterable<T> iterable) {
        final ArrayList<T> list = new ArrayList<T>();
        final Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
