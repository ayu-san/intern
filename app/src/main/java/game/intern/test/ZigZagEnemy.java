package game.intern.test;

import android.widget.ImageView;

public class ZigZagEnemy extends Enemy {
    float m_InitialSpeed = 200.0f;
    float m_Speed = 0.0f;
    float m_Weight = 800.0f; // 重さ

    // 新しいフィールドを追加
    float m_DistanceMoved = 0.0f;
    float m_MaxDistance = 120.0f; // 一定の距離

    ZigZagEnemy(ImageView texture, float posX, float moveX, float moveY, int delayTime, int index, float speed, float weight) {
        super(texture, posX, moveX, moveY, delayTime, index, speed, weight); // 基底クラスのコンストラクタ呼び出し
    }

    @Override
    public void MoveEnemy(Enemy enemy, GameObject target, int width) {
        if (enemy.m_CollisionTimer == 0) {
            enemy.m_MoveX = enemy.m_ConstMoveX;
            enemy.m_MoveY = enemy.m_ConstMoveY;

            if (enemy.m_MoveX != 0.0f || enemy.m_MoveY != 0.0f) {
                normalizeVectorEnemy(enemy, enemy.m_MoveX, enemy.m_MoveY);
            }

            // 移動距離を計算
            float distanceX = enemy.m_MoveX;
            float distanceY = enemy.m_MoveY;
            float distanceMoved = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            m_DistanceMoved += distanceMoved;

            // 一定の距離を超えたら方向を反転
            if (m_DistanceMoved >= m_MaxDistance) {
                enemy.m_ConstMoveX *= -1;
                m_DistanceMoved = 0.0f; // 移動距離をリセット
            }

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
            // 座標更新
            enemy.m_oldPosX = enemy.m_PosX;
            enemy.m_oldPosY = enemy.m_PosY;

            enemy.m_PosX += enemy.m_MoveX;
            enemy.m_PosY += enemy.m_MoveY;
        }
    }
}