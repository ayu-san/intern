package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class TitleActivity extends AppCompatActivity {

    private Button startButton;
    private Button settingButton;
    private Button asobiButton;
    private Button cregitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        startButton = findViewById(R.id.startbutton);
        settingButton = findViewById(R.id.settingbutton);
        asobiButton = findViewById(R.id.asobibutton);
        cregitButton = findViewById(R.id.cregitbutton);

        //始めるボタンを押したとき
        startButton.setOnClickListener((View v)->{
            startActivity(new Intent(this, SelectActivity.class));
        });

        //設定ボタンを押したとき
        settingButton.setOnClickListener((View v)->{
            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            //ポーズ画面ダイアログを設定したときの例
            /*
            //リトライ
            // ダイアログ内のボタンにクリックリスナーを設定
            Button retry = dialogView.findViewById(R.id.retry);
            retry.setOnClickListener((View view)->{
                // ボタンが押されたときの処理
                startActivity(new Intent(this, TitleActivity.class));
                alertDialog.dismiss(); // ダイアログを閉じる
            });

            //ステージ選択へ
            // ダイアログ内のボタンにクリックリスナーを設定
            Button gotoStageSelect = dialogView.findViewById(R.id.gotoStageSelect);
            gotoStageSelect.setOnClickListener((View view)->{
                // ボタンが押されたときの処理
                startActivity(new Intent(this, SelectActivity.class));
                alertDialog.dismiss(); // ダイアログを閉じる
            });

            //タイトルへ
            // ダイアログ内のボタンにクリックリスナーを設定
            Button gotoTitle = dialogView.findViewById(R.id.gotoTitle);
            gotoTitle.setOnClickListener((View view)->{
                // ボタンが押されたときの処理
                startActivity(new Intent(this, TitleActivity.class));
                alertDialog.dismiss(); // ダイアログを閉じる
            });
            */

        });

        //遊び方ボタンを押したとき
        asobiButton.setOnClickListener((View v)->{
            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_asobi,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        //クレジットボタンを押したとき
        cregitButton.setOnClickListener((View v)->{
            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cregit,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }

    public void closeDialog(View view) {
        // ダイアログを閉じる
        if (view != null) {
            AlertDialog alertDialog = (AlertDialog) view.getTag();
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }
}
