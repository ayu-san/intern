package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //定数を定義
    final float OFFSET_POINT = 70.0f;
    //オブジェクト
    //private ImageView player.m_Texture;
    private int test;
    //private ImageView enemy;
    Enemy enemy;
    Player player;

    private boolean isCollision = false;
    private float startX, startY;
    private long touchDownTime = 0;
    private int enemyCollisionCount = 0;
    private boolean isCollisionHandled = false;
    private static final int animationDuration = 500; //ミリ単位のアニメーション時間
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private  int screenWidth;
    private  int screenHeight;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 画面の更新処理を行う
            handler.postDelayed(this, 1000); // 1000ミリ秒ごとに更新
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ImageViewを取得
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);

        // 背景画像を設定
        backgroundImageView.setImageResource(R.drawable.background_image);

        //画面サイズの取得
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //オブジェクト取得
        player = new Player();
        player.m_Texture = findViewById(R.id.character);
        player.m_Texture.setX(screenWidth /  2.0f - player.m_Texture.getWidth()/2);
        player.m_Texture.setY(screenHeight / 1.5f);

        enemy = new Enemy();
        enemy.m_PosX = screenWidth / 3.2f;
        enemy.m_PosY = screenHeight / 4;
        enemy.SetSpeed(10.0f,10.0f);

        //enemy = findViewById(R.id.enemy);
        //enemy.setX(screenWidth / 3.2f);
        //enemy.setY(screenHeight / 4);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Update();
                    }
                });
            }
        }, 0, 20);

        TextView enemyCollisionCountTextView = findViewById(R.id.enemy_collision_count);
        player.m_Texture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownTime = System.currentTimeMillis();
                        startX = event.getX();
                        startY = event.getY();
                        startTimer();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        long currentTime = System.currentTimeMillis();
                        long touchDuration = currentTime - touchDownTime;
                        changeColorBasedOnTouchLength(touchDuration);

                        if(checkCollisionWithEnemy()){
                            // TextViewを更新
                            enemyCollisionCountTextView.setText("敵に当たった回数: " + enemyCollisionCount);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        player.m_Texture.clearColorFilter();
                        isCollisionHandled = false;
                        // 長押しの時間を計算
                        long currentTime2 = System.currentTimeMillis();
                        long touchDuration2 = currentTime2 - touchDownTime;

                        // 三段階の飛距離を計算
                        float flyDistance = calculateFlyDistance(touchDuration2);

                        stopTimer();
                        touchDownTime = 0; // タッチダウン時間をリセット

                        float endX = event.getX();
                        float endY = event.getY();

                        //フリック方向計算
                        float deltaX = endX - startX; //X軸方向の移動距離
                        float deltaY = endY - startY; //Y軸方向の移動距離

                        float moveX = deltaX * flyDistance; //X軸方向の移動ベクトル
                        float moveY = deltaY * flyDistance; //Y軸方向の移動ベクトル

                        // 移動ベクトルの長さを計算
                        float moveVectorLength = (float) Math.sqrt(moveX * moveX + moveY * moveY);

                        // 移動ベクトルの長さが一定の値（例: 200.0f）以上の場合に制限
                        if (moveVectorLength > 300.0f) {
                            float scaleFactor = 300.0f / moveVectorLength;
                            moveX *= scaleFactor;
                            moveY *= scaleFactor;
                        }

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

                        //X軸方向の移動アニメーション
                        ObjectAnimator moveXAnimator = ObjectAnimator.ofFloat(player.m_Texture, "translationX", newX);
                        moveXAnimator.setDuration(animationDuration); //アニメーションの時間を設定
                        moveXAnimator.start(); //アニメーション開始

                        //Y軸方向の移動アニメーション
                        ObjectAnimator moveYAnimator = ObjectAnimator.ofFloat(player.m_Texture, "translationY", newY);
                        moveYAnimator.setDuration(animationDuration); //アニメーションの時間を設定
                        moveYAnimator.start(); //アニメーション開始
                        break;
                }
                return true;
            }
        });

    }

    //関数
    private void changeColorBasedOnTouchLength(double touchLength) {
        if (touchLength < 1000) {
            // 短いタッチ：色を赤に変更
            player.m_Texture.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (touchLength < 2000) {
            // 中程度のタッチ：色を青に変更
            player.m_Texture.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        } else {
            // 長いタッチ：色を緑に変更
            player.m_Texture.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
    }

    private float calculateFlyDistance(long touchDuration) {
        if (touchDuration < 1000) {
            // 短い長押し：飛距離1
            return 1.5f;
        } else if (touchDuration < 2000) {
            // 中程度の長押し：飛距離2
            return 3.0f;
        } else {
            // 長い長押し：飛距離3
            return 5.0f;
        }
    }

    class Enemy extends GameObject
    {
    }

    class GameObject
    {
        protected ImageView m_Texture = findViewById(R.id.enemy);
        protected float m_PosX;
        protected float m_PosY;

        protected float m_SpeedX;
        protected float m_SpeedY;

        public void SetSpeed(float x,float y)
        {
            m_SpeedX = x;
            m_SpeedY = y;
        }

    }

    class Player extends GameObject
    {
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

    private void stopTimer() {
        handler.removeCallbacks(runnable); // タイマーを停止
    }

    // プレイヤーキャラクターと敵キャラクターの当たり判定を検出
    private boolean checkCollisionWithEnemy() {
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

                enemy.m_Texture.setX(player.m_Texture.getX());
                enemy.m_Texture.setY(player.m_Texture.getY());

                // 敵に当たった回数をインクリメント
                enemyCollisionCount++;
            }

            return true;
        }

        return false; // 仮の戻り値
    }

    public void Update()
    {
        changePos();
        hitcheck();
        collisionTest();
    }

    private void changePos()
    {
        //enemy.m_PosX += enemy.m_SpeedX;
        //enemy.m_PosY += enemy.m_SpeedY;

        enemy.m_Texture.setX(enemy.m_PosX);
        enemy.m_Texture.setY(enemy.m_PosY);

        //player.m_Texture.setX(player.m_PosX);
    }

    private void hitcheck()
    {
        //右
        if(enemy.m_PosX + enemy.m_Texture.getWidth() > screenWidth + OFFSET_POINT)
        {
            enemy.m_SpeedX *= -1;
        }

        //左
        if(enemy.m_PosX < 0 - OFFSET_POINT)
        {
            enemy.m_SpeedX *= -1;
        }

        //上
        if(enemy.m_PosY < 0) {
            enemy.m_SpeedY *= -1;
        }

        //下
        if(enemy.m_PosY + enemy.m_Texture.getHeight() > screenHeight)
        {
            enemy.m_SpeedY *= -1;
        }

    }

    private void collisionTest()
    {
        //右判定
        //エネミー左
        if(player.m_Texture.getX()< enemy.m_PosX + enemy.m_Texture.getWidth()
        && player.m_Texture.getY() < enemy.m_PosY + enemy.m_Texture.getHeight()
        && enemy.m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight())
        {
            //enemy.m_SpeedX *= -1;
            enemy.m_PosX += -100.0f;
            player.m_Texture.setX(player.m_Texture.getX() + 100.0f);
            isCollision = true;
        }
    }



}


