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
    private boolean isexist=false;
    //public Map<Integer,String> map=new HashMap<Integer, String>();
    public static SparseArray<String> sparseArray=new SparseArray();
    List<PastWord> wordcollection=new ArrayList<>();

    public DataBaseManager(Context context) {
        String tableName="cet_four_table";

        wordcollection.add(new PastWord("sincere 真诚的","Compliment someone with a genuine comment on what you appreciate or respect about them.对他人就你一直欣赏或敬重的某一点表示真诚的赞美。"));
        wordcollection.add(new PastWord("mood 情绪","The government misjudged the mood of the country when it decided to call an election.政府在决定举行一次大选时，错误地估计了本国人民的情绪。"));
        wordcollection.add(new PastWord("static 稳定的","The stable situation of our country today was hard won. 我们今天稳定的形势是来之不易的。"));

        wordcollection.add(new PastWord("emotional adj. 情绪的；易激动的；感动人的","He fuzzed up the plot line with a lot of emotional nonsense.他说了一大堆情绪激动的废话使情节主线模糊不清。"));
        wordcollection.add(new PastWord("systematic adj. 系统的；体系的；","We need a systematic training program. First, we should do some flexibility exercises, and then I will customize a training program for you. 我们需要一个系统的训练计划.先要做些适应性训练.然后我会为你量身制定一份训练计划."));
        wordcollection.add(new PastWord("exclusive adj. 独有的；排外的；专一的","A journalist has tried to muscle in on my exclusive story.一个记者一直想强行把我的独家报道弄到手。"));
        wordcollection.add(new PastWord("mislead vt. 误导；带错","And that mistake might mislead us if we start thinking the personal identity case.如果考虑人格同一性的问题,这个错误可能会误导我们。"));
        wordcollection.add(new PastWord("political adj. 政治的；党派的","She disaffiliated from the political group.她退出了那个政治团体。"));




        DataBaseOpenHelper helper = new DataBaseOpenHelper(context);
        dataBaseOpenHelper=helper;
        db = helper.getWritableDatabase();
        isexist=helper.tableIsExist(tableName);
    }

    public void add() {
        if(!isexist) {
            db.beginTransaction();
            try {
                for (PastWord word : wordcollection) {
                    db.execSQL("INSERT INTO cet_four_table (word_content,word_meaning)" +
                            " VALUES('" + word.wordcontent + "','" + word.sentence + "');");
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
        for (PastWord word:wordcollection){
            dataBaseOpenHelper.insert(word.wordcontent,word.sentence);
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
            stringBuilder.append(wordcontent+"XXX");
            stringBuilder.append(wordmeaning + "XXX");
            num++;
            //map.put(wordid,wordcontent+wordmeaning);
            sparseArray.put(wordid,wordcontent);
            // do something useful with these
            c.moveToNext();
        }
        c.close();
        // return wordid+wordcontent+wordmeaning;
        return stringBuilder.toString();
    }

    public void delete(int id) {
//        String mString;
//        for (int aPastWordid : wordid) {
//            mString = String.valueOf(aPastWordid);
//            db.delete("cet_four_table", "word_id = ?", new String[]{mString});

        dataBaseOpenHelper.delete(id);

    }


    public void closeDataBase() {
        db.close();
    }
}
