package game.intern.test;

import static java.lang.Double.isNaN;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Player extends GameObject
{
    float m_InitialSpeed = 700.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_holdValue;
    int m_ChargeLevel = 1000;//チャージ速度

    int m_EnemyKilledNumber = 0;

    public void SetSpeed(float sp)
    {
        m_Speed = sp;
    }

    public void AddEnemyKilled()
    {
        m_EnemyKilledNumber++;
    }

    public int GetEnemyKilledNumber()
    {
        return m_EnemyKilledNumber;
    }



    public void CollisionCirclePlayer(Player player, ArrayList<Enemy> enemies, CollideEffect collideEffect, Drawable drawable)
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            int radius = player.m_Texture.getWidth() /2;
            radius += 50.0f;

            float oldplayerX = player.m_oldPosX + (float)player.m_Texture.getWidth()/2;
            float oldplayerY = player.m_oldPosY + (float)player.m_Texture.getHeight()/2;

            float playerX = player.m_PosX + (float)player.m_Texture.getWidth()/2;
            float playerY = player.m_PosY + (float)player.m_Texture.getHeight()/2;
            float enemyX  = enemies.get(i).m_PosX + (float)enemies.get(i).m_Texture.getWidth()/2;
            float enemyY  = enemies.get(i).m_PosY + (float)enemies.get(i).m_Texture.getHeight()/2;

            float oldDX = enemyX - oldplayerX;
            float oldDY = enemyY - oldplayerY;

            float dx = enemyX - playerX;
            float dy = enemyY - playerY;
            float calc = (float) Math.sqrt(dx * dx + dy * dy);
            float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

            if(radius < oldcalc && calc <= radius)
            { //当たった
                //めり込まないように補正する
                player.m_PosX = player.m_oldPosX;
                player.m_PosY = player.m_oldPosY;

                // 衝突位置の座標を計算
                float collisionX = (float) (playerX + dx * (-0.5+radius / calc));
                float collisionY = (float) (playerY + dy * (-0.5+radius / calc));

                //エフェクトを追加
                collideEffect.collideEffect((int) collisionX, (int) collisionY,drawable,400,400,400);

                enemies.get(i).SetPlayerCollision();
                enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;

                float ex;
                float ex2;

                if (energy1 != 0.0f && energy2 != 0.0f) {
                    ex = energy1 / energy2;
                    ex2 = energy2 / energy1;
                } else {
                    ex = 1.3f;
                    ex2 = 1.3f;
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
        }
    }

}