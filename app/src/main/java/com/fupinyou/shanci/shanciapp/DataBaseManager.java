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

        wordcollection.add(new PastWord("sincere [sɪn'sɪə] adj. 真诚的；诚挚的；真实的","Compliment someone with a genuine comment on what you appreciate or respect about them.对他人就你一直欣赏或敬重的某一点表示真诚的赞美。"));
        wordcollection.add(new PastWord("mood [muːd] n. 情绪，语气；心境；气氛","The scenery chimed perfectly with the eerie mood of the play. 这个剧的布景和它的恐怖气氛十分协调。The government misjudged the mood of the country when it decided to call an election.政府在决定举行一次大选时，错误地估计了本国人民的情绪。"));
        wordcollection.add(new PastWord("static ['stætɪk] adj. 静态的；静电的；静力的","The stable situation of our country today was hard won. 我们今天稳定的形势是来之不易的。"));
        wordcollection.add(new PastWord("emotional [ɪ'məʊʃ(ə)n(ə)l] adj. 情绪的；易激动的；感动人的","He fuzzed up the plot line with a lot of emotional nonsense.他说了一大堆情绪激动的废话使情节主线模糊不清。"));
        wordcollection.add(new PastWord("systematic [sɪstə'mætɪk] adj. 系统的；体系的；","We need a systematic training program. First, we should do some flexibility exercises, and then I will customize a training program for you. 我们需要一个系统的训练计划.先要做些适应性训练.然后我会为你量身制定一份训练计划."));
        wordcollection.add(new PastWord("exclusive [ɪk'sklusɪv] adj. 独有的；排外的；专一的","A journalist has tried to muscle in on my exclusive story.一个记者一直想强行把我的独家报道弄到手。"));
        wordcollection.add(new PastWord("mislead [mɪs'liːd] vt. 误导；带错","And that mistake might mislead us if we start thinking the personal identity case.如果考虑人格同一性的问题,这个错误可能会误导我们。"));
        wordcollection.add(new PastWord("political [pə'lɪtɪk(ə)l] adj. 政治的；党派的","She disaffiliated from the political group.她退出了那个政治团体。"));
        wordcollection.add(new PastWord("magnificent [mæg'nɪfɪs(ə)nt] adj. 高尚的；壮丽的；华丽的；宏伟的","After the fire, nothing remained of the magnificent buildings of the temple. 大火过后， 寺院里的那些雄伟建筑已荡然无存。"));
        wordcollection.add(new PastWord("intelligence [ɪn'telɪdʒ(ə)ns] n. 智力；情报工作；情报机关；理解力","She has shown no abnormality in intelligence or in disposition. 她在智力或性情上都未显示出任何反常。"));
        wordcollection.add(new PastWord("asset ['æset] n. 资产；优点；有用的东西；有利条件；财产；有价值的人或物","On the other hand, your debt is an asset to the bank, but it is your liability. 在另一方面，你的债务是银行的一种资产，但它却是你的债务。"));
        wordcollection.add(new PastWord("abbreviations [ə,brivɪ'eʃən] n.缩写词，缩略语","Completely spell out your state or province and country or region; do not use abbreviations. 完全拼出您的州或省和国家或地区，不要使用缩写。"));
        wordcollection.add(new PastWord("understanding [ʌndə'stændɪŋ] n. 谅解，理解；理解力；协议 adj. 了解的；聪明的；有理解力的 v. 理解；明白（understand的ing形式）","This incident will make for better understanding between them. 这次事件将促进他们之间更好地相互了解。"));
        wordcollection.add(new PastWord("desirable [dɪ'zaɪərəb(ə)l] adj. 令人满意的；值得要的 n. 合意的人或事物","Both sides consider it desirable to further the understanding between the two peoples. 双方认为增进两国人民之间的了解是可取的。"));
        wordcollection.add(new PastWord("seriousness ['sɪərɪəsnəs] n. 严重性；严肃；认真","With great seriousness he pondered upon the problem. 他极其严肃地仔细考虑问题。"));
        wordcollection.add(new PastWord("agreement [ə'griːm(ə)nt] n. 协议；同意，一致","We disagreed over the wording of the agreement . 我们对协议的措词有不同意见。"));
        wordcollection.add(new PastWord("renounced [rɪ'naʊns] vt. 宣布放弃；与…断绝关系；垫牌 vi. 放弃权利；垫牌 n. 垫牌","Although educated for the ministry, Nietzsche soon renounced all faith and Christianity on the ground that it impeded the free expansion of life. 虽然培养为政府部门的人员，尼采却基于宗教信仰阻碍了生活的自由膨胀这一理由，而很快宣布放弃所有信仰包括基督教。"));
        wordcollection.add(new PastWord("previously ['priviəsli] adv. 以前；预先；仓促地","This means that we have to set all the previously mentioned properties. 这意味着我们必须设置所有以前提到过的属性。"));



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
