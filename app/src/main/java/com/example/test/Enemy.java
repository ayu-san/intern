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
            m_Speed -= 1.5f;
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

        enemy.m_PosX += enemy.m_MoveX;
        enemy.m_PosY += enemy.m_MoveY;
    }


}
