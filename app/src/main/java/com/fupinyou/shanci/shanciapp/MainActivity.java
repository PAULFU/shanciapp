package com.fupinyou.shanci.shanciapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    private Fragment[] fragments;
    private Toolbar mToolbar;
    private FloatingActionButton mFabButton;
    public static DataBaseManager mDataBaseManager;
    private int[] deleteArray = {1, 2};
    private final static int REQUEST_CODE = 0;
    private ServiceConnection connection;
    private int gap;
    private Intent intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar = toolbar;
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        fragments = new Fragment[4];
        fragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_cet4);
        fragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_cet6);
        fragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_description);
        fragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_about);
        getSupportFragmentManager().beginTransaction().hide(fragments[1]).hide(fragments[2])
                .hide(fragments[3]).show(fragments[0]).commit();
        mDataBaseManager = new DataBaseManager(MainActivity.this);
        mDataBaseManager.add();
        mDataBaseManager.insert();
        initRecyclerView();
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



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mFabButton = fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deleteId=myBinder.getIndex();
                int endId=DataBaseManager.sparseArray.size();
                String string=DataBaseManager.sparseArray.get(deleteId);
                String endString=DataBaseManager.sparseArray.get(endId);
                mDataBaseManager.delete(deleteId);
                DataBaseManager.sparseArray.setValueAt(deleteId-1, endString);
                DataBaseManager.sparseArray.delete(endId);
                Snackbar.make(view, "单词'"+string+"'已经删除", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == SettingTimeActivity.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                //int i = bundle.getInt("gap");
                String string = bundle.getString("gap");
                //gap = Integer.parseInt(string);
                //Log.v(TAG,string);
                intent.putExtra("gap",string);
                bindService(intent, connection, BIND_AUTO_CREATE);
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
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

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        String[] strings = mDataBaseManager.query().split(",");
        for (String s : strings) {
            itemList.add(s);
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
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        mDataBaseManager.closeDataBase();
        unbindService(connection);
    }
}
