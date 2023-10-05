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


public class MainActivity extends AppCompatActivity {

    //オブジェクト
    private ImageView character;
    private ImageView enemy;
    private float startX, startY;
    private long touchDownTime = 0;
    private int enemyCollisionCount = 0;
    private boolean isCollisionHandled = false;
    private static final int animationDuration = 500; //ミリ単位のアニメーション時間

    private Handler handler = new Handler();
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
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //オブジェクト取得
        character = findViewById(R.id.character);
        character.setX(screenWidth / 3.2f);
        character.setY(screenHeight / 1.5f);

        enemy = findViewById(R.id.enemy);
        enemy.setX(screenWidth / 3.2f);
        enemy.setY(screenHeight / 4);

        TextView enemyCollisionCountTextView = findViewById(R.id.enemy_collision_count);


        character.setOnTouchListener(new View.OnTouchListener() {
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
                        character.clearColorFilter();
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

                        //character.setRotation((float)angleDegrees);

                        //X軸方向の制限（画面端）
                        float newX = character.getTranslationX() + moveX2;
                        if(newX < -50){
                            newX = -50;
                        } else if (newX > screenWidth - character.getWidth() + 50) {
                            newX = screenWidth - character.getWidth() + 50;
                        }

                        //Y軸方向の制限（画面端）
                        float newY = character.getTranslationY() + moveY2;
                        if(newY < -150){
                            newY = -150;
                        } else if (newY > screenHeight - character.getHeight()) {
                            newY = screenHeight - character.getHeight();
                        }

                        //X軸方向の移動アニメーション
                        ObjectAnimator moveXAnimator = ObjectAnimator.ofFloat(character, "translationX", newX);
                        moveXAnimator.setDuration(animationDuration); //アニメーションの時間を設定
                        moveXAnimator.start(); //アニメーション開始

                        //Y軸方向の移動アニメーション
                        ObjectAnimator moveYAnimator = ObjectAnimator.ofFloat(character, "translationY", newY);
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
            character.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (touchLength < 2000) {
            // 中程度のタッチ：色を青に変更
            character.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        } else {
            // 長いタッチ：色を緑に変更
            character.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
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
                (int) character.getX(),
                (int) character.getY(),
                (int) (character.getX() + character.getWidth() - 150),
                (int) (character.getY() + character.getHeight() - 150)
        );

        // 敵キャラクターの矩形領域を取得（敵の座標とサイズに合わせて調整が必要）
        Rect enemyRect = new Rect(
                (int) enemy.getX(),
                (int) enemy.getY(),
                (int) (enemy.getX() + enemy.getWidth() - 150),
                (int) (enemy.getY() + enemy.getHeight() - 150)
        );

// プレイヤーキャラクターと敵キャラクターの矩形領域が重なっているか判定
        if (playerRect.intersect(enemyRect)) {
            // 衝突が発生した場合の処理をここに記述

            if (!isCollisionHandled && playerRect.intersect(enemyRect)) {
                // 一度当たったことをマーク
                isCollisionHandled = true;

                enemy.setX(character.getX());
                enemy.setY(character.getY());

                // 敵に当たった回数をインクリメント
                enemyCollisionCount++;
            }

            return true;
        }

        return false; // 仮の戻り値
    }



}


