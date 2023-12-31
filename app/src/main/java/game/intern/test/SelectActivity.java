package game.intern.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;



public class SelectActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private SoundPlayer soundPlayer;
    private TapEffect tapEffect;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //sharedPreferencesの初期化
        sharedPreferences = getSharedPreferences("isCondition", MODE_PRIVATE);

        //sharedPreferencesの設定
        boolean isConditionNormal = sharedPreferences.getBoolean("isConditionNormal", false);
        boolean isConditionHard = sharedPreferences.getBoolean("isConditionHard", false);
        boolean isConditionHell = sharedPreferences.getBoolean("isConditionHell", false);
        boolean isConditionHeaven = sharedPreferences.getBoolean("isConditionHeaven", false);

        ImageButton eazybutton = findViewById(R.id.eazy);
        ImageButton normalbutton = findViewById(R.id.normal);
        ImageButton hardbutton = findViewById(R.id.Hard);
        ImageButton hellbutton = findViewById(R.id.hell);
        ImageButton heavenbutton = findViewById(R.id.heaven);
        ImageButton backbutton = findViewById(R.id.backbutton);

        ImageView logo = findViewById(R.id.selectlogo);
        Animation floatAnimation  = AnimationUtils.loadAnimation(this,R.anim.float_left_right);
        logo.startAnimation(floatAnimation);


        setupButtonTouchEffect(eazybutton);
        setupButtonTouchEffect(normalbutton);
        setupButtonTouchEffect(hardbutton);
        setupButtonTouchEffect(hellbutton);
        setupButtonTouchEffect(heavenbutton);

        backbutton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    backbutton.setScaleX(0.9f);
                    backbutton.setScaleY(0.9f);
                    break;
                case MotionEvent.ACTION_UP:
                    // ボタンを離したときの処理
                    backbutton.setScaleX(1.0f);
                    backbutton.setScaleY(1.0f);
                    break;
            }
            return false;
        });

        ScrollView scrollView = findViewById(R.id.scroll);
        scrollView.setScrollbarFadingEnabled(false);

        // ボタンの初期状態を設定
        setButtonState(normalbutton, isConditionNormal);
        setButtonState(hardbutton, isConditionHard);
        setButtonState(hellbutton, isConditionHell);
        setButtonState(heavenbutton, isConditionHeaven);

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

        if(isConditionNormal){
            normalbutton.setBackgroundResource(R.drawable.stage2button);
        }
        if(isConditionHard){
            hardbutton.setBackgroundResource(R.drawable.stage3button);
        }
        if(isConditionHell){
            hellbutton.setBackgroundResource(R.drawable.stage4button);
        }
        if(isConditionHeaven){
            heavenbutton.setBackgroundResource(R.drawable.stage5button);
        }

        //easyボタンを押したとき
        eazybutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.decision1);
            startActivity(new Intent(this, MainActivity.class));
        });

        //normalボタンを押したとき
        normalbutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.decision1);

            startActivity(new Intent(this, MainActivity2.class));
        });

        //hardボタンを押したとき
        hardbutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.decision1);

            startActivity(new Intent(this, MainActivity3.class));
        });

        //hellボタンを押したとき
        hellbutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.decision1);

            startActivity(new Intent(this, MainActivity4.class));
        });

        //heavenボタンを押したとき
        heavenbutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.decision1);

            startActivity(new Intent(this, MainActivity5.class));
        });

        //戻るボタンを押したとき
        backbutton.setOnClickListener((View v)->{
            soundPlayer.setSE(R.raw.cancel1);

            startActivity(new Intent(this, TitleActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    // ボタンの有効/無効状態を設定
    private void setButtonState(ImageButton button, boolean isEnabled) {
        button.setEnabled(isEnabled);
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
    private void setupButtonTouchEffect(ImageButton button) {
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    button.setScaleX(0.95f);
                    button.setScaleY(0.95f);
                    break;
                case MotionEvent.ACTION_CANCEL:
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
