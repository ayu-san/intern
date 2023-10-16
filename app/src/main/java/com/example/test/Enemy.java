package com.example.test;
import static java.lang.Double.isNaN;

import android.view.View;
import android.widget.ImageView;

import com.example.test.GameObject;
import com.example.test.MainActivity;
import java.util.ArrayList;
import com.example.test.Player;

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

    public void InitEnemy(Enemy enemy)
    {
        //オルドpos初期化
        enemy.m_oldPosX = enemy.m_PosX;
        enemy.m_oldPosY = enemy.m_PosY;
        //テクスチャ座標初期化
        enemy.m_Texture.setX(enemy.m_PosX);
        enemy.m_Texture.setY(enemy.m_PosY);

        //コンスタントMove初期化
        enemy.m_ConstMoveX = enemy.m_MoveX;
        enemy.m_ConstMoveY = enemy.m_MoveY;
    }

    public void SetConstValue(float x, float y)
    {
        m_ConstMoveX = x;
        m_ConstMoveY = y;
    }

    //画面外判定
    public void hitCheckEnemy(ArrayList<Enemy> enemies, Player player, int screenwidth,int screenheight) {
        //右
        if(!enemies.isEmpty())//リストが空ではない
        {
            //for (int i = enemies.size() - 1; i >= 0; i--) {
            for (int i = 0; i < enemies.size(); i++) {
                if(enemies.get(i).m_IsPlayerCollision) {
                    if (enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth() > screenwidth) {

//                enemies.get(i).m_PosX = screenwidth - enemies.get(i).m_Texture.getWidth();
//                enemies.get(i).m_MoveX *= -1;
//                enemies.get(i).m_MoveVecX *= -1;
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        break;
                    }

                    //左
                    if (enemies.get(i).m_PosX < 0) {
//                enemies.get(i).m_PosX = 0;
//                enemies.get(i).m_MoveX *= -1;
//                enemies.get(i).m_MoveVecX *= -1;
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        break;
                    }

                    //上
                    if (enemies.get(i).m_PosY < 0) {
//                enemies.get(i).m_PosY = 0;
//                enemies.get(i).m_MoveY *= -1;
//                enemies.get(i).m_MoveVecY *= -1;
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        break;
                    }

                    //下
                    if (enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight() > screenheight) {
//                enemies.get(i).m_PosY = screenheight - enemies.get(i).m_Texture.getHeight();
//                enemies.get(i).m_MoveY *= -1;
//                enemies.get(i).m_MoveVecY *= -1;
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public void SetTimer(int time)
    {
        m_DisplayTimer = time;
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
                        && player.m_PosX + player.m_Texture.getWidth() < enemies.get(i).m_oldPosX) {
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
            //エネミー右
            if(enemies.get(i).m_PosX < player.m_PosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3
                    && player.m_Texture.getY() < enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight()
                    && enemies.get(i).m_PosY < player.m_Texture.getY() + player.m_Texture.getHeight()
                    && player.m_oldPosX + player.m_Texture.getWidth() - player.m_Texture.getWidth() /3 < enemies.get(i).m_PosX)
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
                //player.m_MoveX *= -1;
                //player.m_MoveVecX *= -1;

            }

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
