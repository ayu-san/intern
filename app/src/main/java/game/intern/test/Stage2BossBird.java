package game.intern.test;

import static java.lang.Double.isNaN;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Stage2BossBird extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 400.0f;
    float m_Speed = 0.0f;
    float m_Weight = 800.0f; //重さ

    Stage2BossBird(ImageView texture, float posX, float posY, float moveX, float moveY, int delayTime, int index, float speed, float weight, float destinationX, float destinationSpeed)
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
            } else {
                enemy.m_Speed = 0.0f;
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
            } else {
                enemy.m_Speed = 0.0f;
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
    public boolean CollisionCircleEnemy(Player player, Enemy enemy,CollideEffect collideEffect, Drawable drawable)
    {
        int radius = player.m_Texture.getWidth() /2;
        radius += 160.0f;

        float oldenemyX = enemy.m_oldPosX + (float)enemy.m_Texture.getWidth()/2;
        float oldenemyY = enemy.m_oldPosY + (float)enemy.m_Texture.getHeight()/2;

        float playerX = player.m_PosX + (float)player.m_Texture.getWidth()/2;
        float playerY = player.m_PosY + (float)player.m_Texture.getHeight()/2;
        float enemyX  = enemy.m_PosX + (float)enemy.m_Texture.getWidth()/2;
        float enemyY  = enemy.m_PosY + (float)enemy.m_Texture.getHeight()/2;

        float oldDX = oldenemyX - playerX;
        float oldDY = oldenemyY - playerY;

        float dx = enemyX - playerX;
        float dy = enemyY - playerY;
        float calc = (float) Math.sqrt(dx * dx + dy * dy);
        float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

        boolean isEnemyUp;

        //if(player.m_CollisionTimer == 0) {
        {
            //if(radius < oldcalc && calc <= radius)

            if ((enemy.m_InvincivleTime == 0) && (calc <= radius)) { //当たった
                //めり込まないように補正する
                player.m_PosX = player.m_oldPosX;
                player.m_PosY = player.m_oldPosY;
                enemy.m_PosX = enemy.m_oldPosX;
                enemy.m_PosY = enemy.m_oldPosY;

                if(enemy.m_PosY <= player.m_PosY)
                {
                    isEnemyUp = true;
                } else {
                    isEnemyUp = false;
                }

                // 衝突位置を計算
                float collisionX = (float) (enemyX - dx * (-0.5 + radius / calc));
                float collisionY = (float) (enemyY - dy * (-0.5 + radius / calc));

                //エフェクトを追加
                collideEffect.collideEffect((int) collisionX, (int) collisionY, drawable, 400, 400, 400);

                float energy1 = player.m_Speed * player.m_Weight;
                float energy2 = enemy.m_Speed * enemy.m_Weight;

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

                float preserveMoveX = enemy.m_MoveX;
                float preserveMoveY = enemy.m_MoveY;

                //正規化処理 敵のノックバック処理
                if (player.m_MoveX == 0.0f && player.m_MoveY == 0.0f) {
                    float vecX = -(player.m_PosX - enemy.m_PosX);
                    float vecY = -(player.m_PosY - enemy.m_PosY);

                    float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);

                    if (vectorLength > 0.0f) {
                        // ベクトルの長さが0でない場合に正規化を行う
                        float normalizedX = vecX / vectorLength;
                        float normalizedY = vecY / vectorLength;

                        // ベクトルの反転を保持したまま正規化されたベクトルを使用
                        enemy.m_MoveX = normalizedX * 3.0f;
                        enemy.m_MoveY = normalizedY * 3.0f;
                    } else
                    {
                        // ベクトルの長さが0の場合は正規化を行えません
                        // 長さが0の場合、ベクトルの方向は定義できません
                        // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
                        enemy.m_MoveX = 0.0f; // 例: デフォルト値を0に設定
                        enemy.m_MoveY = 0.0f;
                    }
                }
                else
                {
                    if(player.m_CollisionTimer == 0)
                    {
                        if(enemy.m_CollisionTimer == 0)
                        {
                            if (!isNaN(player.m_MoveX * ex / 4))
                                enemy.m_MoveX = player.m_MoveX * ex / 4;

                            if (!isNaN(player.m_MoveY * ex / 4))
                                enemy.m_MoveY = player.m_MoveY * ex / 4;
                        }
                        else
                        {
                            if (!isNaN(player.m_MoveX * ex / 4))
                                enemy.m_MoveX = player.m_MoveX * ex / 4;

                            if (!isNaN(player.m_MoveY * ex / 4))
                                enemy.m_MoveY = player.m_MoveY * ex / 4;
                        }
                    }
                    //ノックバック中
                    else
                    {
                        if(enemy.m_CollisionTimer == 0)
                        {
                            if (!isNaN(player.m_MoveX * ex / 8))
                                enemy.m_MoveX = -player.m_MoveX * ex / 2.5f;

                            if (!isNaN(player.m_MoveY * ex / 8))
                                enemy.m_MoveY = -player.m_MoveY * ex / 2.5f;
                        }
                        else
                        {
                            if (!isNaN(player.m_MoveX * ex / 4))
                                enemy.m_MoveX = player.m_MoveX * ex / 4;

                            if (!isNaN(player.m_MoveY * ex / 4))
                                enemy.m_MoveY = player.m_MoveY * ex / 4;
                        }
                    }
                }

                //プレイヤーのノックバック処理
                if (preserveMoveX == 0.0f && preserveMoveY == 0.0f)
                {
                    float vecX = -(enemy.m_PosX - player.m_PosX);
                    float vecY = -(enemy.m_PosY - player.m_PosY);

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
                    //プレイヤー
                    if(player.m_CollisionTimer == 0)
                    {
                        if(enemy.m_CollisionTimer == 0)
                        {
                            if (!isNaN(preserveMoveX * ex2 / 8))
                                player.m_MoveX = preserveMoveX * ex2 / 8;
                            if (!isNaN(preserveMoveY * ex2 / 8))
                                player.m_MoveY = preserveMoveY * ex2 / 8;
                        }
                        else
                        {
                            if (!isNaN(preserveMoveX * ex2 / 2.5f))
                                player.m_MoveX = -preserveMoveX * ex2 / 2.5f;

                            if (!isNaN(preserveMoveY * ex2 / 2.5f))
                                player.m_MoveY = -preserveMoveY * ex2 / 2.5f;
                        }

                        //応急処置
                        if(enemy.getClass() != Enemy.class && enemy.getClass() != Stage4BossDragon.class && !isEnemyUp)//エネミーが下
                        {
                            if (!isNaN(preserveMoveX * ex2 / 8))
                                player.m_MoveX = -preserveMoveX * ex2 / 8;
                            if (!isNaN(preserveMoveY * ex2 / 8))
                                player.m_MoveY = -preserveMoveY * ex2 / 8;
                        }
                    }
                    //ノックバック中
                    else
                    {
                        if(enemy.m_CollisionTimer == 0)
                        {
                            if (!isNaN(preserveMoveX * ex2 / 8))
                                player.m_MoveX = preserveMoveX * ex2 / 8;
                            if (!isNaN(preserveMoveY * ex2 / 8))
                                player.m_MoveY = preserveMoveY * ex2 / 8;
                        }
                        else
                        {
                            if (!isNaN(preserveMoveX * ex2 / 2.5f))
                                player.m_MoveX = -preserveMoveX * ex2 / 2.5f;

                            if (!isNaN(preserveMoveY * ex2 / 2.5f))
                                player.m_MoveY = -preserveMoveY * ex2 / 2.5f;
                        }
                    }
                }
                enemy.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                player.m_CollisionTimer = 60;//約一秒間はプレイヤーとぶつかったらノックバックを受ける
                enemy.m_IsPlayerCollision = true;
                enemy.m_InvincivleTime = 10;
                return true;
            }
            return false;
        }
    }



}
