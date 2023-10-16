package com.example.test;
import static java.lang.Double.isNaN;

import android.widget.ImageView;

import com.example.test.GameObject;
import com.example.test.MainActivity;

public class Enemy extends GameObject
{
    float m_InitialSpeed = 1200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ

    float m_ConstMoveX;
    float m_ConstMoveY;

    int m_DisplayTimer;

    public void MoveEnemy(MainActivity main, Enemy enemy, GameObject target, int width)
    {
        if(enemy.m_CollisionTimer == 0)
        {
            enemy.m_MoveX = target.m_Texture.getX() - enemy.m_Texture.getX();
            enemy.m_MoveY = target.m_Texture.getY() - enemy.m_Texture.getY();
            if(enemy.m_MoveX != 0.0f && enemy.m_MoveY != 0.0f && !isNaN(enemy.m_MoveX) && !isNaN(enemy.m_MoveY))
            {
                    main.normalizeVectorEnemy(enemy, enemy.m_MoveX,enemy.m_MoveY);
            }
            //ベクトルを正規化

            //移動
            if(0.0f < enemy.m_Speed)
            {
                enemy.m_Speed -= 10.0f;

                enemy.m_MoveX = enemy.m_MoveX * (enemy.m_Speed / 100.0f);
                enemy.m_MoveY = enemy.m_MoveY * (enemy.m_Speed / 100.0f);
            } else
            {
                enemy.m_MoveX = 0.0f;
                enemy.m_MoveY = 0.0f;

                enemy.m_Speed = enemy.m_InitialSpeed;
            }
            //enemy.m_MoveX = 3.0f;
            //enemy.m_MoveY = 0.0f;
        }

        if(0 < m_DisplayTimer)
        {
            m_DisplayTimer--;
        } else
        {
            m_DisplayTimer = 0;
            //座標更新
            enemy.m_oldPosX = enemy.m_PosX;
            enemy.m_oldPosY = enemy.m_PosY;

            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        }

        //reflect(enemy , width);
    }
    public void SetConstValue(float x, float y)
    {
        m_ConstMoveX = x;
        m_ConstMoveY = y;
    }

    public void SetTimer(int time)
    {
        m_DisplayTimer = time;
    }

    @Override
    //エネミーのoldPosを使っていく エネミーからプレイヤーにぶつかっていく
    public void collisionTest(Player player, Enemy[] enemy)
    {
        for (int i = 0; i < enemy.length; i++)
        {
            //右判定
            //プレイヤー左
            if(enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && player.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_PosY + player.m_Texture.getHeight()
                    && player.m_PosX + player.m_Texture.getWidth() < enemy[i].m_oldPosX)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex;
                float ex2;

                if(energy1 != 0.0f && energy2 != 0.0f)
                {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                }
                else //Playerのスピードが0の時にも入る
                {
                    ex = 1.0f;
                    ex2 = 1.0f;
                }

                float preserveMoveX = enemy[i].m_MoveX;
                float preserveMoveY = enemy[i].m_MoveY;

                if(!isNaN(player.m_MoveX * ex / 10))
                    enemy[i].m_MoveX = player.m_MoveX * ex / 10;

                if(!isNaN(player.m_MoveY * ex / 10))
                    enemy[i].m_MoveY = player.m_MoveY * ex / 10;

                if(!isNaN(ex2))
                {
                    player.m_MoveX = preserveMoveX * ex2 / 10;
                    player.m_MoveY = preserveMoveY * ex2 / 10;
                }
            }
            /*
            //エネミー右
            if(enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3
                    && player.m_Texture.getY() < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight()
                    && player.m_oldPosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3 < enemy[i].m_PosX)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;
                float ex2 = energy2 / energy1;

                enemy[i].m_MoveX = player.m_MoveX * ex / 10;
                enemy[i].m_MoveY = player.m_MoveY * ex / 10;

                player.m_MoveX = enemy[i].m_MoveX * ex2 / 10;
                player.m_MoveY = enemy[i].m_MoveY * ex2 / 10;
                //player.m_MoveX *= -1;
                //player.m_MoveVecX *= -1;

            }

            //エネミー上
            if(player.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && player.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && enemy[i].m_PosY + enemy[i].m_Texture.getHeight()  < player.m_oldPosY)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;
                float ex2 = energy2 / energy1;

                enemy[i].m_MoveX = player.m_MoveX * ex / 10;
                enemy[i].m_MoveY = player.m_MoveY * ex / 10;

                player.m_MoveX = enemy[i].m_MoveX * ex2 / 10;
                player.m_MoveY = enemy[i].m_MoveY * ex2 / 10;
                //player.m_MoveY *= -1;
                //player.m_MoveVecY *= -1;

            }

            //エネミー下
            if(enemy[i].m_PosY < player.m_PosY + player.m_Texture.getHeight() - 10.0f
                    && player.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && player.m_oldPosY + player.m_Texture.getHeight()  < enemy[i].m_PosY)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;
                float ex2 = energy2 / energy1;

                enemy[i].m_MoveX = player.m_MoveX * ex / 10;
                enemy[i].m_MoveY = player.m_MoveY * ex / 10;

                player.m_MoveX = enemy[i].m_MoveX * ex2 / 10;
                player.m_MoveY = enemy[i].m_MoveY * ex2 / 10;
                //player.m_MoveY *= -1;
                //player.m_MoveVecY *= -1;

            }
             */

        }
    }






}
