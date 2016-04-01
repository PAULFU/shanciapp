package com.fupinyou.shanci.shanciapp;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fupinyou on 2016/3/31.
 */
public class GenDAODataBase {
    public static Word word0=new Word(null,"test 测试","He has stood up to the test of his honesty. 他已经通过了这次对他是否诚实的考验。");
    public static Word word1=new Word(null,"honest 诚实的","Honest men despise lies and liars. \n" +
            "诚实的人蔑视谎言和撒谎者。");


    public static void genDAODataBase(WordDao wordDao){
wordDao.insert(word0);
        wordDao.insert(word1);
    }
}
