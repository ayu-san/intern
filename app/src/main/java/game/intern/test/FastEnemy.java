package game.intern.test;
import static java.lang.Double.isNaN;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class FastEnemy extends Enemy
{
    float m_DestinationX;
    float m_DestinationSpeed;
    float m_InitialSpeed = 1200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 500.0f; //重さ
    float m_ConstMoveX;//m_Moveの初期化状態を保持する変数
    float m_ConstMoveY;//m_Moveの初期化状態を保持する変数

    boolean m_IsPlayerCollision;
    int m_InvincivleTime;

    FastEnemy(ImageView texture, float posX, float moveX, float moveY, int delayTime, int index, float speed, float weight)
    {
        super(texture,posX,moveX,moveY,delayTime,index,speed,weight);
        m_InitialSpeed = 2000.0f;
    }

    public void SetPlayerCollision()
    {
        m_IsPlayerCollision = true;
    }
    public void PullInvincivleTime(Enemy enemy)
    {
        if(enemy.m_InvincivleTime != 0)
        {
            enemy.m_InvincivleTime--;
        }

        if(enemy.m_InvincivleTime < 0)
        {
            enemy.m_InvincivleTime = 0;
        }
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width)
    {
        if (enemy.m_CollisionTimer == 0) {
            enemy.m_MoveX = target.m_Texture.getX() - enemy.m_Texture.getX();
            enemy.m_MoveY = target.m_Texture.getY() - enemy.m_Texture.getY();
            if (enemy.m_MoveX != 0.0f && enemy.m_MoveY != 0.0f && !isNaN(enemy.m_MoveX) && !isNaN(enemy.m_MoveY)) {
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
            //enemy.m_MoveX = 3.0f;
            //enemy.m_MoveY = 0.0f;
        } else {
            enemy.m_Speed = 0.0f;
        }

        if (0 < m_DisplayTimer) {
            m_DisplayTimer--;
        } else {
            m_DisplayTimer = 0;
            //座標更新
            if (enemy.m_oldPosX != enemy.m_PosX) {
                //進んでいるなら更新
                enemy.m_oldPosX = enemy.m_PosX;
            }

            if (enemy.m_oldPosY != enemy.m_PosY) {
                enemy.m_oldPosY = enemy.m_PosY;
            }

            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        }

        //reflect(enemy , width);
    }

/*
    public boolean isCoinCideEnemy(Enemy enemy, GameObject target)
    {
        {
            int radius = target.m_Texture.getHeight() /2;
            radius += 20.0f;

            float oldenemyX = enemy.m_oldPosX + (float)enemy.m_Texture.getWidth()/2;
            float oldenemyY = enemy.m_oldPosY + (float)enemy.m_Texture.getHeight()/2;

            float playerX = target.m_PosX + (float)target.m_Texture.getWidth()/2;
            float playerY = target.m_PosY + (float)target.m_Texture.getHeight()/2 - 40.0f;
            float enemyX  = enemy.m_PosX + (float)enemy.m_Texture.getWidth()/2;
            float enemyY  = enemy.m_PosY + (float)enemy.m_Texture.getHeight()/2;

            float oldDX = oldenemyX - playerX;
            float oldDY = oldenemyY - playerY;

            float dx = enemyX - playerX;
            float dy = enemyY - playerY;
            float calc = (float) Math.sqrt(dx * dx + dy * dy);
            float oldcalc = (float) Math.sqrt(oldDX * oldDX + oldDY * oldDY);

            if(radius < oldcalc && calc <= radius)
            { //当たった
                //補正
                return true;
            }
        }
        return false;
    }
*/

    private float calculateLength(float x, float y)
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalizeVectorEnemy(GameObject gameobject, float x, float y)
    {
        double length = calculateLength(x, y); //ベクトルの長さを計算
        gameobject.m_MoveX /= length;
        gameobject.m_MoveY /= length;
    }
/*
    public void SetConstValue(float x, float y)
    {
        m_ConstMoveX = x;
        m_ConstMoveY = y;
    }
*/
    //画面外判定
    public boolean hitCheckEnemy(ArrayList<Enemy> enemies, int screenwidth, int screenheight, CollideEffect collideEffect, Drawable drawable) {
        long duration = 500;
        //右
        if(!enemies.isEmpty())//リストが空ではない
        {
            //for (int i = enemies.size() - 1; i >= 0; i--) {
            for (int i = 0; i < enemies.size(); i++) {
                if(enemies.get(i).m_IsPlayerCollision) {
                    if (enemies.get(i).m_PosX + enemies.get(i).m_Texture.getWidth() > screenwidth)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,500,500,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                    //左
                    else if (enemies.get(i).m_PosX < 0)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,500,500,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

                    //上
                    else if (enemies.get(i).m_PosY < 0)
                    {
                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,500,500,duration);
                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
                        enemies.remove(i);
                        return true;
                    }

//                    //下
//                    else if (enemies.get(i).m_PosY + enemies.get(i).m_Texture.getHeight() > screenheight)
//                    {
//                        collideEffect.collideEffect((int)enemies.get(i).m_PosX+enemies.get(i).m_Texture.getWidth()/2,
//                                (int)enemies.get(i).m_PosY+enemies.get(i).m_Texture.getHeight()/2,drawable,500,500,duration);
//                        enemies.get(i).m_Texture.setVisibility(View.INVISIBLE);
//                        enemies.remove(i);
//                        return true;
//                    }

                }
            }
        }
        return false;
    }

/*
    public void SetTimer(int time)
    {
        m_DisplayTimer = time;
    }
*/

    @Override
    public boolean CollisionCircleEnemy(Player player, Enemy enemy, CollideEffect collideEffect, Drawable drawable)
    {
        int radius = player.m_Texture.getWidth() /2;
        radius += 90.0f;

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

        //if(player.m_CollisionTimer == 0) {
        {
            //if(radius < oldcalc && calc <= radius)

            if ((enemy.m_InvincivleTime == 0) && (calc <= radius)) { //当たった
                //めり込まないように補正する
                player.m_PosX = player.m_oldPosX;
                player.m_PosY = player.m_oldPosY;
                enemy.m_PosX = enemy.m_oldPosX;
                enemy.m_PosY = enemy.m_oldPosY;

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
//public void CollisionCircleEnemy(Player player, ArrayList<Enemy> enemies, CollideEffect collideEffect, Drawable drawable) {
//    for (int i = 0; i < enemies.size(); i++) {
//        int radius = player.m_Texture.getWidth() / 2;
//        radius += 50.0f;
//
//        float playerX = player.m_PosX + (float) player.m_Texture.getWidth() / 2;
//        float playerY = player.m_PosY + (float) player.m_Texture.getHeight() / 2 - 40.0f;
//        float enemyX = enemies.get(i).m_PosX + (float) enemies.get(i).m_Texture.getWidth() / 2;
//        float enemyY = enemies.get(i).m_PosY + (float) enemies.get(i).m_Texture.getHeight() / 2;
//
//        float dx = enemyX - playerX;
//        float dy = enemyY - playerY;
//        float calc = (float) Math.sqrt(dx * dx + dy * dy);
//
//        if (calc <= radius) { //当たった
//            if (!player.m_IsColliding && !enemies.get(i).m_IsColliding) {
//                //補完
//                player.m_PosX = player.m_oldPosX;
//                player.m_PosY = player.m_oldPosY;
//                enemies.get(i).m_PosX = enemies.get(i).m_oldPosX;
//                enemies.get(i).m_PosY = enemies.get(i).m_oldPosY;
//
//                player.m_IsColliding = true;
//                enemies.get(i).m_IsColliding = true;
//
//                // 衝突位置を計算
//                float collisionX = (float) (enemyX - dx * (-0.5 + radius / calc));
//                float collisionY = (float) (enemyY - dy * (-0.5 + radius / calc));
//
//                // エフェクトを追加
//                collideEffect.collideEffect((int) collisionX, (int) collisionY, drawable, 400, 400, 400);
//
//                // 衝突後の速度ベクトルを調整
//                // ここで速度ベクトルの調整を行う
//                float energy1 = player.m_Speed * player.m_Weight;
//                float energy2 = enemies.get(i).m_Speed * enemies.get(i).m_Weight;
//
//                float ex;
//                float ex2;
//
//                if (energy1 != 0.0f && energy2 != 0.0f) {
//                    ex = energy1 / energy2;
//                    ex2 = energy2 / energy1;
//                } else //Playerのスピードが0の時にも入る
//                {
//                    ex = 1.0f;
//                    ex2 = 5.0f;
//                }
//
//                float preserveMoveX = enemies.get(i).m_MoveX;
//                float preserveMoveY = enemies.get(i).m_MoveY;
//
//                //正規化処理 敵のノックバック処理
//                if (player.m_MoveX == 0.0f) {
//                    float vecX = -(player.m_PosX - enemies.get(i).m_PosX);
//                    float vecY = -(player.m_PosY - enemies.get(i).m_PosY);
//
//                    float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);
//
//                    if (vectorLength > 0.0f) {
//                        // ベクトルの長さが0でない場合に正規化を行う
//                        float normalizedX = vecX / vectorLength;
//                        float normalizedY = vecY / vectorLength;
//
//                        // ベクトルの反転を保持したまま正規化されたベクトルを使用
//                        enemies.get(i).m_MoveX = normalizedX * 3.0f;
//                        enemies.get(i).m_MoveY = normalizedY * 3.0f;
//                    } else
//                    {
//                        // ベクトルの長さが0の場合は正規化を行えません
//                        // 長さが0の場合、ベクトルの方向は定義できません
//                        // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
//                        enemies.get(i).m_MoveX = 0.0f; // 例: デフォルト値を0に設定
//                        enemies.get(i).m_MoveY = 0.0f;
//                    }
//                } else
//                {
//                    if(player.m_CollisionTimer != 0)
//                    //ノックバック中
//                    {
//                        if (!isNaN(player.m_MoveX * ex / 8))
//                            enemies.get(i).m_MoveX = -player.m_MoveX * ex / 8;
//
//                        if (!isNaN(player.m_MoveY * ex / 8))
//                            enemies.get(i).m_MoveY = -player.m_MoveY * ex / 8;
//                    }
//                    else
//                    {
//                        if (!isNaN(player.m_MoveX * ex / 8))
//                            enemies.get(i).m_MoveX = player.m_MoveX * ex / 8;
//
//                        if (!isNaN(player.m_MoveY * ex / 8))
//                            enemies.get(i).m_MoveY = player.m_MoveY * ex / 8;
//                    }
//                }
//
//                //プレイヤーのノックバック処理
//                if (preserveMoveX == 0.0f)
//                {
//                    float vecX = -(enemies.get(i).m_PosX - player.m_PosX);
//                    float vecY = -(enemies.get(i).m_PosY - player.m_PosY);
//
//                    float vectorLength = (float) Math.sqrt(vecX * vecX + vecY * vecY);
//
//                    if (vectorLength > 0.0f) {
//                        // ベクトルの長さが0でない場合に正規化を行う
//                        float normalizedX = vecX / vectorLength;
//                        float normalizedY = vecY / vectorLength;
//
//                        // ベクトルの反転を保持したまま正規化されたベクトルを使用
//                        player.m_MoveX = normalizedX * 3.0f;
//                        player.m_MoveY = normalizedY * 3.0f;
//                    } else {
//                        // ベクトルの長さが0の場合は正規化を行えません
//                        // 長さが0の場合、ベクトルの方向は定義できません
//                        // ここで適切なエラー処理を行うか、ベクトルのデフォルト値を設定します
//                        player.m_MoveX = 0.0f; // 例: デフォルト値を0に設定
//                        player.m_MoveY = 0.0f;
//                    }
//                }
//                else
//                {
//                    if(!isNaN(ex2))
//                    {
//                        if(enemies.get(i).m_CollisionTimer != 0)
//                        //ノックバック中
//                        {
//                            player.m_MoveX = -preserveMoveX * ex2 / 8;
//                            player.m_MoveY = -preserveMoveY * ex2 / 8;
//                        }
//                        else
//                        {
//                            player.m_MoveX = preserveMoveX * ex2 / 8;
//                            player.m_MoveY = preserveMoveY * ex2 / 8;
//
//                        }
//                    }
//                }
//
//                player.m_CollisionTimer = 60;
//                enemies.get(i).m_CollisionTimer = 60;
//                enemies.get(i).m_IsPlayerCollision = true;
//
//            }
//        } else {
//            player.m_IsColliding = false;
//            enemies.get(i).m_IsColliding = false;
//        }
//    }
//}
}