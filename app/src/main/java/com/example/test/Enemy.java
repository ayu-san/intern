package com.example.test;
import static java.lang.Double.isNaN;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class Enemy extends GameObject
{
    float m_InitialSpeed = 1200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_ConstMoveX;//m_Moveの初期化状態を保持する変数
    float m_ConstMoveY;//m_Moveの初期化状態を保持する変数

    boolean m_IsPlayerCollision;

    Enemy(ImageView texture,float posX, float moveX, float moveY, int delayTime, int index)
    {
        m_Texture = texture;
        m_IsPlayerCollision = false;

        m_PosX = posX;
        m_PosY = -300.0f;

        m_oldPosX = m_PosX;
        m_oldPosY = m_PosY;

        m_Texture.setX(m_PosX);
        m_Texture.setY(m_PosY);

        m_MoveX = moveX;
        m_MoveY = moveY;

        m_ConstMoveX = m_MoveX;
        m_ConstMoveY = m_MoveY;

        m_DisplayTimer = delayTime;

        m_Index = index;
    }

    int m_DisplayTimer; //出現Deray時間
    int m_Index; //ArrayListのインデックス

    public void SetPlayerCollision()
    {
        m_IsPlayerCollision = true;
    }

    public void MoveEnemy(Enemy enemy, GameObject target, int width)
    {
        if (enemy.m_CollisionTimer == 0) {
            enemy.m_MoveX = target.m_Texture.getX() - enemy.m_Texture.getX();
            enemy.m_MoveY = target.m_Texture.getY() - enemy.m_Texture.getY();
            if (enemy.m_MoveX != 0.0f && enemy.m_MoveY != 0.0f && !isNaN(enemy.m_MoveX) && !isNaN(enemy.m_MoveY)) {
                normalizeVectorEnemy(enemy, enemy.m_MoveX, enemy.m_MoveY);
            }
            //ベクトルを正規化

            //移動
            if (0.0f < enemy.m_Speed) {
                enemy.m_Speed -= 10.0f;

                enemy.m_MoveX = enemy.m_MoveX * (enemy.m_Speed / 100.0f);
                enemy.m_MoveY = enemy.m_MoveY * (enemy.m_Speed / 100.0f);
            } else {
                enemy.m_MoveX = 0.0f;
                enemy.m_MoveY = 0.0f;

                enemy.m_Speed = enemy.m_InitialSpeed;
            }
            //enemy.m_MoveX = 3.0f;
            //enemy.m_MoveY = 0.0f;
        }

        if (0 < m_DisplayTimer) {
            m_DisplayTimer--;
        } else {
            m_DisplayTimer = 0;
            //座標更新
            if (enemy.m_oldPosX != enemy.m_PosX) {
                //進んでいるなら更新
                enemy.m_oldPosX = enemy.m_PosX;
            }

            if (enemy.m_oldPosY != enemy.m_PosY) {
                enemy.m_oldPosY = enemy.m_PosY;
            }

            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        }

        //reflect(enemy , width);
    }

    public boolean isCoinCideEnemy(Enemy enemy, GameObject target)
    {
        {
            int radius = target.m_Texture.getHeight() /2;
            radius += 20.0f;

            float oldenemyX = enemy.m_oldPosX + (float)enemy.m_Texture.getWidth()/2;
            float oldenemyY = enemy.m_oldPosY + (float)enemy.m_Texture.getHeight()/2;

            float playerX = target.m_PosX + (float)target.m_Texture.getWidth()/2;
            float playerY = target.m_PosY + (float)target.m_Texture.getHeight()/2 - 40.0f;
            float enemyX  = enemy.m_PosX + (float)enemy.m_Texture.getWidth()/2;
            float enemyY  = enemy.m_PosY + (float)enemy.m_Texture.getHeight()/2;

            float oldDX = oldenemyX - playerX;
            float oldDY = oldenemyY - playerY;

            float dx = enemyX - playerX;
            float dy = enemyY - playerY;
            float calc = (float) Math.sqrt(dx * dx + dy * dy);
            float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

            if(radius < oldcalc && calc <= radius)
            { //当たった
                //補正
                return true;
            }
        }
        return false;
    }


    private float calculateLength(float x, float y)
    {
        return (float) Math.sqrt(x * x + y * y);
    }
    
   public void normalizeVectorEnemy(GameObject gameobject, float x, float y)
   {
       double length = calculateLength(x, y); //ベクトルの長さを計算
       gameobject.m_MoveX /= length;
       gameobject.m_MoveY /= length;
   }
    public void SetConstValue(float x, float y)
    {
        m_ConstMoveX = x;
        m_ConstMoveY = y;
    }

    //画面外判定
    public boolean hitCheckEnemy(ArrayList<Enemy> enemies, Player player, int screenwidth, int screenheight, CollideEffect collideEffect, Drawable drawable) {
        long duration = 500;
        //右
        if(!enemies.isEmpty())//リストが空ではない
        {
            //for (int i = enemies.size() - 1; i >= 0; i--) {
            for (int i = 0; i < enemies.size(); i++) {
                if(enemies.get(i).m_IsPlayerCollision) {
                    if (enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth() > screenwidth)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,350,350,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                    //左
                    else if (enemies.get(i).m_PosX < 0)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,350,350,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                    //上
                    else if (enemies.get(i).m_PosY < 0)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,350,350,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                    //下
                    else if (enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight() > screenheight)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,350,350,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void SetTimer(int time)
    {
        m_DisplayTimer = time;
    }
    public void CollisionCircleEnemy(Player player, ArrayList<Enemy> enemies)
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            int radius = player.m_Texture.getHeight() /2;
            radius += 50.0f;

            float oldenemyX = enemies.get(i).m_oldPosX + (float)enemies.get(i).m_Texture.getWidth()/2;
            float oldenemyY = enemies.get(i).m_oldPosY + (float)enemies.get(i).m_Texture.getHeight()/2;

            float playerX = player.m_PosX + (float)player.m_Texture.getWidth()/2;
            float playerY = player.m_PosY + (float)player.m_Texture.getHeight()/2 - 40.0f;
            float enemyX  = enemies.get(i).m_PosX + (float)enemies.get(i).m_Texture.getWidth()/2;
            float enemyY  = enemies.get(i).m_PosY + (float)enemies.get(i).m_Texture.getHeight()/2;

            float oldDX = oldenemyX - playerX;
            float oldDY = oldenemyY - playerY;

            float dx = enemyX - playerX;
            float dy = enemyY - playerY;
            float calc = (float) Math.sqrt(dx * dx + dy * dy);
            float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

            if(radius < oldcalc && calc <= radius)
            { //当たった
                //めり込まないように補正する
                enemies.get(i).m_PosX = enemies.get(i).m_oldPosX;
                enemies.get(i).m_PosY = enemies.get(i).m_oldPosY;

                m_IsPlayerCollision = true;
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex;
                float ex2;

                if (energy1 != 0.0f && energy2 != 0.0f) {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else //Playerのスピードが0の時にも入る
                {
                    ex = 1.0f;
                    ex2 = 5.0f;
                }

                float preserveMoveX = enemies.get(i).m_MoveX;
                float preserveMoveY = enemies.get(i).m_MoveY;

                //正規化処理
                if(player.m_MoveX == 0.0f && player.m_MoveX == 0.0f)
                {
                    float vecX = -(player.m_PosX - enemies.get(i).m_PosX);
                    float vecY = -(player.m_PosY - enemies.get(i).m_PosY);

                    float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);

                    if (vectorLength > 0.0f) {
                        // ベクトルの長さが0でない場合に正規化を行う
                        float normalizedX = vecX / vectorLength;
                        float normalizedY = vecY / vectorLength;

                        // ベクトルの反転を保持したまま正規化されたベクトルを使用
                        enemies.get(i).m_MoveX = normalizedX;
                        enemies.get(i).m_MoveY = normalizedY;
                    } else {
                        // ベクトルの長さが0の場合は正規化を行えません
                        // 長さが0の場合、ベクトルの方向は定義できません
                        // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
                        enemies.get(i).m_MoveX = 0.0f; // 例: デフォルト値を0に設定
                        enemies.get(i).m_MoveY = 0.0f;
                    }
                }
                else
                {
                    if (!isNaN(player.m_MoveX * ex / 10))
                        enemies.get(i).m_MoveX = player.m_MoveX * ex / 10;

                    if (!isNaN(player.m_MoveY * ex / 10))
                        enemies.get(i).m_MoveY = player.m_MoveY * ex / 10;

                }

                if (!isNaN(ex2)) {
                    player.m_MoveX = preserveMoveX * ex2 / 10;
                    player.m_MoveY = preserveMoveY * ex2 / 10;
                }
            }
        }
    }
    
    @Override
    //エネミーのoldPosを使っていく エネミーからプレイヤーにぶつかっていく
    public void collisionTest(Player player, ArrayList<Enemy> enemies)
    {
        if(!enemies.isEmpty())//リストが空ではない
        {
            for (int i = 0; i < enemies.size(); i++) {
                //右判定
                //プレイヤー左
            if (enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && player.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                    && enemies.get(i).m_PosY < player.m_PosY + player.m_Texture.getHeight()
                    && player.m_PosX + player.m_Texture.getWidth() < enemies.get(i).m_oldPosX)
            {
                m_IsPlayerCollision = true;
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex;
                float ex2;

                if (energy1 != 0.0f && energy2 != 0.0f) {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else //Playerのスピードが0の時にも入る
                {
                    ex = 1.0f;
                    ex2 = 1.0f;
                }

                float preserveMoveX = enemies.get(i).m_MoveX;
                float preserveMoveY = enemies.get(i).m_MoveY;

                if (!isNaN(player.m_MoveX * ex / 10))
                    enemies.get(i).m_MoveX = player.m_MoveX * ex / 10;

                if (!isNaN(player.m_MoveY * ex / 10))
                    enemies.get(i).m_MoveY = player.m_MoveY * ex / 10;

                if (!isNaN(ex2)) {
                    player.m_MoveX = preserveMoveX * ex2 / 10;
                    player.m_MoveY = preserveMoveY * ex2 / 10;
                }
            }

                //プレイヤー右
            if(player.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                    && player.m_Texture.getY() < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                    && enemies.get(i).m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight()
                    && enemies.get(i).m_oldPosX + enemies.get(i).m_Texture.getWidth() < player.m_PosX)
            {
                m_IsPlayerCollision = true;
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex;
                float ex2;

                if (energy1 != 0.0f && energy2 != 0.0f) {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else //Playerのスピードが0の時にも入る
                {
                    ex = 1.0f;
                    ex2 = 1.0f;
                }

                float preserveMoveX = enemies.get(i).m_MoveX;
                float preserveMoveY = enemies.get(i).m_MoveY;

                if (!isNaN(player.m_MoveX * ex / 10))
                    enemies.get(i).m_MoveX = player.m_MoveX * ex / 10;

                if (!isNaN(player.m_MoveY * ex / 10))
                    enemies.get(i).m_MoveY = player.m_MoveY * ex / 10;

                if (!isNaN(ex2)) {
                    player.m_MoveX = preserveMoveX * ex2 / 10;
                    player.m_MoveY = preserveMoveY * ex2 / 10;
                }
            }
            /*
            //エネミー上
            if(player.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                    && player.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                    && enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()  < player.m_oldPosY)
            {
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex = energy1 / energy2;
                float ex2 = energy2 / energy1;

                enemies.get(i).m_MoveX = player.m_MoveX * ex / 10;
                enemies.get(i).m_MoveY = player.m_MoveY * ex / 10;

                player.m_MoveX = enemies.get(i).m_MoveX * ex2 / 10;
                player.m_MoveY = enemies.get(i).m_MoveY * ex2 / 10;
                //player.m_MoveY *= -1;
                //player.m_MoveVecY *= -1;

            }

            //エネミー下
            if(enemies.get(i).m_PosY < player.m_PosY + player.m_Texture.getHeight() - 10.0f
                    && player.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                    && enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && player.m_oldPosY + player.m_Texture.getHeight()  < enemies.get(i).m_PosY)
            {
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex = energy1 / energy2;
                float ex2 = energy2 / energy1;

                enemies.get(i).m_MoveX = player.m_MoveX * ex / 10;
                enemies.get(i).m_MoveY = player.m_MoveY * ex / 10;

                player.m_MoveX = enemies.get(i).m_MoveX * ex2 / 10;
                player.m_MoveY = enemies.get(i).m_MoveY * ex2 / 10;
                //player.m_MoveY *= -1;
                //player.m_MoveVecY *= -1;

            }
             */

            }
        } else {

        }
    }


}
