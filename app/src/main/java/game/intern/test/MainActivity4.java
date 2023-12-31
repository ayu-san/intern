package game.intern.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity4 extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    // 画像リソースの名前のリスト
    private static final String[] imageResourceNames = {
            "speed","power","energy","size"
    };

    public int g_InitSize = 0;
    private boolean isDialogVisible = false;
    private boolean isGameOver = false;
    private boolean isGameclear = false;
    private boolean isPauseDialog = true;
    private int dialogCount = 0;
    private  Player player;
    ArrayList<Enemy> Enemies;
    private final List<Integer> selectedImages = new ArrayList<>();
    private  GallLine gallLine;
    private float startX, startY;
    private long touchDownTime = 0;
    private Timer timer = new Timer();
    private final Handler handler = new Handler();
    private Drawable enemyeffect;
    private Drawable screeneffect;
    private Drawable chargeeffect1;
    private Drawable chargeeffect2;
    private Drawable chargeeffect3;
    private Drawable hiteffect;
    private Drawable goaleffect;
    private TapEffect tapEffect;
    private CollideEffect collideEffect;
    private ProgressBar levelBar;
    private TextView myLevelView;
    private ArrowView arrowView;
    private  int screenWidth;
    private  int screenHeight;
    private int experience;
    private int nowlevel;
    private ImageButton pauseButton;
    private SoundPlayer soundPlayer;
    private boolean gameStarted = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 画面の更新処理を行う
            handler.postDelayed(this, 1000); // 1000ミリ秒ごとに更新
        }
    };

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        sharedPreferences = getSharedPreferences("isCondition",MODE_PRIVATE);

        // ImageViewを取得
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);

        // 背景画像を設定
        backgroundImageView.setImageResource(R.drawable.back_ground_mountains);

        //画面サイズの取得
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        pauseButton = findViewById(R.id.pauseButton);

        soundPlayer = new SoundPlayer(this);

        // MyApplication から BGM と SE の音量を取得
        float initialBGMVolume = MyApplication.getBGMVolume();
        float initialSEVolume = MyApplication.getSEVolume();
        // SoundPlayer に初期音量を設定
        soundPlayer.setBGMVolume(0);
        soundPlayer.setSEVolume(initialSEVolume);

        View touchView = findViewById(R.id.startText);
        View blackView = findViewById(R.id.blackview);

        touchView.setOnTouchListener((v, event) -> {
            isPauseDialog = false;
            touchView.setVisibility(View.INVISIBLE);
            blackView.setVisibility(View.INVISIBLE);
            soundPlayer.setBGMVolume(initialBGMVolume);
            soundPlayer.setBGM(R.raw.stage4);//setBGM(R.raw.bgmtest2)
            gameStarted = true;
            return true;
        });

        screeneffect = ContextCompat.getDrawable(this, R.drawable.hiteffect1);
        enemyeffect = ContextCompat.getDrawable(this,R.drawable.hiteffect2);
        chargeeffect1 = ContextCompat.getDrawable(this, R.drawable.chargeeffect1);
        chargeeffect2 = ContextCompat.getDrawable(this, R.drawable.chargeeffect2);
        chargeeffect3 = ContextCompat.getDrawable(this, R.drawable.chargeeffect3);
        hiteffect = ContextCompat.getDrawable(this, R.drawable.hiteffect);
        goaleffect = ContextCompat.getDrawable(this, R.drawable.goaleffect);

        levelBar = findViewById(R.id.level);
        myLevelView = findViewById(R.id.myLevelView);
        nowlevel = 0;
        experience = 0;

        FrameLayout tapEffectContainer = findViewById(R.id.tap_effect);
        tapEffect = new TapEffect(this,tapEffectContainer);
        collideEffect = new CollideEffect(tapEffectContainer);
        tapEffectContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                tapEffect.show(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });


        arrowView = findViewById(R.id.arrow_view);

        //オブジェクト取得
        player = new Player();
        player.m_Texture = findViewById(R.id.character);
        player.m_PosX = screenWidth / 2.0f - 126.0f;
        player.m_PosY = screenHeight / 2.0f;
        player.m_Texture.setX( player.m_PosX );
        player.m_Texture.setY(player.m_PosY);
        player.SetMove(0.0f,0.0f);

        gallLine = new GallLine(screenHeight);
        gallLine.m_Texture = findViewById(R.id.gall);
        gallLine.m_Texture.setImageResource(R.drawable.goal);
        gallLine.m_Texture.setX(gallLine.m_PosX );
        gallLine.m_Texture.setY(gallLine.m_PosY);

        Enemies = new ArrayList<>();
        Enemies.add(new MillerEnemy(findViewById(R.id.enemy),(float)screenWidth / 8 * 3,0.0f,7.0f, 0, 0,2400.0f,400.0f));

        Enemies.add(new SideEnemy(findViewById(R.id.enemy4),screenWidth + 126.0f, (float)screenHeight /2, 0.0f, 5.0f, 10, 4, 1000.0f,200.0f,(float)screenWidth 	/5, -5.0f));

        Enemies.add(new Enemy(findViewById(R.id.enemy3),(float)screenWidth / 8 * 3,0.0f,7.0f, 12, 3,1100.0f,300.0f));

        Enemies.add(new MillerEnemy(findViewById(R.id.enemy1),(float)screenWidth / 8 * 4.5f,0.0f,7.0f, 14, 1,2400.0f,400.0f));
        Enemies.add(new MillerEnemy(findViewById(R.id.enemy2),(float)screenWidth / 8 * 1.5f,0.0f,7.0f, 15, 2 ,2400.0f,400.0f));

        Enemies.add(new SideEnemy(findViewById(R.id.enemy6),-390.0f, (float)screenHeight / 4, 0.0f, 5.0f, 30, 6, 1000.0f,300.0f,(float)screenWidth 	/5, 5.0f));
        Enemies.add(new SideEnemy(findViewById(R.id.enemy7),screenWidth + 126.0f, (float)screenHeight / 4, 0.0f, 5.0f, 30, 7, 1000.0f,300.0f,(float)screenWidth 	/9 * 5, -5.0f));

        Enemies.add(new MillerEnemy(findViewById(R.id.enemy5),(float)screenWidth / 8 * 3,0.0f,7.0f, 32, 5,2400.0f,400.0f));

        Enemies.add(new Enemy(findViewById(R.id.enemy8),-(float)screenWidth / 3,0.0f,7.0f, 35, 8,1100.0f,300.0f));

        Enemies.add(new MillerEnemy(findViewById(R.id.enemy9),(float)screenWidth / 8 * 4.5f,0.0f,7.0f, 47, 9,2400.0f,400.0f));
        Enemies.add(new MillerEnemy(findViewById(R.id.enemy11),(float)screenWidth / 8 * 1.5f,0.0f,7.0f, 48, 11,2400.0f,400.0f));

        Enemies.add(new Enemy(findViewById(R.id.enemy16),(float)screenWidth / 8 * 3,0.0f,7.0f, 50, 16,1100.0f,200.0f));

        Enemies.add(new MillerEnemy(findViewById(R.id.enemy12),(float)screenWidth / 8 * 7,-1.5f,7.0f, 53, 12,2400.0f,400.0f));
        Enemies.add(new MillerEnemy(findViewById(R.id.enemy13),-(float)screenWidth / 8,1.5f,7.0f, 54, 13,2400.0f,400.0f));

        Enemies.add(new ZigZagEnemy(findViewById(R.id.enemy14),(float)screenWidth / 2.6f,-5.0f,5.0f, 63, 14,1200.0f,600.0f));
        Enemies.add(new ZigZagEnemy(findViewById(R.id.enemy15),(float)screenWidth / 2.6f,5.0f,5.0f, 63, 15,1200.0f,600.0f));

        Enemies.add(new Stage4BossDragon(findViewById(R.id.enemy10),(float)screenWidth / 16 * 5,0.0f,7.0f, 65, 10,1000.0f,5000.0f));

        Enemies.add(new MillerEnemy(findViewById(R.id.enemy17),(float)screenWidth / 8 * 5,0.0f,7.0f, 67, 17,2400.0f,400.0f));
        Enemies.add(new MillerEnemy(findViewById(R.id.enemy18),(float)screenWidth / 8,0.0f,7.0f, 67, 18,2400.0f,400.0f));

        g_InitSize = Enemies.size();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> Update());
            }
        }, 0, 16);

        //ポーズを押したら
        pauseButton.setOnClickListener((View view)->{
            if (!isDialogVisible) {
                // ダイアログを表示
                soundPlayer.setSE(R.raw.decision1);
                showPauseDialog();
                isDialogVisible = true; // ダイアログが表示中であることをフラグで示す
            }
        });

        pauseButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    pauseButton.setScaleX(1.1f);
                    pauseButton.setScaleY(1.2f);
                    break;
                case MotionEvent.ACTION_UP:
                    // ボタンを離したときの処理
                    pauseButton.setScaleX(1.2f);
                    pauseButton.setScaleY(1.3f);
                    break;
            }
            return false;
        });

        player.m_Texture.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    player.m_MoveX = 0.0f;
                    player.m_MoveVecX = 0.0f;

                    player.m_MoveY = 0.0f;
                    player.m_MoveVecY = 0.0f;

                    touchDownTime = System.currentTimeMillis();
                    startX = event.getX();
                    startY = event.getY();
                    startTimer();
                    break;

                case MotionEvent.ACTION_MOVE:
                    long currentTime = System.currentTimeMillis();
                    long touchDuration = currentTime - touchDownTime;
                    changeColorBasedOnTouchLength(touchDuration);

                    arrowView.setArrow(player.m_PosX+(float)player.m_Texture.getWidth()/2,player.m_PosY+(float)player.m_Texture.getHeight()/2, player.m_PosX+event.getX(), player.m_PosY+event.getY());

                    break;

                case MotionEvent.ACTION_UP:
                    soundPlayer.setSE(R.raw.move);

                    player.m_Texture.clearColorFilter();
                    // 長押しの時間を計算
                    long currentTime2 = System.currentTimeMillis();
                    long touchDuration2 = currentTime2 - touchDownTime;

                    // 三段階の飛距離を計算
                    player.m_holdValue = calculateFlyDistance(touchDuration2);

                    stopTimer();
                    touchDownTime = 0; // タッチダウン時間をリセット

                    float endX = event.getX();
                    float endY = event.getY();

                    player.m_MoveVecX = endX - startX;
                    player.m_MoveVecY = endY - startY;


                    arrowView.setArrow(0, 0, 0, 0); // 矢印を非表示に

                    if(player.m_MoveVecX != 0.0 && player.m_MoveVecY != 0.0f)
                    {
                        normalizeVector(player, player.m_MoveVecX, player.m_MoveVecY);//ベクトルを正規化
                    }

                    //初めての移動
                    player.m_Speed = player.m_InitialSpeed * (player.m_holdValue * 0.7f);//スピードに初速を代入＋長押し効果
                    player.m_MoveX = player.m_MoveVecX * (player.m_Speed / 100.0f) * player.m_holdValue;
                    player.m_MoveY = player.m_MoveVecY * (player.m_Speed / 100.0f) * player.m_holdValue;
                    player.m_CollisionTimer = 0;

                break;
            }
            return true;
        });

    }

    // 別のアクティビティに移動する前に呼び出されるメソッド

    @Override
    protected void onStop() {
        super.onStop();
        if(!isPauseDialog) {
            // ダイアログを表示
            showPauseDialog();
            isDialogVisible = true; // ダイアログが表示中であることをフラグで示す
        }
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


    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    //関数
    private void changeColorBasedOnTouchLength(double touchLength) {

        if (touchLength < player.m_ChargeLevel) {
            // 短いタッチ：色を赤に変更
            collideEffect.collideEffectDelay((int) player.m_PosX + player.m_Texture.getWidth()/2, (int) player.m_PosY + player.m_Texture.getHeight()/2,chargeeffect1,player.m_Texture.getWidth()+80,player.m_Texture.getHeight()+80,250);
        } else if (touchLength < player.m_ChargeLevel*2) {
            // 中程度のタッチ：色を青に変更
            collideEffect.collideEffectDelay((int) player.m_PosX + player.m_Texture.getWidth()/2, (int) player.m_PosY + player.m_Texture.getHeight()/2,chargeeffect2,player.m_Texture.getWidth()+80,player.m_Texture.getHeight()+80,250);
        } else {
            // 長いタッチ：色を緑に変更
            collideEffect.collideEffectDelay((int) player.m_PosX + player.m_Texture.getWidth()/2, (int) player.m_PosY + player.m_Texture.getHeight()/2,chargeeffect3,player.m_Texture.getWidth()+80,player.m_Texture.getHeight()+80,250);
        }
    }

    private float calculateFlyDistance(long touchDuration) {
        if (touchDuration < player.m_ChargeLevel) {
            // 短い長押し：飛距離1
            return 1.5f;
        } else if (touchDuration < player.m_ChargeLevel* 2L) {
            // 中程度の長押し：飛距離2
            return 2.5f;
        } else {
            // 長い長押し：飛距離3
            return 3.5f;
        }
    }
    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long touchDuration = currentTime - touchDownTime;
                changeColorBasedOnTouchLength(touchDuration);
                handler.postDelayed(this, 100); // 100ミリ秒ごとにチェック
            }
        };
        handler.postDelayed(runnable, 100); // 最初の実行をスケジュール
    }

    private void stopTimer()
    {
        handler.removeCallbacks(runnable); // タイマーを停止
    }

    public void Update()
    {
        if(gameStarted) {
            String stageName;
            String resultText;
            if (!Enemies.isEmpty()) {
                for (int i = 0; i < g_InitSize; i++) {
                    int upSize = Enemies.size();
                    if (i <= upSize - 1) {
                        Enemies.get(i).PullCollisionTimer(Enemies.get(i));
                        Enemies.get(i).PullInvincivleTime(Enemies.get(i));//無敵時間を減らしていく。
                    }

                    if (i < 3)
                    {
                        player.PullCollisionTimer(player);
                    }

                    if (i <= upSize - 1) {
                        Enemies.get(i).MoveEnemy(Enemies.get(i), player, screenWidth);
                    }

                    if (i < 3)
                    {
                        changePosPlayer();
                    }

                    if (i < 3)
                    {
                        player.CollisionCirclePlayer(player, Enemies,collideEffect,hiteffect);
                    }


                    if (i <= upSize - 1) {
                        if(Enemies.get(i).CollisionCircleEnemy(player, Enemies.get(i),collideEffect,hiteffect))
                        {
                            //当たった
                            soundPlayer.setSE(R.raw.hit1);
                        }
                    }

                    if (gallLine.checkGall(gallLine, Enemies,collideEffect,goaleffect)) {
                        if(!isGameOver) {
                            soundPlayer.setBGM(R.raw.gameover);
                            stopTimer();
                            stageName = "";
                            resultText = "ゲームオーバー";

                            showResult(stageName, resultText);
                            isGameOver = true;
                        }
                    }

                    //画面外

                    hitCheck(player);
                    if (i <= upSize - 1) {
                        if(Enemies.get(i).hitCheckEnemy(Enemies, screenWidth, screenHeight,collideEffect,enemyeffect)){
                            soundPlayer.setSE(R.raw.burst1);
                            experience++;
                        }
                    }

                    updateLevelBar(experience);

                    changePos();
                }
            } else {
                //ゲームクリア
                if(!isGameclear) {
                    soundPlayer.setBGM(R.raw.gameclear);
                    stageName = "ステージ４";
                    resultText = "クリア！";
                    showResult(stageName, resultText);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isConditionHeaven", true);
                    editor.apply();
                    isGameclear = true;
                }
            }
        }
    }


    private void changePos()
    {
        if(!Enemies.isEmpty()) {
            for (int i = 0; i < Enemies.size(); i++) {
                Enemies.get(i).m_Texture.setX(Enemies.get(i).m_PosX);
                Enemies.get(i).m_Texture.setY(Enemies.get(i).m_PosY);
            }
        }

        //player.m_Texture.setX(player.m_PosX);
        //プレイヤー座標更新
        player.m_Texture.setX(player.m_PosX);
        player.m_Texture.setY(player.m_PosY);
    }

    private void changePosPlayer()
    {
        //前フレームの座標を保存しておく
        if(player.m_oldPosX != player.m_PosX)
        {
            //進んでいるなら更新
            player.m_oldPosX = player.m_PosX;
        }

        if(player.m_oldPosY != player.m_PosY)
        {
            player.m_oldPosY = player.m_PosY;
        }

        //限界地設定
        if(player.m_MoveX < -90.0f)
        {
            player.m_MoveX = -90.0f;
        }
        if(90.0f < player.m_MoveX)
        {
            player.m_MoveX = 90.0f;
        }
        if(player.m_MoveY < -90.0f)
        {
            player.m_MoveY = -90.0f;
        }
        if(90.0f < player.m_MoveY)
        {
            player.m_MoveY = 90.0f;
        }

        player.m_PosX += player.m_MoveX;
        player.m_PosY += player.m_MoveY;

        //スピードが0でないのなら
        if(0.0f < player.m_Speed)

        {
            player.m_Speed -= 10.0; //減衰率
            if(0 < player.m_CollisionTimer)
            {
                player.m_Speed = 0.0f;
            } else {
                player.m_MoveX = player.m_MoveVecX * (player.m_Speed / 100.0f) * player.m_holdValue;
                player.m_MoveY = player.m_MoveVecY * (player.m_Speed / 100.0f) * player.m_holdValue;
            }
        }
        else
        {
            player.m_Speed = 0.0f;
            if(0 == player.m_CollisionTimer)
            {
                player.m_MoveX = player.m_MoveVecX * (player.m_Speed / 100.0f) * player.m_holdValue;
                player.m_MoveY = player.m_MoveVecY * (player.m_Speed / 100.0f) * player.m_holdValue;
            }
        }

    }

    //画面外判定
    private void hitCheck(GameObject gameObject)
    {
        long durations = 500;

        //右
        if(gameObject.m_PosX + gameObject.m_Texture.getWidth() > screenWidth)
        {
            gameObject.m_PosX = screenWidth - gameObject.m_Texture.getWidth();
            if(0.0f < player.m_Speed)
            {//敵からぶつかってこられたとき
                collideEffect.collideEffect(screenWidth, (int) (player.m_PosY +130),screeneffect,350,350,durations);
                gameObject.m_MoveX *= -1.0;
                player.m_MoveVecX *= -1.0;
            }
            else
            {
                gameObject.m_MoveX *= -0.1;
                player.m_MoveVecX *= -0.1;
            }
            soundPlayer.setSE(R.raw.se_reflection);
        }

        //左
        if(gameObject.m_PosX < 0)
        {
            gameObject.m_PosX = 0;
            if(0.0f < player.m_Speed)
            {//敵からぶつかってこられたとき
                collideEffect.collideEffect(0, (int) (player.m_PosY +130),screeneffect,350,350,durations);
                gameObject.m_MoveX *= -1.0;
                player.m_MoveVecX *= -1.0;
            }
            else
            {
                gameObject.m_MoveX *= -0.1;
                player.m_MoveVecX *= -0.1;
            }
            soundPlayer.setSE(R.raw.se_reflection);
        }

        //上
        if(gameObject.m_PosY < 0) {
            gameObject.m_PosY = 0;
            if(0.0f < player.m_Speed)
            {//敵からぶつかってこられたとき
                collideEffect.collideEffect((int) (player.m_PosX +130), 0,screeneffect,350,350,durations);
                gameObject.m_MoveY *= -1.0;
                player.m_MoveVecY *= -1.0;
            }
            else
            {
                gameObject.m_MoveY *= -0.1;
                player.m_MoveVecY *= -0.1;
            }
            soundPlayer.setSE(R.raw.se_reflection);
        }

        //下
        if(gameObject.m_PosY + gameObject.m_Texture.getHeight() > screenHeight)
        {
            gameObject.m_PosY = screenHeight - gameObject.m_Texture.getHeight();
            if(0.0f < player.m_Speed)
            {//敵からぶつかってこられたとき
                collideEffect.collideEffect((int) (player.m_PosX +130), screenHeight,screeneffect,350,350,durations);
                gameObject.m_MoveY *= -1.0;
                player.m_MoveVecY *= -1.0;
            }
            else
            {
                gameObject.m_MoveY *= -0.1;
                player.m_MoveVecY *= -0.1;
            }
            soundPlayer.setSE(R.raw.se_reflection);
        }

    }
    private float calculateLength(float x, float y)
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalizeVector(GameObject gameobject, float x, float y)
    {
        double length = calculateLength(x, y); //ベクトルの長さを計算
        gameobject.m_MoveVecX /= length;
        gameobject.m_MoveVecY /= length;
    }

    @SuppressLint("SetTextI18n")
    private void updateLevelBar(int value){
        int maxExperience = levelBar.getMax();
        if(value >= maxExperience){
            experience = 0;
            levelBar.setProgress(0);
            nowlevel++;
            myLevelView.setText("レベル : " + nowlevel);
            showLevelUp();
        }else {
            levelBar.setProgress(value);
        }

    }

    public void showPauseDialog() {
        isPauseDialog = true;
        dialogCount++;
        // ダイアログを表示するコード
        // タイマーを停止
        timer.cancel();
        timer.purge(); // タイマーのキューをクリア

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pause, null);
        //ダイアログビューの設定
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        //AlertDialogを表示
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
        alertDialog.show();

        //BGM.SE設定
        SeekBar seekBarbgm = dialogView.findViewById(R.id.BGMseekBarPause);
        SeekBar seekBarse = dialogView.findViewById(R.id.SEseekBarPause);

        // シークバーの初期値を設定
        seekBarbgm.setProgress((int) (MyApplication.getBGMVolume() * 100));
        seekBarse.setProgress((int) (MyApplication.getSEVolume() * 100));

        seekBarbgm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //BGMの音量設定
                float bgmvolume = progress / 100.0f;
                MyApplication.setBGMVolume(bgmvolume);
                soundPlayer.setBGMVolume(bgmvolume);
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

        seekBarse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //BGMの音量設定
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

        //リトライ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button retry = dialogView.findViewById(R.id.retry);
        setupButtonTouchEffect(retry);
        retry.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            soundPlayer.setSE(R.raw.decision1);
            startActivity(new Intent(this, MainActivity4.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //ステージ選択へ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoStageSelect = dialogView.findViewById(R.id.gotoStageSelect);
        setupButtonTouchEffect(gotoStageSelect);
        gotoStageSelect.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            soundPlayer.setSE(R.raw.decision1);
            startActivity(new Intent(this, SelectActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //タイトルへ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoTitle = dialogView.findViewById(R.id.gotoTitle);
        setupButtonTouchEffect(gotoTitle);
        gotoTitle.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            soundPlayer.setSE(R.raw.decision1);
            startActivity(new Intent(this, TitleActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //戻る
        Button closeButton = dialogView.findViewById(R.id.closeButtonPause);
        setupButtonTouchEffect(closeButton);
        closeButton.setOnClickListener((View view) -> {
            soundPlayer.setSE(R.raw.cancel1);
            alertDialog.dismiss(); // ダイアログを閉じる

            if(dialogCount == 1) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> Update());
                    }
                }, 0, 16);
            }

        });

        alertDialog.setOnDismissListener(dialog -> {
            isDialogVisible = false;
            isPauseDialog = false;
            dialogCount--;
        });

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


    private void setRandomImageForImageButton(ImageButton imageButton) {
        // ランダムな画像を選択
        Random random = new Random();
        int randomIndex;

        do {
            randomIndex = random.nextInt(imageResourceNames.length);
        } while (selectedImages.contains(randomIndex)); // 既に選択済みの場合、再選択

        selectedImages.add(randomIndex); // 選択済みリストに追加

        String randomImageResourceName = imageResourceNames[randomIndex];
        @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(randomImageResourceName, "drawable", getPackageName());

        imageButton.setImageResource(resID);
        imageButton.setTag(resID);
    }

    //レベルアップ画面表示関数
    @SuppressLint("ClickableViewAccessibility")
    public void showLevelUp(){
        dialogCount++;
        // タイマーを停止
        timer.cancel();
        timer.purge(); // タイマーのキューをクリア

        //設定ダイアログの読み込み
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_levelup,null);

        //ダイアログビューの設定
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(false);

        //AlertDialogを表示
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
        alertDialog.show();

        ImageButton item1 = dialogView.findViewById(R.id.item1);
        ImageButton item2 = dialogView.findViewById(R.id.item2);
        Button noselect = dialogView.findViewById(R.id.noSelect);
        TextView oldLevel = dialogView.findViewById(R.id.oldLevel);
        TextView newLevel = dialogView.findViewById(R.id.newLevel);

        oldLevel.setText(String.valueOf(nowlevel - 1));
        newLevel.setText(String.valueOf(nowlevel));

        item1.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    item1.setScaleX(0.95f);
                    item1.setScaleY(0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                    // ボタンを離したときの処理
                    item1.setScaleX(1.0f);
                    item1.setScaleY(1.0f);
                    break;
            }
            return false;
        });

        item2.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // ボタンをタッチしたときの処理
                    item2.setScaleX(0.95f);
                    item2.setScaleY(0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                    // ボタンを離したときの処理
                    item2.setScaleX(1.0f);
                    item2.setScaleY(1.0f);
                    break;
            }
            return false;
        });

        setupButtonTouchEffect(noselect);

        setRandomImageForImageButton(item1);
        setRandomImageForImageButton(item2);

        // 新しいサイズを設定
        ViewGroup.LayoutParams params = player.m_Texture.getLayoutParams();

        item1.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.decision1);

            // ImageButtonの現在の画像を取得
            int currentDrawableId = (int) item1.getTag();

            //画像によって処理を変える
            if(currentDrawableId == R.drawable.speed){
                player.m_InitialSpeed *= 1.2f;
            } else if (currentDrawableId == R.drawable.power) {
                player.m_Weight *= 1.2f;
            }else if (currentDrawableId == R.drawable.energy) {
                player.m_ChargeLevel *= 0.8f;
            }else if (currentDrawableId == R.drawable.size) {
                params.width += 30.0f;
                params.height += 30.0f;
                player.m_Texture.setLayoutParams(params);
            }
            alertDialog.dismiss();

            if(dialogCount == 1) {
                timer = new Timer();// タイマーを再生成
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> Update());
                    }
                }, 0, 16);
            }

        });

        item2.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.decision1);

            // ImageButtonの現在の画像を取得
            int currentDrawableId = (int) item2.getTag();

            //画像によって処理を変える
            if(currentDrawableId == R.drawable.speed){
                player.m_InitialSpeed *= 1.2f;
            } else if (currentDrawableId == R.drawable.power) {
                player.m_Weight *= 1.2f;
            }else if (currentDrawableId == R.drawable.energy) {
                player.m_ChargeLevel *= 0.8f;
            }else if (currentDrawableId == R.drawable.size) {
                params.width += 30.0f;
                params.height += 30.0f;
                player.m_Texture.setLayoutParams(params);
            }
            alertDialog.dismiss();

            if(dialogCount == 1) {
                timer = new Timer();// タイマーを再生成
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> Update());
                    }
                }, 0, 16);
            }

        });

        noselect.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.cancel1);
            alertDialog.dismiss();

            if(dialogCount == 1) {
                timer = new Timer();// タイマーを再生成
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> Update());
                    }
                }, 0, 16);
            }

        });
        alertDialog.setOnDismissListener(dialog ->{
            dialogCount--;
            selectedImages.clear();
        });

    }

    public void showResult(String stagename,String resulttext){
        isPauseDialog = true;
        dialogCount++;

        timer.cancel();
        timer.purge(); // タイマーのキューをクリア

        //設定ダイアログの読み込み
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_result,null);

        //ダイアログビューの設定
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.NoDimDialog);
        builder.setView(dialogView)
                .setCancelable(false);

        //AlertDialogを表示
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

        Button retrybutton = dialogView.findViewById(R.id.result_retry);
        Button selectbutton = dialogView.findViewById(R.id.result_selectstage);
        Button titlebutton = dialogView.findViewById(R.id.result_title);
        TextView stage = dialogView.findViewById(R.id.stagename);
        TextView result = dialogView.findViewById(R.id.result);

        setupButtonTouchEffect(retrybutton);
        setupButtonTouchEffect(selectbutton);
        setupButtonTouchEffect(titlebutton);

        stage.setText(stagename);
        result.setText(resulttext);

        //リトライボタン
        retrybutton.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.decision1);
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, MainActivity4.class));
        });

        //ステージ選択ボタン
        selectbutton.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.decision1);
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, SelectActivity.class));
        });

        //タイトルボタン
        titlebutton.setOnClickListener(view -> {
            soundPlayer.setSE(R.raw.decision1);
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, TitleActivity.class));
        });

        alertDialog.setOnDismissListener(dialog -> {
            isDialogVisible = false;
            isPauseDialog = false;
            dialogCount--;
        });

    }
}


