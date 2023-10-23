package com.example.test;

import android.widget.ImageView;

public class InVisibleEnemy extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 100.0f;
    float m_Speed = 0.0f;
    float m_Weight = 1500.0f; //重さ
    private boolean isStopped = false;
    private long stopStartTime = 0;
    private float currentAlpha = 1.0f; // 現在の透明度

    InVisibleEnemy(ImageView texture, float posX, float moveX, float moveY, int delayTime, int index,float speed, float weight) {
        super(texture,posX,moveX,moveY,delayTime,index, speed,weight);//基底クラスのコンストラクタ呼び出し
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width) {
        if (enemy.m_CollisionTimer == 0) {
            if (isStopped) {
                // 停止時間（ミリ秒）
                long stopDuration = 3000;
                if (System.currentTimeMillis() - stopStartTime >= stopDuration) {
                    isStopped = false;
                }
            } else {
                if (currentAlpha < 1.0f) {
                    // 透明度を元に戻す
                    currentAlpha = 1.0f;
                    enemy.m_Texture.setAlpha(currentAlpha);
                }

                if (enemy.m_MoveX != 0.0f || enemy.m_MoveY != 0.0f) {
                    normalizeVectorEnemy(enemy, enemy.m_MoveX, enemy.m_MoveY);
                }

                if (0.0f < enemy.m_Speed) {
                    enemy.m_Speed -= 25.0f;

                    enemy.m_MoveX = enemy.m_MoveX * (enemy.m_Speed / 100.0f);
                    enemy.m_MoveY = enemy.m_MoveY * (enemy.m_Speed / 100.0f);
                } else {
                    enemy.m_MoveX = m_ConstMoveX;
                    enemy.m_MoveY = m_ConstMoveY;

                    enemy.m_Speed = enemy.m_InitialSpeed;

                    // 一定時間経過後、敵を停止
                    isStopped = true;
                    stopStartTime = System.currentTimeMillis();
                }
            }
        }

        if (m_DisplayTimer > 0) {
            m_DisplayTimer--;
        }

        if (!isStopped) {
            enemy.m_oldPosX = enemy.m_PosX;
            enemy.m_oldPosY = enemy.m_PosY;
            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        } else {
            // 敵が停止中は透明度を変化させる
            if (currentAlpha > 0.1f) {
                currentAlpha -= 0.01f; // 透明度を徐々に下げる
                enemy.m_Texture.setAlpha(currentAlpha);
            }
        }
    }
}