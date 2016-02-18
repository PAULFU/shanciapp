package com.fupinyou.shanci.shanciapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yi on 2015/11/4.
 */
public class DataBaseManager {
    private SQLiteDatabase db;
    private DataBaseOpenHelper dataBaseOpenHelper;
    private boolean isexist;
    //public Map<Integer,String> map=new HashMap<Integer, String>();
    public static SparseArray<String> sparseArray=new SparseArray();
    List<Word> wordcollection=new ArrayList<>();

    public DataBaseManager(Context context) {
        String tableName="cet_four_table";
        wordcollection.add(new Word("hello"," 你好1"));
        wordcollection.add(new Word("hello"," 你好2"));
        wordcollection.add(new Word("hello"," 你好3"));
        wordcollection.add(new Word("hello"," 你好4"));
        wordcollection.add(new Word("hello"," 你好5"));
        wordcollection.add(new Word("hello"," 你好6"));
        wordcollection.add(new Word("hello"," 你好7"));
        wordcollection.add(new Word("hello"," 你好8"));
        wordcollection.add(new Word("hello"," 你好9"));
        wordcollection.add(new Word("hello"," 你好10"));
        wordcollection.add(new Word("hello"," 你好11"));
        wordcollection.add(new Word("hello"," 你好12"));
        wordcollection.add(new Word("hello"," 你好13"));
        wordcollection.add(new Word("hello"," 你好14"));
        wordcollection.add(new Word("hello"," 你好15"));
        wordcollection.add(new Word("hello"," 你好16"));
        wordcollection.add(new Word("hello"," 你好17"));
        wordcollection.add(new Word("hello"," 你好18"));
        wordcollection.add(new Word("hello"," 你好19"));
        wordcollection.add(new Word("hello"," 你好20"));
        wordcollection.add(new Word("hello"," 你好21"));
        wordcollection.add(new Word("hello"," 你好22"));
        wordcollection.add(new Word("hello"," 你好23"));
        wordcollection.add(new Word("hello"," 你好24"));
        wordcollection.add(new Word("hello"," 你好25"));
        wordcollection.add(new Word("hello"," 你好26"));
        wordcollection.add(new Word("hello"," 你好27"));
        wordcollection.add(new Word("hello"," 你好28"));
        wordcollection.add(new Word("hello"," 你好29"));
        wordcollection.add(new Word("hello"," 你好30"));
        DataBaseOpenHelper helper = new DataBaseOpenHelper(context);
        dataBaseOpenHelper=helper;
        db = helper.getWritableDatabase();
        isexist=helper.tableIsExist(tableName);
    }

    public void add() {
        if(!isexist) {
            db.beginTransaction();
            try {
                for (Word word : wordcollection) {
                    db.execSQL("INSERT INTO cet_four_table (word_content,word_meaning)" +
                            " VALUES('" + word.wordcontent + "','" + word.wordmeaning + "');");
                }
                db.setTransactionSuccessful();
            }
            catch (SQLException e) {
                Log.w("shanciapp","数据插入失败");
            } finally {
                db.endTransaction();
            }
        }
    }
    public void insert(){
        for (Word word:wordcollection){
            dataBaseOpenHelper.insert(word.wordcontent,word.wordmeaning);
        }
    }
    public String query(){
        int num=0;
        int wordid;
        String wordcontent;
        String wordmeaning;
        StringBuilder stringBuilder=new StringBuilder();
        //StringBuilder word2=new StringBuilder();
        Cursor c=db.rawQuery(
                "SELECT word_id,word_content,word_meaning FROM cet_four_table", null);// WHERE word_content='hello'
        //num=c.getColumnCount();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // wordid=String.valueOf(c.getInt(0));
            wordid=c.getInt(0);
             wordcontent= c.getString(1);
             wordmeaning=c.getString(2);
            stringBuilder.append(wordcontent);
            stringBuilder.append(wordmeaning+",");
             num++;
            //map.put(wordid,wordcontent+wordmeaning);
            sparseArray.put(wordid,wordcontent+wordmeaning);
            // do something useful with these
            c.moveToNext();
        }
        c.close();
       // return wordid+wordcontent+wordmeaning;
        return stringBuilder.toString()+ num;
    }

    public void delete(int id) {
//        String mString;
//        for (int aWordid : wordid) {
//            mString = String.valueOf(aWordid);
//            db.delete("cet_four_table", "word_id = ?", new String[]{mString});

            dataBaseOpenHelper.delete(id);

    }


    public void closeDataBase() {
        db.close();
    }
}
