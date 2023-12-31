package game.intern.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import game.intern.test.R;

public class TitleActivity extends AppCompatActivity {

    private SoundPlayer soundPlayer;

    private TapEffect tapEffect;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button startButton = findViewById(R.id.startbutton);
        Button settingButton = findViewById(R.id.settingbutton);
        Button asobiButton = findViewById(R.id.asobibutton);
        Button cregitButton = findViewById(R.id.cregitbutton);

        ImageView titlLogo = findViewById(R.id.titlelogo);
        Animation floatAnimation  = AnimationUtils.loadAnimation(this,R.anim.scale_up_down);
        titlLogo.startAnimation(floatAnimation);

        soundPlayer = new SoundPlayer(this);

        // MyApplication から BGM と SE の音量を取得
        float initialBGMVolume = MyApplication.getBGMVolume();
        float initialSEVolume = MyApplication.getSEVolume();
        // SoundPlayer に初期音量を設定
        soundPlayer.setBGMVolume(initialBGMVolume);
        soundPlayer.setSEVolume(initialSEVolume);

        soundPlayer.setBGM(R.raw.title);

        FrameLayout tapEffectContainer = findViewById(R.id.tap_effect);
        tapEffect = new TapEffect(this,tapEffectContainer);

        tapEffectContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                tapEffect.show(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });

        setupButtonTouchEffect(startButton);
        setupButtonTouchEffect(settingButton);
        setupButtonTouchEffect(asobiButton);
        setupButtonTouchEffect(cregitButton);

        //始めるボタンを押したとき
        startButton.setOnClickListener((View v)->{

            soundPlayer.setSE(R.raw.decision1);

            startActivity(new Intent(this, SelectActivity.class));
        });

        //設定ボタンを押したとき
        settingButton.setOnClickListener((View v)->{

            soundPlayer.setSE(R.raw.decision1);

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
            alertDialog.show();

            SeekBar seekBarBGM = dialogView.findViewById(R.id.BGMseekBar);
            SeekBar seekBarSE = dialogView.findViewById(R.id.SEseekBar);

            // シークバーの初期値を設定
            seekBarBGM.setProgress((int) (MyApplication.getBGMVolume() * 100));
            seekBarSE.setProgress((int) (MyApplication.getSEVolume() * 100));

            seekBarBGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //BGMの音量設定
                    float bgmvolume = progress / 100.0f;
                    MyApplication.setBGMVolume(bgmvolume);
                    soundPlayer.setBGMVolume(bgmvolume);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // シークバーの操作が開始されたときの処理
                    soundPlayer.setSE(R.raw.decision1);
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // シークバーの操作が終了したときの処理
                    soundPlayer.setSE(R.raw.decision1);
                }
            });

            seekBarSE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //SEの音量設定
                    float sevolume = progress / 100.0f;
                    MyApplication.setSEVolume(sevolume);
                    soundPlayer.setSEVolume(sevolume);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    soundPlayer.setSE(R.raw.decision1);
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    soundPlayer.setSE(R.raw.decision1);
                }
            });

            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonSetting);
            setupButtonTouchEffect(closeButton);
            closeButton.setOnClickListener((View view)->{
                soundPlayer.setSE(R.raw.cancel1);

                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

        //遊び方ボタンを押したとき
        asobiButton.setOnClickListener((View v)->{

            soundPlayer.setSE(R.raw.decision1);

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_asobi,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
            alertDialog.show();

            ImageView practice = dialogView.findViewById(R.id.imageView2);
            Button page1button = dialogView.findViewById(R.id.page1button);
            Button page2button = dialogView.findViewById(R.id.page2button);
            Button page3button = dialogView.findViewById(R.id.page3button);
            CheckBox checkBox = dialogView.findViewById(R.id.checkBox);

            checkBox.setVisibility(View.INVISIBLE);

            page1button.setOnClickListener((View view) -> {
                soundPlayer.setSE(R.raw.decision1);
                page1button.setVisibility(View.INVISIBLE);
                page2button.setVisibility(View.VISIBLE);
                page3button.setVisibility(View.VISIBLE);
                practice.setImageResource(R.drawable.asobi1);
            });

           page2button.setOnClickListener((View view) -> {
               soundPlayer.setSE(R.raw.decision1);
               page1button.setVisibility(View.VISIBLE);
               page2button.setVisibility(View.INVISIBLE);
               page3button.setVisibility(View.VISIBLE);
               practice.setImageResource(R.drawable.asobi2);
            });

           page3button.setOnClickListener((View view) -> {
               soundPlayer.setSE(R.raw.decision1);
               page1button.setVisibility(View.VISIBLE);
               page2button.setVisibility(View.VISIBLE);
               page3button.setVisibility(View.INVISIBLE);
               practice.setImageResource(R.drawable.asobi3);
            });


            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonAsobi);
            setupButtonTouchEffect(closeButton);
            closeButton.setOnClickListener((View view)->{
                soundPlayer.setSE(R.raw.cancel1);

                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

        //クレジットボタンを押したとき
        cregitButton.setOnClickListener((View v)->{

            soundPlayer.setSE(R.raw.decision1);

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cregit,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
            alertDialog.show();

            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonCregit);
            setupButtonTouchEffect(closeButton);
            closeButton.setOnClickListener((View view)->{
                soundPlayer.setSE(R.raw.cancel1);

                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

    }
    // ボタンクリック音を再生するメソッド

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    @Override
    protected void onPause() {
        super.onPause();

        // BGMとSEの現在の音量を取得
        float currentBGMVolume = soundPlayer.getBGMVolume();
        float currentSEVolume = soundPlayer.getSEVolume();

        // BGMとSEの現在の音量を保存
        MyApplication.saveCurrentBGMVolume(currentBGMVolume);
        MyApplication.saveCurrentSEVolume(currentSEVolume);

        // BGMの一時停止
        soundPlayer.pauseBGM();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // BGMの再生再開
        soundPlayer.resumeBGM();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // アクティビティが破棄されるときに音声をリリース
        soundPlayer.release();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButtonTouchEffect(Button button) {
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    button.setScaleX(0.95f);
                    button.setScaleY(0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                    // ボタンを離したときの処理
                    button.setScaleX(1.0f);
                    button.setScaleY(1.0f);
                    break;
            }
            return false;
        });
    }
}
