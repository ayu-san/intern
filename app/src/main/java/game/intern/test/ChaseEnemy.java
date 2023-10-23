package game.intern.test;

import android.widget.ImageView;

public class ChaseEnemy extends Enemy //縦に落ちてくるだけの敵
{
    float m_InitialSpeed = 200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 300.0f; //重さ

    ChaseEnemy(ImageView texture, float posX, float moveX, float moveY, int delayTime, int index,float speed, float weight) {
        super(texture,posX,moveX,moveY,delayTime,index, speed,weight);//基底クラスのコンストラクタ呼び出し
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width)
    {
        if (enemy.m_CollisionTimer == 0)
        {
            // プレイヤーに向かって進むベクトルを計算
            float playerX = target.m_PosX;
            float playerY = target.m_PosY;

            float dx = playerX - enemy.m_PosX;
            float dy = playerY - enemy.m_PosY;

            // ベクトルを正規化
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance > 0.0f) {
                dx /= distance;
                dy /= distance;
            }

            enemy.m_MoveX = dx;
            enemy.m_MoveY = dy;

            // 移動
            if (0.0f < enemy.m_Speed) {
                enemy.m_Speed -= 20.0f;

                enemy.m_MoveX = enemy.m_MoveX * (enemy.m_Speed / 100.0f);
                enemy.m_MoveY = enemy.m_MoveY * (enemy.m_Speed / 100.0f);
            } else {
                enemy.m_MoveX = 0.0f;
                enemy.m_MoveY = 0.0f;

                enemy.m_Speed = enemy.m_InitialSpeed;
            }
        }

        if (0 < m_DisplayTimer)
        {
            m_DisplayTimer--;
        } else
        {
            m_DisplayTimer = 0;
            // 座標更新
            enemy.m_oldPosX = enemy.m_PosX;
            enemy.m_oldPosY = enemy.m_PosY;

            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        }
    }
}
