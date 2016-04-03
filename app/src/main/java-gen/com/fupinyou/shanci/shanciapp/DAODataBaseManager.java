package com.fupinyou.shanci.shanciapp;

import android.database.sqlite.SQLiteDatabase;
import java.io.File;


/**
 * Created by fupinyou on 2016/4/1.
 */
public class DAODataBaseManager {
    private SQLiteDatabase sqLiteDatabase;
    public SQLiteDatabase openDatabase(File file){
         sqLiteDatabase= SQLiteDatabase.openOrCreateDatabase(file,null);
        return sqLiteDatabase;
    }

    public void closeDatabase() {
        this.sqLiteDatabase.close();
    }

  /*  public String query(){

    }*/
}
