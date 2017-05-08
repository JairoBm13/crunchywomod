// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Map;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import android.database.Cursor;
import java.util.Iterator;
import android.database.sqlite.SQLiteStatement;
import java.util.List;
import android.database.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.concurrent.atomic.AtomicBoolean;

public class SQLiteLocalStorage implements IFastInsertLocalStorage, ILocalStorage
{
    private AtomicBoolean connectionOpen;
    private SQLiteDatabase database;
    private SwrveSQLiteOpenHelper dbHelper;
    
    public SQLiteLocalStorage(final Context context, final String s, final long maximumSize) {
        this.dbHelper = new SwrveSQLiteOpenHelper(context, s);
        (this.database = this.dbHelper.getWritableDatabase()).setMaximumSize(maximumSize);
        this.connectionOpen = new AtomicBoolean(true);
    }
    
    private void insertOrUpdate(final String s, final ContentValues contentValues, final String s2, final String[] array) {
        if (this.connectionOpen.get() && this.database.update(s, contentValues, s2, array) == 0) {
            this.database.insertOrThrow(s, (String)null, contentValues);
        }
    }
    
    @Override
    public void addEvent(final String s) throws SQLException {
        if (this.connectionOpen.get()) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put("event", s);
            this.database.insertOrThrow("events", (String)null, contentValues);
        }
    }
    
    @Override
    public void addMultipleEvent(final List<String> list) throws SQLException {
        if (this.connectionOpen.get()) {
            this.database.beginTransaction();
            SQLiteStatement compileStatement = null;
            SQLiteStatement sqLiteStatement;
            try {
                sqLiteStatement = (compileStatement = this.database.compileStatement("INSERT INTO events (event) VALUES (?)"));
                final Iterator<String> iterator = list.iterator();
                while (true) {
                    compileStatement = sqLiteStatement;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    compileStatement = sqLiteStatement;
                    sqLiteStatement.bindString(1, (String)iterator.next());
                    compileStatement = sqLiteStatement;
                    sqLiteStatement.execute();
                    compileStatement = sqLiteStatement;
                    sqLiteStatement.clearBindings();
                }
            }
            finally {
                if (compileStatement != null) {
                    compileStatement.close();
                }
                this.database.endTransaction();
            }
            this.database.setTransactionSuccessful();
            if (sqLiteStatement != null) {
                sqLiteStatement.close();
            }
            this.database.endTransaction();
        }
    }
    
    @Override
    public void close() {
        this.dbHelper.close();
        this.database.close();
        this.connectionOpen.set(false);
    }
    
    @Override
    public String getCacheEntryForUser(String s, final String s2) {
        Object o = null;
        final Cursor cursor = null;
        final String s3 = null;
        if (!this.connectionOpen.get()) {
            return (String)o;
        }
        final Cursor cursor2 = null;
        Cursor query;
        final Cursor cursor3 = query = null;
        Object o2 = cursor;
        o = cursor2;
        try {
            final SQLiteDatabase database = this.database;
            query = cursor3;
            o2 = cursor;
            o = cursor2;
            s = "user_id= \"" + s + "\" AND " + "category" + "= \"" + s2 + "\"";
            query = cursor3;
            o2 = cursor;
            o = cursor2;
            final Cursor cursor4 = query = database.query("server_cache", new String[] { "raw_data" }, s, (String[])null, (String)null, (String)null, (String)null, "1");
            o2 = cursor;
            o = cursor4;
            cursor4.moveToFirst();
            s = s3;
            query = cursor4;
            o2 = cursor;
            o = cursor4;
            if (!cursor4.isAfterLast()) {
                query = cursor4;
                o2 = cursor;
                o = cursor4;
                s = cursor4.getString(0);
                query = cursor4;
                o2 = s;
                o = cursor4;
                cursor4.moveToNext();
            }
            o = s;
            if (cursor4 != null) {
                cursor4.close();
                o = s;
            }
            return (String)o;
        }
        catch (Exception ex) {
            o = query;
            ex.printStackTrace();
            o = o2;
            return (String)o2;
        }
        finally {
            if (o != null) {
                ((Cursor)o).close();
            }
        }
    }
    
    @Override
    public LinkedHashMap<Long, String> getFirstNEvents(final Integer n) {
        final String s = null;
        final LinkedHashMap<Long, String> linkedHashMap = new LinkedHashMap<Long, String>();
        if (!this.connectionOpen.get()) {
            return linkedHashMap;
        }
        final Cursor cursor = null;
        Cursor query;
        final Cursor cursor2 = query = null;
        Cursor cursor3 = cursor;
        Label_0167: {
            while (true) {
                try {
                    final SQLiteDatabase database = this.database;
                    if (n != null) {
                        break Label_0167;
                    }
                    final String string = s;
                    query = cursor2;
                    cursor3 = cursor;
                    final Cursor cursor4 = cursor3 = (query = database.query("events", new String[] { "_id", "event" }, (String)null, (String[])null, (String)null, (String)null, "_id", string));
                    cursor4.moveToFirst();
                    while (true) {
                        query = cursor4;
                        cursor3 = cursor4;
                        if (cursor4.isAfterLast()) {
                            return linkedHashMap;
                        }
                        query = cursor4;
                        cursor3 = cursor4;
                        linkedHashMap.put(cursor4.getLong(0), cursor4.getString(1));
                        query = cursor4;
                        cursor3 = cursor4;
                        cursor4.moveToNext();
                    }
                }
                catch (Exception ex) {
                    cursor3 = query;
                    ex.printStackTrace();
                    return linkedHashMap;
                    query = cursor2;
                    cursor3 = cursor;
                    final Integer n2;
                    final String string = Integer.toString(n2);
                    continue;
                }
                finally {
                    if (cursor3 != null) {
                        cursor3.close();
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public void removeEventsById(final Collection<Long> collection) {
        if (this.connectionOpen.get()) {
            final ArrayList<String> list = new ArrayList<String>(collection.size());
            final Iterator<Long> iterator = collection.iterator();
            while (iterator.hasNext()) {
                list.add(Long.toString(iterator.next()));
            }
            this.database.delete("events", "_id IN (" + TextUtils.join((CharSequence)",  ", (Iterable)list) + ")", (String[])null);
        }
    }
    
    @Override
    public void setCacheEntryForUser(final String s, final String s2, final String s3) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", s);
        contentValues.put("category", s2);
        contentValues.put("raw_data", s3);
        this.insertOrUpdate("server_cache", contentValues, "user_id= ? AND category= ?", new String[] { s, s2 });
    }
    
    @Override
    public void setMultipleCacheEntries(final List<Map.Entry<String, Map.Entry<String, String>>> list) throws SQLException {
        if (this.connectionOpen.get()) {
            this.database.beginTransaction();
            try {
                final Iterator<Map.Entry<String, Map.Entry<String, String>>> iterator = list.iterator();
                final ContentValues contentValues = new ContentValues();
                while (iterator.hasNext()) {
                    final Map.Entry<String, Map.Entry<String, String>> entry = iterator.next();
                    final String s = entry.getKey();
                    final String s2 = entry.getValue().getKey();
                    contentValues.put("user_id", s);
                    contentValues.put("category", s2);
                    contentValues.put("raw_data", (String)entry.getValue().getValue());
                    this.insertOrUpdate("server_cache", contentValues, "user_id= ? AND category= ?", new String[] { s, s2 });
                }
            }
            finally {
                this.database.endTransaction();
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
        }
    }
    
    @Override
    public void setSecureCacheEntryForUser(final String s, final String s2, final String s3, final String s4) {
        this.setCacheEntryForUser(s, s2, s3);
        this.setCacheEntryForUser(s, s2 + "_SGT", s4);
    }
    
    private static class SwrveSQLiteOpenHelper extends SQLiteOpenHelper
    {
        public SwrveSQLiteOpenHelper(final Context context, final String s) {
            super(context, s, (SQLiteDatabase$CursorFactory)null, 1);
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT NOT NULL);");
            sqLiteDatabase.execSQL("CREATE TABLE server_cache (user_id TEXT NOT NULL, category TEXT NOT NULL, raw_data TEXT NOT NULL, PRIMARY KEY (user_id,category));");
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        }
    }
}
