package com.example.test;
import com.example.test.GameObject;

import static java.lang.Double.TYPE;
import static java.lang.Double.isNaN;

public class Player extends GameObject
{
    float m_InitialSpeed = 700.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_holdValue;
    int m_ChargeLevel = 0;//チャージ速度

    public void SetSpeed(float sp)
    {
        m_Speed = sp;
    }

    @Override
    public void collisionTest(Player player, Enemy[] enemy)
    {
        for (int i = 0; i < enemy.length; i++)
        {
            //右判定
            //エネミー左
            if(player.m_PosX + player.m_Texture.getWidth() /3 < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && player.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_PosY + player.m_Texture.getHeight()
                    && enemy[i].m_PosX + enemy[i].m_Texture.getWidth() < player.m_oldPosX + player.m_Texture.getWidth() /3 )
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
                } else
                {
                    ex = 0.3f;
                    ex2 = 0.3f;
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

                //player.m_MoveX *= -1;
                //player.m_MoveVecX *= -1;
            }

            //エネミー右
            if(enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3
                    && player.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_PosY + player.m_Texture.getHeight()
                    && player.m_oldPosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3 < enemy[i].m_PosX)
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
                } else
                {
                    ex = 0.3f;
                    ex2 = 0.3f;
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

                float ex;
                float ex2;

                if(energy1 != 0.0f && energy2 != 0.0f)
                {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else
                {
                    ex = 0.3f;
                    ex2 = 0.3f;
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

                float ex;
                float ex2;

                if(energy1 != 0.0f && energy2 != 0.0f)
                {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else
                {
                    ex = 0.3f;
                    ex2 = 0.3f;
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
                //player.m_MoveY *= -1;
                //player.m_MoveVecY *= -1;

            }


        }
    }


}
