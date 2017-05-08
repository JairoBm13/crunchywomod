// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.logger;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.database.ContentObserver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ResultsDbAdapter
{
    public static final Uri CONTENT_URI;
    public static final String KEY_LOGEVENT = "logevent";
    public static final String KEY_LOGTYPE = "logtype";
    public static final String KEY_LONGMSG = "longmsg";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SHORTMSG = "shortmsg";
    public static final String PROVIDER_NAME = "com.tremorvideo.sdk.android.validator.log";
    private a a;
    private SQLiteDatabase b;
    private final Context c;
    
    static {
        CONTENT_URI = Uri.parse("content://com.tremorvideo.sdk.android.validator.log/logs");
    }
    
    public ResultsDbAdapter(final Context c) {
        this.c = c;
    }
    
    public void close() {
        this.a.close();
    }
    
    public long createResultRow(final String s, final String s2, final String s3, final String s4) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("logevent", s);
        contentValues.put("shortmsg", s2);
        contentValues.put("longmsg", s3);
        contentValues.put("logtype", s4);
        while (true) {
            try {
                this.b.insert("results", (String)null, contentValues);
                this.c.getContentResolver().notifyChange(ResultsDbAdapter.CONTENT_URI, (ContentObserver)null);
                return -1L;
            }
            catch (Exception ex) {
                Log.e("ResultsDbAdapter", "Database insert exception : " + ex);
                continue;
            }
            break;
        }
    }
    
    public boolean deleteResultRow(final long n) {
        return this.b.delete("results", "_id=" + n, (String[])null) > 0;
    }
    
    public void dropTable() throws SQLException {
        try {
            this.b.delete("results", (String)null, (String[])null);
        }
        catch (Exception ex) {
            Log.e("ResultsDbAdapter", "Table results doesnt exist : " + ex);
        }
    }
    
    public Cursor fetchAllResults() {
        try {
            return this.b.query("results", new String[] { "_id", "logevent", "shortmsg", "longmsg", "logtype" }, (String)null, (String[])null, (String)null, (String)null, "_id");
        }
        catch (Exception ex) {
            Log.e("ResultsDbAdapter", "Database fetchAll exception : " + ex);
            return null;
        }
    }
    
    public Cursor fetchResultRow(final long n) throws SQLException {
        final Cursor query = this.b.query(true, "results", new String[] { "_id", "logevent", "shortmsg", "longmsg", "logtype" }, "_id=" + n, (String[])null, (String)null, (String)null, (String)null, (String)null);
        if (query != null) {
            query.moveToFirst();
        }
        return query;
    }
    
    public ResultsDbAdapter open() throws SQLException {
        this.a = ResultsDbAdapter.a.a(this.c);
        this.b = this.a.getWritableDatabase();
        return this;
    }
    
    public boolean updateResultRow(final long n, final String s, final String s2, final String s3, final String s4) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("logevent", s);
        contentValues.put("shortmsg", s2);
        contentValues.put("longmsg", s3);
        contentValues.put("logtype", s4);
        return this.b.update("results", contentValues, "_id=" + n, (String[])null) > 0;
    }
    
    private static class a extends SQLiteOpenHelper
    {
        private static a a;
        
        static {
            ResultsDbAdapter.a.a = null;
        }
        
        a(final Context context) {
            super(context, "data", (SQLiteDatabase$CursorFactory)null, 2);
        }
        
        public static a a(final Context context) {
            if (ResultsDbAdapter.a.a == null) {
                ResultsDbAdapter.a.a = new a(context.getApplicationContext());
            }
            return ResultsDbAdapter.a.a;
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL("create table results (_id integer primary key autoincrement, logtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, logevent text not null, shortmsg text, longmsg text, logtype text not null);");
                Log.v("ResultsDbAdapter", "Database create success");
            }
            catch (Exception ex) {
                Log.e("ResultsDbAdapter", "Database create exception : " + ex);
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            Log.w("ResultsDbAdapter", "Upgrading database from version " + n + " to " + n2 + ", which will destroy all old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS results");
            this.onCreate(sqLiteDatabase);
        }
    }
}
