package com.fupinyou.shanci.shanciapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by fupinyou on 2016/2/5.
 */
public class SettingTimeActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RadioGroup rg;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    public final static int RESULT_CODE=1;
    private RadioGroup.OnCheckedChangeListener listener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId==radioButton1.getId()){
                Intent intent=new Intent();
                intent.putExtra("gap", "10000");
                setResult(RESULT_CODE, intent);
                finish();
            }
            if (checkedId==radioButton2.getId()){
                Intent intent=new Intent();
                intent.putExtra("gap", "20000");
                setResult(RESULT_CODE, intent);
                finish();
            }
            if(checkedId==radioButton3.getId()){
                Intent intent=new Intent();
                intent.putExtra("gap", "30000");
                setResult(RESULT_CODE, intent);
                finish();
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_time);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar1);
        mToolbar=toolbar;
        setSupportActionBar(toolbar);
        initWindow();
        rg=(RadioGroup)findViewById(R.id.radiogroup1);
        radioButton1=(RadioButton)findViewById(R.id.radiobutton1);
        radioButton2=(RadioButton)findViewById(R.id.radiobutton2);
        radioButton3=(RadioButton)findViewById(R.id.radiobutton3);

        rg.setOnCheckedChangeListener(listener);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
