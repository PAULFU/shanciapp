package com.fupinyou.shanci.shanciapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.canyinghao.candialog.CanDialog;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    private static String localFilePath="/shanci";
    private String searchstr;
    private List extralist;
    private List<String> popupItemList;
    public static SparseArray<String> sparseArray=new SparseArray();
    private int FILE_SELECT_CODE=1;
    private int mark=0;
    public static boolean aBoolean=false;
    private Fragment[] fragments;
    private RecyclerView mRecyclerView;
    private WordDao wordDao;
    private Toolbar mToolbar;
    private FloatingActionButton mFabButton;
    //private  FloatingActionButton actionButton;
    public static DataBaseManager mDataBaseManager;
    private int[] deleteArray = {1, 2};
    private final static int REQUEST_CODE = 0;
    private ServiceConnection connection;
    private ProgressBar mProgressBar;
    private int gap;
    private final int MSG_SUCCESS=1;
    private final int MSG_FAILURE=0;
    private final int DAO_MESSAGE=2;
    private Intent intent;
    private Bundle mBundle;
    private Handler mHandler;
    private IcibaInfo icibaInfo;
    private YouDaoSearchData youDaoSearchData;
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if (!msg.equals("")) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingTimeActivity.class);
                intent.putExtra("gap", "0");
                startActivityForResult(intent, REQUEST_CODE);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    private MyService.MyBinder myBinder;


            private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView= (TextView) findViewById(R.id.text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar= (ProgressBar) findViewById(R.id.google_progress);
      /*  mProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(this)
                .build());*/
        mToolbar = toolbar;
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);


         mHandler=new  Handler() {
            public void handleMessage(Message msg) {//此方法在ui线程运行
                switch (msg.what) {
                    case MSG_SUCCESS:
                        mProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(MainActivity.this)
                                .build());
                        initRecyclerView(createItemList());
                        textView.setText("");
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        Log.i("MainActivity", "SUCCESS");
                        break;
                    case MSG_FAILURE:
                        Log.i("MainActivity", "FAILURE");
                        break;
                    case DAO_MESSAGE:
                        //textView.setText("正在加载所选数据...");
                        mProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(MainActivity.this)
                                .build());
                        initRecyclerView(createItemListDAO());
                        //mRecyclerView.setVisibility(View.VISIBLE);
                        textView.setText("");
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        break;
                }
            }
        };

        fragments = new Fragment[4];
        fragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_cet4);
        fragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_cet6);
        fragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_description);
        fragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_about);
        getSupportFragmentManager().beginTransaction().hide(fragments[1]).hide(fragments[2])
                .hide(fragments[3]).show(fragments[0]).commit();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                mDataBaseManager = new DataBaseManager(MainActivity.this);
                mDataBaseManager.add();
                mDataBaseManager.insert();
                mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
               //mDataBaseManager.closeDataBase();
            }
        };
        mThread=new Thread(runnable);
        mThread.start();


        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"onlineieltstable.db",null);
        SQLiteDatabase db=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);
        DaoSession daoSession=daoMaster.newSession();
        final WordDao wordDao=daoSession.getWordDao();
        GenDAODataBase.genDAODataBase(wordDao);

        //initRecyclerView();
        Intent bindIntent = new Intent(this, MyService.class);
        intent=bindIntent;
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyService.MyBinder) service;
                myBinder.startToast();
            }
        };

        ImageView icon=new ImageView(this);
        icon.setImageResource(R.mipmap.plus);
        mFabButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .build();



        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.mipmap.download);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(MainActivity.this)
                        .build());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SSFTPsync.connectSftp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mProgressBar.setVisibility(View.GONE);
                final String[] items = SSFTPsync.strings;

                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.alerttitle)
                        .items(items)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, final int which, CharSequence text) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String remoteFileName;
                                        remoteFileName = items[which];
                                        File fi = new File(Environment
                                                .getExternalStorageDirectory().getPath()
                                                + localFilePath);
                                        if (!fi.exists() && !fi.isDirectory()) {
                                            System.out.println("//不存在");
                                            fi.mkdir();
                                        }
                                        File fiLF = new File(fi.getPath() + File.separator + remoteFileName);
                                        Log.d("fileLOCALPATH", fiLF.toString());
                                        if (!fiLF.exists()) {
                                            try {
                                                fiLF.createNewFile();
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                        try {
                                            SSFTPsync.sshSftpDOWN(fiLF.toString(), remoteFileName);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, "您选择的文件已同步到本地" + localFilePath + "文件夹", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        }).positiveText(R.string.choose)
                        .show();
            }
        });

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.delete);
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aBoolean) {
                    if (mBundle != null) {
                        int deleteId = myBinder.getIndex();
                        int endId = DataBaseManager.sparseArray.size();
                        String string = DataBaseManager.sparseArray.get(deleteId);
                        String endString = DataBaseManager.sparseArray.get(endId);
                        mDataBaseManager.delete(deleteId);
                        DataBaseManager.sparseArray.setValueAt(deleteId - 1, endString);
                        DataBaseManager.sparseArray.delete(endId);
                        Snackbar.make(v, "单词'" + string + "'已经删除", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(MainActivity.this, "请先设置好时间间隔后再试", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (mBundle != null) {
                        int deleteId = myBinder.getIndex();
                        int endId =sparseArray.size();
                        String string = sparseArray.get(deleteId);
                        String endString = sparseArray.get(endId);
                        wordDao.queryBuilder().where(WordDao.Properties.Id.eq(deleteId)).buildDelete().executeDeleteWithoutDetachingEntities();
                        sparseArray.setValueAt(deleteId - 1, endString);
                        sparseArray.delete(endId);
                        Snackbar.make(v, "单词'" + string + "'已经删除", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(MainActivity.this, "请先设置好时间间隔后再试", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.heart);
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CanDialog.Builder(MainActivity.this)
                        .setTitle("每日一句")
                        .setMessage(icibaInfo.getContent() + icibaInfo.getNote() + "\r\n" + icibaInfo.getDateline())
                        .setTileAnimator()
                        .setPositiveButton("确定", true, null)
                        .setCancelable(true)
                        .show();
            }
        });


        ImageView itemIcon4=new ImageView(this);
        itemIcon4.setImageResource(R.mipmap.openfile);
        SubActionButton button4=itemBuilder.setContentView(itemIcon4).build();
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG);
//                        .setAction("Action", null).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
//                intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择文件"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView itemIcon5=new ImageView(this);
        itemIcon5.setImageResource(R.mipmap.sousuo);
        SubActionButton button5=itemBuilder.setContentView(itemIcon5).build();
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.inputbox)
                        .input(R.string.input_hint,0,new MaterialDialog.InputCallback(){
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                searchstr=input.toString();
                                getSearchData();
                            }
                        }).show();
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .addSubActionView(button5)
                .attachTo(mFabButton)
                .build();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getEverydayJOSNData();
       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);*/
       /* navigationView.setNavigationItemSelectedListener(this);*/
    }

    private void getEverydayJOSNData(){
        String url="http://open.iciba.com/dsapi";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                icibaInfo= com.alibaba.fastjson.JSONObject.parseObject(response,IcibaInfo.class);
                Logger.i("response="+response);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.i("onErrorResponse: " + volleyError.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    public static boolean isChinese(char a) {
        int v = (int)a;
        return (v >=19968 && v <= 171941);
    }
    public static boolean containsChinese(String s){
        if (null == s || "".equals(s.trim())) return false;
        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i))) return true;
        }
        return false;
    }
    private void getSearchData(){
        String url="http://fanyi.youdao.com/openapi.do?keyfrom=fupinyou&key=1443574777&type=data&doctype=json&version=1.1&q="+searchstr;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                youDaoSearchData= com.alibaba.fastjson.JSONObject.parseObject(response,YouDaoSearchData.class);
                Logger.i(String.valueOf(youDaoSearchData.getErrorCode()));
                if(youDaoSearchData.getErrorCode()==0 && containsChinese(searchstr)) {
                    new CanDialog.Builder(MainActivity.this)
                            .setTitle("查询结果")
                            .setMessage("释义：" + youDaoSearchData.getTranslation() + "\r\n" + "音标：" + youDaoSearchData.getBasic().getPhonetic() + "\r\n"
                                    +"同义词："+ youDaoSearchData.getWeb().get(0).getKey() +
                                    youDaoSearchData.getWeb().get(0).getValue())
                            .setTileAnimator()
                            .setPositiveButton("确定", true, null)
                            .setCancelable(true)
                            .show();
                    Logger.i("response=" + response);
                }
                else if(youDaoSearchData.getErrorCode()==0) {
                    new CanDialog.Builder(MainActivity.this)
                            .setTitle("查询结果")
                            .setMessage("释义：" + youDaoSearchData.getTranslation() + "\r\n"
                                    + youDaoSearchData.getWeb().get(0).getKey() +
                                    youDaoSearchData.getWeb().get(0).getValue())
                            .setTileAnimator()
                            .setPositiveButton("确定", true, null)
                            .setCancelable(true)
                            .show();
                    Logger.i("response=" + response);
                }
                else {
                    Toast.makeText(MainActivity.this,"抱歉，没有查到您输入的单词释义( ^_^ )"+"     状态码："+youDaoSearchData.getErrorCode(),Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.i("onErrorResponse: " + volleyError.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == SettingTimeActivity.RESULT_CODE) {
                mark=1;
                Bundle bundle = data.getExtras();
                mBundle=bundle;
                //int i = bundle.getInt("gap");
                String string = bundle.getString("gap");
                //gap = Integer.parseInt(string);
                //Log.v(TAG,string);
                intent.putExtra("gap", string);
                bindService(intent, connection, BIND_AUTO_CREATE);
                int x=Integer.parseInt(string);
                x=x/1000;
                String str= String.valueOf(x);
                Toast.makeText(MainActivity.this,"您选择的时间间隔是"+str+"秒", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,"sincere 真诚的",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==FILE_SELECT_CODE){
            if(resultCode==RESULT_OK){
                aBoolean=true;
                Uri uri=data.getData();
                String filepath=uri.getPath();
                String fpath=uri.getLastPathSegment();
                Logger.e("返回选择的文件名", fpath);
                File file=new File(filepath);
                DAODataBaseManager dbm=new DAODataBaseManager();
               SQLiteDatabase sqldb= dbm.openDatabase(file);
                DaoMaster daoM=new DaoMaster(sqldb);
                DaoSession daoS=daoM.newSession();
                wordDao=daoS.getWordDao();
                //GenDAODataBase.genDAODataBase(wordDao);
                List list=wordDao.queryBuilder().list();
                extralist=list;
                stopRecyclerViews();
               Runnable daorunable=new Runnable() {
                   @Override
                   public void run() {
//                       mProgressBar.setVisibility(View.VISIBLE);
                       mHandler.obtainMessage(DAO_MESSAGE).sendToTarget();
                   }
               };
                mThread=new Thread(daorunable);
                mThread.start();
               /* try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bindService(intent, connection, BIND_AUTO_CREATE);*/

                //String daostr=daoquery(list);

               /*for (int i=0;i<list.size();i++){
                   //Log.e("DAODAODAO",list.get(i).toString());
                   Word w=(Word)list.get(i);
                   String str=w.toString();
                   Log.e("DAODAODAO",str);
               }*/
            }
        }
    }



    private List<String> createItemListDAO() {
        List<String> itemList = new ArrayList<>();
       popupItemList=new ArrayList<>();
        String[] strings = daoquery(extralist).split("XXX");
        for (int i=0;i<strings.length;i=i+2) {
            itemList.add(strings[i]);
            sparseArray.put(i/2,strings[i]);
        }
        for(int i=1;i<strings.length;i=i+2){
            popupItemList.add(strings[i]);
        }
        return itemList;
    }


    private String daoquery(List list){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<list.size();i++){
            //Log.e("DAODAODAO",list.get(i).toString());
            Word w=(Word)list.get(i);
            String str=w.toString();
            sb.append(str);
            //Log.i("DAODAODAO",str);
        }
        //sb=sb.append(list.size());
        return sb.toString();
    }

    protected void initRecyclerView(final List<String> liststring) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setVisibility(View.VISIBLE);
        mRecyclerView=recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(liststring);
        recyclerAdapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                new CanDialog.Builder(MainActivity.this)
                        .setTitle("单词例句")
                        .setMessage(popupItemList.get(position-1))
                        .setCircularRevealAnimator(CanDialog.CircularRevealStatus.TOP_LEFT)
                        .setPositiveButton("确定",true,null)
                        .show();
                //Toast.makeText(MainActivity.this,liststring.get(position-1),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void stopRecyclerViews(){
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        popupItemList=new ArrayList<>();
        String[] strings = mDataBaseManager.query().split("XXX");
        for (int i=0;i<strings.length;i=i+2) {
            itemList.add(strings[i]);
        }

        for(int i=1;i<strings.length;i=i+2){
            popupItemList.add(strings[i]);
        }
       /* for (int i=1;i<20;i++)
        {
            itemList.add("Item" + i);
        }*/
        return itemList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction().hide(fragments[1])
                    .hide(fragments[2]).hide(fragments[3]).show(fragments[0]).commit();
        } else if (id == R.id.nav_gallery) {
           /* fragmentManager.beginTransaction()
                    .replace(R.id.fragment_cet4,cet6Fragment)
                    .commit();*/
            getSupportFragmentManager().beginTransaction().hide(fragments[0])
                    .hide(fragments[2]).hide(fragments[3]).show(fragments[1]).commit();
        } else if (id == R.id.nav_slideshow) {
            getSupportFragmentManager().beginTransaction().hide(fragments[0])
                    .hide(fragments[1]).hide(fragments[3]).show(fragments[2]).commit();
        } else if (id == R.id.nav_manage) {
            getSupportFragmentManager().beginTransaction().hide(fragments[0])
                    .hide(fragments[1]).hide(fragments[2]).show(fragments[3]).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        mDataBaseManager.closeDataBase();
        if(mark!=0) {
            unbindService(connection);
        }
    }
}
