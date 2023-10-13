package com.example.test;
import static java.lang.Double.isNaN;

import com.example.test.Enemy;

public class VerticalEnemy extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 800.0f; //重さ

    @Override
    public void MoveEnemy(MainActivity main, Enemy enemy, GameObject target, int width)
    {
        if(enemy.m_CollisionTimer == 0)
        {
            enemy.m_MoveX = 0.0f;
            enemy.m_MoveY = 1.0f;

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
        }

        enemy.m_oldPosX = enemy.m_PosX;
        enemy.m_oldPosY = enemy.m_PosY;

        enemy.m_PosX += enemy.m_MoveX;
        enemy.m_PosY += enemy.m_MoveY;
    }

}