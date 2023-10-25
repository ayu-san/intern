package game.intern.test;

import static java.lang.Double.isNaN;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;

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

    @Override
    public void CollisionCircleEnemy(Player player, ArrayList<Enemy> enemies, CollideEffect collideEffect, Drawable drawable)
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            int radius = player.m_Texture.getWidth() /2;
            radius += 75.0f;

            float oldenemyX = enemies.get(i).m_oldPosX + (float)enemies.get(i).m_Texture.getWidth()/2;
            float oldenemyY = enemies.get(i).m_oldPosY + (float)enemies.get(i).m_Texture.getHeight()/2;

            float playerX = player.m_PosX + (float)player.m_Texture.getWidth()/2;
            float playerY = player.m_PosY + (float)player.m_Texture.getHeight()/2;
            float enemyX  = enemies.get(i).m_PosX + (float)enemies.get(i).m_Texture.getWidth()/2;
            float enemyY  = enemies.get(i).m_PosY + (float)enemies.get(i).m_Texture.getHeight()/2;

            float oldDX = oldenemyX - playerX;
            float oldDY = oldenemyY - playerY;

            float dx = enemyX - playerX;
            float dy = enemyY - playerY;
            float calc = (float) Math.sqrt(dx * dx + dy * dy);
            float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

            //if(player.m_CollisionTimer == 0) {
            {
                //if(radius < oldcalc && calc <= radius)

                if (calc <= radius) { //当たった
                    //めり込まないように補正する
                    player.m_PosX = player.m_oldPosX;
                    player.m_PosY = player.m_oldPosY;
                    enemies.get(i).m_PosX = enemies.get(i).m_oldPosX;
                    enemies.get(i).m_PosY = enemies.get(i).m_oldPosY;

                    // 衝突位置を計算
                    float collisionX = (float) (enemyX - dx * (-0.5 + radius / calc));
                    float collisionY = (float) (enemyY - dy * (-0.5 + radius / calc));

                    //エフェクトを追加
                    collideEffect.collideEffect((int) collisionX, (int) collisionY, drawable, 400, 400, 400);

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
                        ex2 = 5.0f;
                    }

                    float preserveMoveX = enemies.get(i).m_MoveX;
                    float preserveMoveY = enemies.get(i).m_MoveY;

                    //正規化処理 敵のノックバック処理
                    if (player.m_MoveX == 0.0f) {
                        float vecX = -(player.m_PosX - enemies.get(i).m_PosX);
                        float vecY = -(player.m_PosY - enemies.get(i).m_PosY);

                        float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);

                        if (vectorLength > 0.0f) {
                            // ベクトルの長さが0でない場合に正規化を行う
                            float normalizedX = vecX / vectorLength;
                            float normalizedY = vecY / vectorLength;

                            // ベクトルの反転を保持したまま正規化されたベクトルを使用
                            enemies.get(i).m_MoveX = normalizedX * 3.0f;
                            enemies.get(i).m_MoveY = normalizedY * 3.0f;
                        } else
                        {
                            // ベクトルの長さが0の場合は正規化を行えません
                            // 長さが0の場合、ベクトルの方向は定義できません
                            // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
                            enemies.get(i).m_MoveX = 0.0f; // 例: デフォルト値を0に設定
                            enemies.get(i).m_MoveY = 0.0f;
                        }
                    }
                    else
                    {
                        if(player.m_CollisionTimer != 0)
                        //ノックバック中
                        {
                            if (!isNaN(player.m_MoveX * ex / 8))
                                enemies.get(i).m_MoveX = -player.m_MoveX * ex / 8;

                            if (!isNaN(player.m_MoveY * ex / 8))
                                enemies.get(i).m_MoveY = -player.m_MoveY * ex / 8;
                        }
                        else
                        {
                            if (!isNaN(player.m_MoveX * ex / 8))
                                enemies.get(i).m_MoveX = player.m_MoveX * ex / 8;

                            if (!isNaN(player.m_MoveY * ex / 8))
                                enemies.get(i).m_MoveY = player.m_MoveY * ex / 8;
                        }
                    }

                    //プレイヤーのノックバック処理
                    if (preserveMoveX == 0.0f)
                    {
                        float vecX = -(enemies.get(i).m_PosX - player.m_PosX);
                        float vecY = -(enemies.get(i).m_PosY - player.m_PosY);

                        float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);

                        if (vectorLength > 0.0f) {
                            // ベクトルの長さが0でない場合に正規化を行う
                            float normalizedX = vecX / vectorLength;
                            float normalizedY = vecY / vectorLength;

                            // ベクトルの反転を保持したまま正規化されたベクトルを使用
                            player.m_MoveX = normalizedX * 3.0f;
                            player.m_MoveY = normalizedY * 3.0f;
                        } else {
                            // ベクトルの長さが0の場合は正規化を行えません
                            // 長さが0の場合、ベクトルの方向は定義できません
                            // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
                            player.m_MoveX = 0.0f; // 例: デフォルト値を0に設定
                            player.m_MoveY = 0.0f;
                        }
                    }
                    else
                    {
                        if(enemies.get(i).m_CollisionTimer != 0)
                        //ノックバック中
                        {
                            player.m_MoveX = -preserveMoveX * ex2 / 8;
                            player.m_MoveY = -preserveMoveY * ex2 / 8;
                        }
                        else
                        {
                            player.m_MoveX = preserveMoveX * ex2 / 8;
                            player.m_MoveY = preserveMoveY * ex2 / 8;

                        }
                    }
                    enemies.get(i).m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                    player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                    enemies.get(i).m_IsPlayerCollision = true;

                }
            }
        }
    }


}
