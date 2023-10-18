package com.example.test;

import static java.lang.Double.isNaN;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {
    //定数を定義
    final float OFFSET_POINT = 70.0f;

    //オブジェクト
    //private ImageView player.m_Texture;
    //private ImageView enemy;

    // 画像リソースの名前のリスト
    private static final String[] imageResourceNames = {
            "speed","power","energy","heavy"
    };

    private boolean isDialogVisible = false;

    private  Player player;
    ArrayList<Enemy> Enemies;
    private  GallLine gallLine;
    private float startX, startY;
    private long touchDownTime = 0;
    private final int enemyCollisionCount = 0;
    //ミリ単位のアニメーション時間
    private Timer timer = new Timer();
    private final Handler handler = new Handler();
    private TapEffect tapEffect;
    private ArrowView arrowView;
    private  int screenWidth;
    private  int screenHeight;
    private ImageButton pauseButton;
    private SoundPlayer soundPlayer;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 画面の更新処理を行う
            handler.postDelayed(this, 1000); // 1000ミリ秒ごとに更新
        }
    };

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // ImageViewを取得
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);

        // 背景画像を設定
        backgroundImageView.setImageResource(R.drawable.back_ground_desert);

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
        soundPlayer.setBGMVolume(initialBGMVolume);
        soundPlayer.setSEVolume(initialSEVolume);



        FrameLayout tapEffectContainer = findViewById(R.id.tap_effect);
        tapEffect = new TapEffect(this,tapEffectContainer);
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
        player.m_PosX = screenWidth /  2.0f - player.m_Texture.getWidth()/2;
        player.m_PosY = screenHeight / 1.5f;
        player.m_Texture.setX( player.m_PosX );
        player.m_Texture.setY(player.m_PosY);
        player.SetMove(0.0f,0.0f);

        gallLine = new GallLine(screenHeight);
        gallLine.m_Texture = findViewById(R.id.gall);
        gallLine.m_Texture.setX(gallLine.m_PosX );
        gallLine.m_Texture.setY(gallLine.m_PosY);

        Enemies = new ArrayList<>();
        Enemies.add(new VerticalEnemy(findViewById(R.id.enemy),screenWidth / 3 * 2,-0.5f,7.0f, 180, 0));
        Enemies.add(new VerticalEnemy(findViewById(R.id.enemy1),screenWidth / 8,0.5f,7.0f, 180, 0));
        Enemies.add(new ChaseEnemy(findViewById(R.id.enemy2),screenWidth / 5 * (3),0.0f,7.0f, 700, 1));
        Enemies.add(new VerticalEnemy2(findViewById(R.id.enemy3),screenWidth / 7 * (4),0.0f,7.0f, 1200,2));
        //Enemies.add(new Enemy(findViewById(R.id.enemy),screenWidth / 5,0.0f,0.0f, 90, 0));
        //Enemies.add(new VerticalEnemy(findViewById(R.id.enemy1),screenWidth / 5 * (1 * 3),0.0f,7.0f, 90700, 1));
        //Enemies.add(new VerticalEnemy(findViewById(R.id.enemy2),screenWidth / 5 * (4),0.0f,7.0f, 901200,2));

        //ImageView texture,float posX, float moveX, float moveY, int delayTime

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


        TextView enemyCollisionCountTextView = findViewById(R.id.enemy_collision_count);
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

                    arrowView.setArrow(player.m_PosX+130,player.m_PosY+130, player.m_PosX+event.getX(), player.m_PosY+event.getY());

                    if(checkCollisionWithEnemy()){
                        // TextViewを更新
                        enemyCollisionCountTextView.setText("レベル : " + enemyCollisionCount);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    player.m_Texture.clearColorFilter();
                    // 長押しの時間を計算
                    long currentTime2 = System.currentTimeMillis();
                    long touchDuration2 = currentTime2 - touchDownTime;

                    // 三段階の飛距離を計算
                    float flyDistance = calculateFlyDistance(touchDuration2);
                    player.m_holdValue = flyDistance;

                    stopTimer();
                    touchDownTime = 0; // タッチダウン時間をリセット

                    float endX = event.getX();
                    float endY = event.getY();

                    //フリック方向計算
                    float deltaX = endX - startX; //X軸方向の移動距離
                    float deltaY = endY - startY; //Y軸方向の移動距離

                    player.m_MoveVecX = endX - startX;
                    player.m_MoveVecY = endY - startY;

                    float moveX = deltaX * flyDistance; //X軸方向の移動ベクトル
                    float moveY = deltaY * flyDistance; //Y軸方向の移動ベクトル

                    arrowView.setArrow(0, 0, 0, 0); // 矢印を非表示に

                    /*
                    // 移動ベクトルの長さを計算
                    float moveVectorLength = (float) Math.sqrt(moveX * moveX + moveY * moveY);

                    // 移動ベクトルの長さが一定の値（例: 200.0f）以上の場合に制限
                    if (moveVectorLength > 300.0f) {
                        float scaleFactor = 300.0f / moveVectorLength;
                        moveX *= scaleFactor;
                        moveY *= scaleFactor;
                    }
                    *
                     */

                    if(player.m_MoveVecX != 0.0 && player.m_MoveVecY != 0.0f)
                    {
                        normalizeVector(player, player.m_MoveVecX, player.m_MoveVecY);//ベクトルを正規化
                    }

                    //初めての移動
                    player.m_Speed = player.m_InitialSpeed * (player.m_holdValue * 0.7f);//スピードに初速を代入＋長押し効果
                    player.m_MoveX = player.m_MoveVecX * (player.m_Speed / 100.0f) * player.m_holdValue;
                    player.m_MoveY = player.m_MoveVecY * (player.m_Speed / 100.0f) * player.m_holdValue;
                    //player.m_MoveX *= 7.0f;
                    //player.m_MoveY *= 7.0f; //仮調整

                    //長押し効果を加える

                    float moveX2 = moveX * flyDistance; //X軸方向の移動ベクトル
                    float moveY2 = moveY * flyDistance; //Y軸方向の移動ベクトル

                    //double angleRadians = Math.atan2(deltaY,deltaX);
                    //double angleDegrees = Math.toDegrees(angleRadians);

                    //player.m_Texture.setRotation((float)angleDegrees);

                    //X軸方向の制限（画面端）
                    float newX = player.m_Texture.getTranslationX() + moveX2;
                    //if (isCollision)
                {
                    //newX = player.m_Texture.getTranslationX();
                }
                if(newX < -50){
                    newX = -50;
                } else if (newX > screenWidth - player.m_Texture.getWidth() + 50) {
                    newX = screenWidth - player.m_Texture.getWidth() + 50;
                }

                //Y軸方向の制限（画面端）
                float newY = player.m_Texture.getTranslationY() + moveY2;
                //if (isCollision)
                {
                    // newY  = player.m_Texture.getTranslationY();
                }
                if(newY < -150){
                    newY = -150;
                } else if (newY > screenHeight - player.m_Texture.getHeight()) {
                    newY = screenHeight - player.m_Texture.getHeight();
                }

                break;
            }
            return true;
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isDialogVisible) {
            // ダイアログを表示
            showPauseDialog();
            isDialogVisible = true; // ダイアログが表示中であることをフラグで示す
        }
    }

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    //関数
    private void changeColorBasedOnTouchLength(double touchLength) {
        if (touchLength < 1000) {
            // 短いタッチ：色を赤に変更
            player.m_Texture.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        } else if (touchLength < 2000) {
            // 中程度のタッチ：色を青に変更
            player.m_Texture.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        } else {
            // 長いタッチ：色を緑に変更
            player.m_Texture.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }
    }

    private float calculateFlyDistance(long touchDuration) {
        if (touchDuration < 1000) {
            // 短い長押し：飛距離1
            return 1.5f;
        } else if (touchDuration < 2000) {
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

    // プレイヤーキャラクターと敵キャラクターの当たり判定を検出
    private boolean checkCollisionWithEnemy() {
        /*
        // プレイヤーキャラクターの矩形領域を取得
        Rect playerRect = new Rect(
                (int) player.m_Texture.getX(),
                (int) player.m_Texture.getY(),
                (int) (player.m_Texture.getX() + player.m_Texture.getWidth()),
                (int) (player.m_Texture.getY() + player.m_Texture.getHeight())
        );

        // 敵キャラクターの矩形領域を取得（敵の座標とサイズに合わせて調整が必要）
        Rect enemyRect = new Rect(
                (int) enemy.m_Texture.getX(),
                (int) enemy.m_Texture.getY(),
                (int) (enemy.m_Texture.getX() + enemy.m_Texture.getWidth()),
                (int) (enemy.m_Texture.getY() + enemy.m_Texture.getHeight())
        );

// プレイヤーキャラクターと敵キャラクターの矩形領域が重なっているか判定
        if (playerRect.intersect(enemyRect)) {
            // 衝突が発生した場合の処理をここに記述

            if (!isCollisionHandled && playerRect.intersect(enemyRect)) {
                // 一度当たったことをマーク
                isCollisionHandled = true;

                //enemy.m_Texture.setX(player.m_Texture.getX());
                //enemy.m_Texture.setY(player.m_Texture.getY());

                // 敵に当たった回数をインクリメント
                enemyCollisionCount++;
            }

            return true;
        }

        *
         */
        return true; // 仮の戻り値

    }

    public void Update()
    {
        String stageName;
        String resultText;
        if(!Enemies.isEmpty())
        {
            for (int i = 0; i < Enemies.size(); i++) {
                Enemies.get(i).PullCollisionTimer(Enemies.get(i));
                player.PullCollisionTimer(player);

                Enemies.get(i).MoveEnemy(Enemies.get(i), player, screenWidth);
                changePosPlayer();

                player.CollisionCirclePlayer(player, Enemies);
                Enemies.get(i).CollisionCircleEnemy(player, Enemies);

                if(gallLine.checkGall(gallLine, Enemies)){
                    stageName = "";
                    resultText = "ゲームオーバー";
                    showResult(stageName, resultText);
                }

                //画面外
                hitCheck(player);
                Enemies.get(i).hitCheckEnemy(Enemies, player, screenWidth, screenHeight);

                changePos();
            }
        } else
        {
            //ゲームクリア
            stageName = "ステージ２";
            resultText = "クリア！";
            showResult(stageName, resultText);
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
        //右
        if(gameObject.m_PosX + gameObject.m_Texture.getWidth() > screenWidth)
        {
            gameObject.m_PosX = screenWidth - gameObject.m_Texture.getWidth();
            gameObject.m_MoveX *= -1;
            player.m_MoveVecX *= -1;
        }

        //左
        if(gameObject.m_PosX < 0)
        {
            gameObject.m_PosX = 0;
            gameObject.m_MoveX *= -1;
            player.m_MoveVecX *= -1;
        }

        //上
        if(gameObject.m_PosY < 0) {
            gameObject.m_PosY = 0;
            gameObject.m_MoveY *= -1;
            player.m_MoveVecY *= -1;
        }

        //下
        if(gameObject.m_PosY + gameObject.m_Texture.getHeight() > screenHeight)
        {
            gameObject.m_PosY = screenHeight - gameObject.m_Texture.getHeight();
            gameObject.m_MoveY *= -1;
            player.m_MoveVecY *= -1;
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

    public void showPauseDialog() {
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

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //リトライ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button retry = dialogView.findViewById(R.id.retry);
        setupButtonTouchEffect(retry);
        retry.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, MainActivity2.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //ステージ選択へ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoStageSelect = dialogView.findViewById(R.id.gotoStageSelect);
        setupButtonTouchEffect(gotoStageSelect);
        gotoStageSelect.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, SelectActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //タイトルへ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoTitle = dialogView.findViewById(R.id.gotoTitle);
        setupButtonTouchEffect(gotoTitle);
        gotoTitle.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, TitleActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //戻る
        Button closeButton = dialogView.findViewById(R.id.closeButtonPause);
        setupButtonTouchEffect(closeButton);
        closeButton.setOnClickListener((View view)->{
            alertDialog.dismiss(); // ダイアログを閉じる

            timer = new Timer();// タイマーを再生成
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> Update());
                }
            }, 0, 16);

        });

        alertDialog.setOnDismissListener(dialog -> isDialogVisible = false);

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
        int randomIndex = random.nextInt(imageResourceNames.length);
        String randomImageResourceName = imageResourceNames[randomIndex];

        // リソースIDを取得
        @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(randomImageResourceName, "drawable", getPackageName());

        // ImageButtonに画像を設定
        imageButton.setImageResource(resID);
    }

    //レベルアップ画面表示関数
    @SuppressLint("ClickableViewAccessibility")
    public void showLevelUp(){
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

        item1.setOnClickListener(view -> {
            // ImageButtonの現在の画像を取得
            Drawable currentDrawable = item1.getDrawable();

            //画像によって処理を変える
            if(currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.speed).getConstantState())){

            } else if (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.power).getConstantState())) {

            }
            alertDialog.dismiss();

            timer = new Timer();// タイマーを再生成
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> Update());
                }
            }, 0, 16);

        });

        item2.setOnClickListener(view -> {
            // ImageButtonの現在の画像を取得
            Drawable currentDrawable = item2.getDrawable();

            //画像によって処理を変える
            if(currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.speed).getConstantState())){

            } else if (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.power).getConstantState())) {

            }
            alertDialog.dismiss();

            timer = new Timer();// タイマーを再生成
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> Update());
                }
            }, 0, 16);

        });

        noselect.setOnClickListener(view -> {
            alertDialog.dismiss();

            timer = new Timer();// タイマーを再生成
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(MainActivity2.this::Update);
                }
            }, 0, 16);

        });

    }

    public void showResult(String stagename,String resulttext){
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
            soundPlayer.setTestSE();
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, MainActivity2.class));
        });

        //ステージ選択ボタン
        selectbutton.setOnClickListener(view -> {
            soundPlayer.setTestSE();
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, SelectActivity.class));
        });

        //タイトルボタン
        titlebutton.setOnClickListener(view -> {
            soundPlayer.setTestSE();
            alertDialog.dismiss(); // ダイアログを閉じる
            startActivity(new Intent(this, TitleActivity.class));
        });

    }


}

