package com.example.test;

import android.widget.ImageView;

public class SideEnemy extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 400.0f;
    float m_Speed = 0.0f;
    float m_Weight = 800.0f; //重さ

    SideEnemy(ImageView texture, float posX, float posY, float moveX, float moveY, int delayTime, int index,float speed, float weight, float destinationX, float destinationSpeed)
    {
        super(texture,posX,moveX,moveY,delayTime,index, speed,weight);//基底クラスのコンストラクタ呼び出し
        m_PosY = posY;
        m_DestinationX = destinationX;//横の画面外から目的地のX軸まで横移動する
        m_DestinationSpeed = destinationSpeed;
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width)
    {
        if((enemy.m_DestinationSpeed < 0 && enemy.m_PosX < enemy.m_DestinationX) || (0 < enemy.m_DestinationSpeed && enemy.m_DestinationX < enemy.m_PosX))
        {
            if (enemy.m_CollisionTimer == 0) {
                enemy.m_MoveX = enemy.m_ConstMoveX;
                enemy.m_MoveY = enemy.m_ConstMoveY;

                if (enemy.m_MoveX != 0.0f || enemy.m_MoveY != 0.0f) {
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
            }

            if (0 < m_DisplayTimer) {
                m_DisplayTimer--;
            } else {
                m_DisplayTimer = 0;
                //座標更新
                enemy.m_oldPosX = enemy.m_PosX;
                enemy.m_oldPosY = enemy.m_PosY;

                enemy.m_PosX += enemy.m_MoveX;
                enemy.m_PosY += enemy.m_MoveY;
            }
        } else //目的地に達していない
        {

            if (enemy.m_CollisionTimer == 0)
            {
                enemy.m_MoveX = m_DestinationSpeed;
                enemy.m_MoveY = 0.0f;

                if (enemy.m_MoveX != 0.0f || enemy.m_MoveY != 0.0f)
                {
                    normalizeVectorEnemy(enemy, enemy.m_MoveX, 0.0f);
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
            }

            if (0 < m_DisplayTimer) {
                m_DisplayTimer--;
            } else {
                m_DisplayTimer = 0;
                //座標更新
                enemy.m_oldPosX = enemy.m_PosX;
                enemy.m_oldPosY = enemy.m_PosY;

                enemy.m_PosX += enemy.m_MoveX;
                enemy.m_PosY += enemy.m_MoveY;
            }

        }
    }

}
