package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class SelectActivity extends AppCompatActivity {

    private Button eazybutton;
    private Button normalbutton;
    private Button hardbutton;
    private ImageButton backbutton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;

    private boolean isConditionNormal = false;
    private boolean isConditionHard = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //sharedPreferencesの初期化
        sharedPreferences = getSharedPreferences("isConditionNormal",MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("isConditionHard",MODE_PRIVATE);

        //sharedPreferencesの設定
        //isConditionNormal = sharedPreferences.getBoolean("isConditionNormal",false);
        //isConditionHard = sharedPreferences2.getBoolean("isConditionHard",false);

        eazybutton = findViewById(R.id.eazy);
        normalbutton = findViewById(R.id.normal);
        hardbutton = findViewById(R.id.Hard);
        backbutton = findViewById(R.id.backbutton);

        //最初はどちらも無効化しておく
        normalbutton.setEnabled(false);
        hardbutton.setEnabled(false);

        //easyボタンを押したとき
        eazybutton.setOnClickListener((View v)->{
            onConditionNormal();
            startActivity(new Intent(this, MainActivity.class));
        });

        //normalボタンを押したとき
        normalbutton.setOnClickListener((View v)->{
            onConditionHard();
            startActivity(new Intent(this, MainActivity.class));
        });

        //hardボタンを押したとき
        hardbutton.setOnClickListener((View v)->{
            startActivity(new Intent(this, MainActivity.class));
        });

        //戻るボタンを押したとき
        backbutton.setOnClickListener((View v)->{
            startActivity(new Intent(this, TitleActivity.class));
        });

        //条件をクリアしたら有効可
        if(isConditionNormal){
            isConditionNormal = true;
            normalbutton.setEnabled(true);
        }

        //条件をクリアしたら有効可
        if(isConditionHard){
            isConditionHard = true;
            hardbutton.setEnabled(true);
        }

    }

    // 条件が満たされた場合に呼び出すメソッド
    private void onConditionNormal() {
        isConditionNormal = true;
        normalbutton.setEnabled(true);

        // 条件が満たされたことをSharedPreferencesに保存
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean("isConditionNormal", true);
        //editor.apply();

    }

    private void onConditionHard() {
        isConditionHard = true;
        hardbutton.setEnabled(true);

        // 条件が満たされたことをSharedPreferencesに保存
        //SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        //editor2.putBoolean("isConditionHard", true);
        //editor2.apply();
    }

}
