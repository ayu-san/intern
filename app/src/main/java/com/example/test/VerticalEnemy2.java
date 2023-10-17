package com.example.test;
import static java.lang.Double.isNaN;

import android.widget.ImageView;

import com.example.test.Enemy;

public class VerticalEnemy2 extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 300.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ

    VerticalEnemy2(ImageView texture,float posX, float moveX, float moveY, int delayTime, int index) {
        super(texture,posX,moveX,moveY,delayTime,index);//基底クラスのコンストラクタ呼び出し
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width)
    {
        if(enemy.m_CollisionTimer == 0)
        {
            if(enemy.m_MoveX != 0.0f || enemy.m_MoveY != 0.0f)
            {
                normalizeVectorEnemy(enemy, enemy.m_MoveX,enemy.m_MoveY);
            }
            //ベクトルを正規化

            //移動
            if(0.0f < enemy.m_Speed)
            {
                enemy.m_Speed -= 30.0f;

                enemy.m_MoveX = enemy.m_MoveX * (enemy.m_Speed / 100.0f);
                enemy.m_MoveY = enemy.m_MoveY * (enemy.m_Speed / 100.0f);
            } else
            {
                enemy.m_MoveX = m_ConstMoveX;
                enemy.m_MoveY = m_ConstMoveY;

                enemy.m_Speed = enemy.m_InitialSpeed;
            }
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
    }

}
