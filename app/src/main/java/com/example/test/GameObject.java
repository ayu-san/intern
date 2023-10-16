package com.example.test;

import android.widget.ImageView;

import java.util.ArrayList;

public class GameObject
{
        protected ImageView m_Texture;
        protected float m_PosX;
        protected float m_PosY;
        protected float m_MoveX;
        protected float m_MoveY;

        int m_CollisionTimer = 0;

        protected float m_oldPosX;
        protected float m_oldPosY;


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
                if(0 < gameObject.m_CollisionTimer)
                {
                        gameObject.m_CollisionTimer--;
                }

                else
                {
                        gameObject.m_CollisionTimer = 0;
                }
        }

        //当たり判定関数
        public void collisionTest(Player player, ArrayList<Enemy> enemy)
        {
        }

}
