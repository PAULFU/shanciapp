package com.fupinyou.shanci.shanciapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yi on 2015/11/4.
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_PATH = "data/data/com.fupinyou.shanci.shanciapp/databases/";
    private static final String DATABASE_NAME = "vocabulary1.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="cet_four_table";
    private static final String WORD_CONTENT="word_content";
    private static final String WORD_ID="word_id";
    private static final String WORD_MEANING="word_meaning";
    private static final String ACTIVITY_TAG="LogItem";

    public DataBaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql="Create table "+TABLE_NAME+" ("+WORD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
               +WORD_CONTENT+" TEXT," +WORD_MEANING+" TEXT);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql=" DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        return  db.query(TABLE_NAME, null, null, null, null, null, " word_id desc");
    }

    public long insert(String wordContent,String wordMeaning)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(WORD_CONTENT, wordContent);
        cv.put(WORD_MEANING, wordMeaning);
        return db.insert(TABLE_NAME, null, cv);
    }

    public void delete(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=WORD_ID+"=?";
        String[] whereValue={Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }

    public void update(int id,String wordContent,String wordMeaning)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=WORD_ID+"=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues();
        cv.put(WORD_CONTENT, wordContent);
        cv.put(WORD_MEANING, wordMeaning);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    //判断表是否存在
    public boolean tableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READONLY| SQLiteDatabase.NO_LOCALIZED_COLLATORS );
        } catch (SQLiteException e) {
            // TODO: handle exception
            Log.w("shanciapp","NoSuchDataBase");
        }

        if(db!=null)
            result=true;

        return result;
    }
}
