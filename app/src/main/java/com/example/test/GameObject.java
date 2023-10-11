package com.example.test;

import android.widget.ImageView;

public class GameObject
{
        int m_CollisionTimer = 0;
        protected ImageView m_Texture;

        protected float m_oldPosX;
        protected float m_oldPosY;
        protected float m_PosX;
        protected float m_PosY;

        protected float m_MoveX;
        protected float m_MoveY;

        protected float m_InitialSpeed;//初速
        protected float m_Speed; //スピード
        protected float m_Weight; //重さ

        float m_MoveVecX; //移動ベクトルの正規化した値を入れる
        float m_MoveVecY; //移動ベクトルの正規化した値を入れる

        public void SetMove(float x, float y)
        {
            m_MoveX = x;
            m_MoveY = y;
        }

        public void PullCollisionTimer(GameObject gameObject)
        {
                if(gameObject.m_CollisionTimer != 0)
                {
                        gameObject.m_CollisionTimer--;
                }

                if(gameObject.m_CollisionTimer < 0)
                {
                        gameObject.m_CollisionTimer = 0;
                }
        }
}
