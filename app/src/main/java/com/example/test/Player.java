package com.example.test;
import com.example.test.GameObject;

import static java.lang.Double.TYPE;
import static java.lang.Double.isNaN;

import java.util.ArrayList;

public class Player extends GameObject
{
    float m_InitialSpeed = 700.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_holdValue;
    int m_ChargeLevel = 0;//チャージ速度

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



    @Override
    public void collisionTest(Player player, ArrayList<Enemy> enemies) {
       // if (!enemies.isEmpty())//リストが空ではない
        {
            for (int i = 0; i < enemies.size(); i++) {
                //右判定
                //エネミー左
                if (player.m_PosX + player.m_Texture.getWidth() / 3 < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                        && player.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                        && enemies.get(i).m_PosY < player.m_PosY + player.m_Texture.getHeight()
                        && enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth() < player.m_oldPosX + player.m_Texture.getWidth() / 3) {
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
                        ex = 0.3f;
                        ex2 = 0.3f;
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

                    //player.m_MoveX *= -1;
                    //player.m_MoveVecX *= -1;
                }

                //エネミー右
                if (enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() / 3
                        && player.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                        && enemies.get(i).m_PosY < player.m_PosY + player.m_Texture.getHeight()
                        && player.m_oldPosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() / 3 < enemies.get(i).m_PosX) {
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
                        ex = 0.3f;
                        ex2 = 0.3f;
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

                    //player.m_MoveX *= -1;
                    //player.m_MoveVecX *= -1;

                }

                //エネミー上
                if (player.m_PosY < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                        && player.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                        && enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth()
                        && enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight() < player.m_oldPosY) {
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
                        ex = 0.3f;
                        ex2 = 0.3f;
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
                    //player.m_MoveY *= -1;
                    //player.m_MoveVecY *= -1;

                }

                //エネミー下
                if (enemies.get(i).m_PosY < player.m_PosY + player.m_Texture.getHeight() - 10.0f
                        && player.m_PosX < enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth()
                        && enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth()
                        && player.m_oldPosY + player.m_Texture.getHeight() < enemies.get(i).m_PosY) {
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
                        ex = 0.3f;
                        ex2 = 0.3f;
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
                    //player.m_MoveY *= -1;
                    //player.m_MoveVecY *= -1;

                }


            }
        }
        //else {


    }


}
