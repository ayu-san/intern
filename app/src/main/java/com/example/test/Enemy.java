package com.example.test;
import com.example.test.GameObject;
import com.example.test.MainActivity;

public class Enemy extends GameObject
{
    float m_InitialSpeed = 700.0f;
    float m_Speed = 0.0f;
    float m_Weight = 1300.0f; //重さ

    public void MoveEnemy(MainActivity main, Enemy enemy, GameObject target)
    {
        if(!(0.0f < m_Speed) && !(m_Speed < 0.0f))
            m_Speed = m_InitialSpeed;
        else {
            m_Speed -= 1.0f;
        }

        if(enemy.m_CollisionTimer == 0)
        {
            enemy.m_MoveX = target.m_Texture.getX() - enemy.m_Texture.getX();
            enemy.m_MoveY = target.m_Texture.getY() - enemy.m_Texture.getY();
            //ベクトルを正規化
            main.normalizeVectorEnemy(enemy, enemy.m_MoveX,enemy.m_MoveY);

            //移動
            enemy.m_MoveX = enemy.m_MoveX * (m_Speed / 100.0f);
            enemy.m_MoveY = enemy.m_MoveY * (m_Speed / 100.0f);
        }

        enemy.m_oldPosX = enemy.m_PosX;
        enemy.m_oldPosY = enemy.m_PosY;

        enemy.m_PosX += enemy.m_MoveX;
        enemy.m_PosY += enemy.m_MoveY;
    }

    public void collisionTest(Player player, Enemy[] enemy)
    {
        for (int i = 0; i < enemy.length; i++)
        {
            //右判定
            //エネミー左
            if(player.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && player.m_Texture.getY() < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight()
                    && enemy[i].m_PosX + enemy[i].m_Texture.getWidth() < player.m_oldPosX)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;

                enemy[i].m_MoveX = player.m_MoveX * ex;
                enemy[i].m_MoveY = player.m_MoveY * ex;

                player.m_MoveX *= -1;
                player.m_MoveVecX *= -1;
            }

            //エネミー右
            else if(enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth()
                    && player.m_Texture.getY() < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
                    && enemy[i].m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight()
                    && player.m_oldPosX + player.m_Texture.getWidth() < enemy[i].m_PosX)
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;

                enemy[i].m_MoveX = player.m_MoveX * ex;
                enemy[i].m_MoveY = player.m_MoveY * ex;

                player.m_MoveX *= -1;
                player.m_MoveVecX *= -1;
            }

            /*
            //エネミー上
            else if(player.m_PosY < enemy[i].m_PosY + enemy[i].m_Texture.getHeight()
            && player.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
            && enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth())
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;

                enemy[i].m_MoveX = player.m_MoveX * ex;
                enemy[i].m_MoveY = player.m_MoveY * ex;

                player.m_MoveY *= -1;
                player.m_MoveVecY *= -1;
            }

            //エネミー下
            else if(enemy[i].m_PosY < player.m_PosY - player.m_Texture.getHeight()
                    && player.m_PosX < enemy[i].m_PosX + enemy[i].m_Texture.getWidth()
                    && enemy[i].m_PosX < player.m_PosX + player.m_Texture.getWidth())
            {
                enemy[i].m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy[i].m_Speed * enemy[i].m_Weight;

                float ex = energy1 / energy2;

                enemy[i].m_MoveX = player.m_MoveX * ex;
                enemy[i].m_MoveY = player.m_MoveY * ex;

                player.m_MoveY *= -1;
                player.m_MoveVecY *= -1;
            }
             */


        }
    }



}
